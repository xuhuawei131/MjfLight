package jiayuan.huawei.com.mjflight.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.adapters.RingAdapter;
import jiayuan.huawei.com.mjflight.bean.RingAdapterBean;

public class RingListActivity extends AppCompatActivity {
    private ListView listview;
    private RingAdapter adapter;
    private List<RingAdapterBean> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_list);

        listview=(ListView)findViewById(R.id.listview);
        adapter=new RingAdapter(this,)
    }
}
