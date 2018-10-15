package jiayuan.huawei.com.mjflight.services;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ControlerLightSix {
    private static final String TAG = "FlashlightController";
    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);
    private static final int DISPATCH_ERROR = 0;
    private static final int DISPATCH_OFF = 1;
    private static final int DISPATCH_AVAILABILITY_CHANGED = 2;
    private final CameraManager mCameraManager;
    private Handler mHandler;
    private final ArrayList<FlashlightListener> mListeners = new ArrayList<FlashlightListener>(1);
    private boolean mFlashlightEnabled;
    private String mCameraId;
    private boolean mCameraAvailable;
    private CameraDevice mCameraDevice;
    private CaptureRequest mFlashlightRequest;
    private CameraCaptureSession mSession;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private Context mContext;

    public ControlerLightSix(Context mContext) {
        this.mContext=mContext;
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        initialize();
    }

    public void initialize() {
        try {
            mCameraId = getCameraId();
        } catch (Throwable e) {
            return;
        }
        if (mCameraId != null) {
            ensureHandler();
            mCameraManager.registerAvailabilityCallback(mAvailabilityCallback, mHandler);
        }
    }

    public synchronized void setFlashlight(boolean enabled) {
        if (mFlashlightEnabled != enabled) {
            mFlashlightEnabled = enabled;
            postUpdateFlashlight();
        }
    }

    public void killFlashlight() {
        boolean enabled;
        synchronized (this) {
            enabled = mFlashlightEnabled;
        }
        if (enabled) {
            mHandler.post(mKillFlashlightRunnable);
        }
    }

    public synchronized boolean isAvailable() {
        return mCameraAvailable;
    }

    public void addListener(FlashlightListener l) {
        synchronized (mListeners) {
            cleanUpListenersLocked(l);
            mListeners.add(l);
        }
    }

    public void removeListener(FlashlightListener l) {
        synchronized (mListeners) {
            cleanUpListenersLocked(l);
        }
    }

    private synchronized void ensureHandler() {
        if (mHandler == null) {
            HandlerThread thread = new HandlerThread(TAG);
            thread.start();
            mHandler = new Handler(thread.getLooper());
        }
    }

    private void startDevice() throws CameraAccessException {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mCameraManager.openCamera(getCameraId(), new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {
                mCameraDevice = camera;
                postUpdateFlashlight();
            }

            @Override
            public void onDisconnected(CameraDevice camera) {
                if (mCameraDevice == camera) {
                    dispatchOff();
                    teardown();
                }
            }

            @Override
            public void onError(CameraDevice camera, int error) {
                if (camera == mCameraDevice || mCameraDevice == null) {
                    handleError();
                }
            }
        }, mHandler);
    }
    private void startSession() throws CameraAccessException {
        mSurfaceTexture = new SurfaceTexture(0,false);
        Size size = getSmallestSize(mCameraDevice.getId());
        mSurfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
        mSurface = new Surface(mSurfaceTexture);
        ArrayList<Surface> outputs = new ArrayList<Surface>(1);
        outputs.add(mSurface);
        mCameraDevice.createCaptureSession(outputs, new android.hardware.camera2.CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                mSession = session;
                postUpdateFlashlight();
            }
            @Override
            public void onConfigureFailed(CameraCaptureSession session) {
                if (mSession == null || mSession == session) {
                    handleError();
                }
            }
        }, mHandler);
    }
    private Size getSmallestSize(String cameraId) throws CameraAccessException {
        Size[] outputSizes = mCameraManager.getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                .getOutputSizes(SurfaceTexture.class);
        if (outputSizes == null || outputSizes.length == 0) {
            throw new IllegalStateException(
                    "Camera " + cameraId + "doesn't support any outputSize.");
        }
        Size chosen = outputSizes[0];
        for (Size s : outputSizes) {
            if (chosen.getWidth() >= s.getWidth() && chosen.getHeight() >= s.getHeight()) {
                chosen = s;
            }
        }
        return chosen;
    }
    private void postUpdateFlashlight() {
        ensureHandler();
        mHandler.post(mUpdateFlashlightRunnable);
    }
    private String getCameraId() throws CameraAccessException {
        String[] ids = mCameraManager.getCameraIdList();
        for (String id : ids) {
            CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
            Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
            if (flashAvailable != null && flashAvailable
                    && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                return id;
            }
        }
        return null;
    }
    private void updateFlashlight(boolean forceDisable) {
        try {
            boolean enabled;
            synchronized (this) {
                enabled = mFlashlightEnabled && !forceDisable;
            }
            if (enabled) {
                if (mCameraDevice == null) {
                    startDevice();
                    return;
                }
                if (mSession == null) {
                    startSession();
                    return;
                }
                if (mFlashlightRequest == null) {
                    CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(
                            CameraDevice.TEMPLATE_PREVIEW);
                    builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
                    builder.addTarget(mSurface);
                    CaptureRequest request = builder.build();
                    mSession.capture(request, null, mHandler);
                    mFlashlightRequest = request;
                }
            } else {
                if (mCameraDevice != null) {
                    mCameraDevice.close();
                    teardown();
                }
            }
        } catch (Exception e) {
            handleError();
        }
    }
    private void teardown() {
        mCameraDevice = null;
        mSession = null;
        mFlashlightRequest = null;
        if (mSurface != null) {
            mSurface.release();
            mSurfaceTexture.release();
        }
        mSurface = null;
        mSurfaceTexture = null;
    }
    private void handleError() {
        synchronized (this) {
            mFlashlightEnabled = false;
        }
        dispatchError();
        dispatchOff();
        updateFlashlight(true);
    }
    private void dispatchOff() {
        dispatchListeners(DISPATCH_OFF, false);
    }
    private void dispatchError() {
        dispatchListeners(DISPATCH_ERROR, false);
    }
    private void dispatchAvailabilityChanged(boolean available) {
        dispatchListeners(DISPATCH_AVAILABILITY_CHANGED, available);
    }
    private void dispatchListeners(int message, boolean argument) {
        synchronized (mListeners) {
            final int N = mListeners.size();
            boolean cleanup = false;
            for (int i = 0; i < N; i++) {
                FlashlightListener l = mListeners.get(i);
                if (l != null) {
                    if (message == DISPATCH_ERROR) {
                        l.onFlashlightError();
                    } else if (message == DISPATCH_OFF) {
                        l.onFlashlightOff();
                    } else if (message == DISPATCH_AVAILABILITY_CHANGED) {
                        l.onFlashlightAvailabilityChanged(argument);
                    }
                } else {
                    cleanup = true;
                }
            }
            if (cleanup) {
                cleanUpListenersLocked(null);
            }
        }
    }
    private void cleanUpListenersLocked(FlashlightListener listener) {
        for (int i = mListeners.size() - 1; i >= 0; i--) {
            FlashlightListener found = mListeners.get(i);
            if (found == null || found == listener) {
                mListeners.remove(i);
            }
        }
    }

    private final Runnable mUpdateFlashlightRunnable = new Runnable() {
        @Override
        public void run() {
            updateFlashlight(false);
        }
    };
    private final Runnable mKillFlashlightRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (this) {
                mFlashlightEnabled = false;
            }
            updateFlashlight(true);
            dispatchOff();
        }
    };
    private final CameraManager.AvailabilityCallback mAvailabilityCallback =
            new CameraManager.AvailabilityCallback() {
                @Override
                public void onCameraAvailable(String cameraId) {
                    if (cameraId.equals(mCameraId)) {
                        setCameraAvailable(true);
                    }
                }
                @Override
                public void onCameraUnavailable(String cameraId) {
                    if (cameraId.equals(mCameraId)) {
                        setCameraAvailable(false);
                    }
                }
                private void setCameraAvailable(boolean available) {
                    boolean changed;
                    synchronized (ControlerLightSix.this) {
                        changed = mCameraAvailable != available;
                        mCameraAvailable = available;
                    }
                    if (changed) {
                        dispatchAvailabilityChanged(available);
                    }
                }
            };
    public interface FlashlightListener {

        void onFlashlightOff();

        void onFlashlightError();

        void onFlashlightAvailabilityChanged(boolean available);
    }
}
