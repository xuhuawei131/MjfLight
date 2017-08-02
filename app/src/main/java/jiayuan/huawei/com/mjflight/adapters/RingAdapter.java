package jiayuan.huawei.com.mjflight.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jiayuan.huawei.com.mjflight.R;
import jiayuan.huawei.com.mjflight.bean.RingAdapterBean;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class RingAdapter extends BaseAdapter {
    private List<RingAdapterBean> arrayList;
    private LayoutInflater inflater;
    public RingAdapter(Context context,List<RingAdapterBean> arrayList){
        this.inflater=LayoutInflater.from(context);
        this.arrayList=arrayList;
    }
    @Override
    public int getCount() {
        return arrayList==null?0:arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.adapter_layout_ring,parent,false);
        }

        TextView textView= (TextView) convertView.findViewById(R.id.text_title);
        ImageView image_nike= (ImageView) convertView.findViewById(R.id.image_nike);
        RingAdapterBean bean=arrayList.get(position);

        textView.setText(bean.title);
        if(bean.isSelected){
            image_nike.setImageResource(R.drawable.conversation_icon_msg_toggle_false);
        }else{
            image_nike.setImageResource(R.drawable.conversation_icon_msg_toggle_true);
        }
        return convertView;
    }
}
