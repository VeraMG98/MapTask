package in.wangziq.fitnessrecorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<MyModel> listRes;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context aContext, ArrayList<MyModel> listCity) {
        this.listRes = listCity;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listRes.size();
    }

    @Override
    public Object getItem(int position) {
        return listRes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list, null);
            holder = new ViewHolder();
            holder.time = convertView.findViewById(R.id.id_timer);
            holder.distanse = convertView.findViewById(R.id.id_km);

            convertView.setTag(holder);
        } else {
            holder = new ViewHolder();

            holder.time = convertView.findViewById(R.id.id_timer);
            holder.distanse = convertView.findViewById(R.id.id_km);
            holder = (ViewHolder) convertView.getTag();
        }

        holder.time.setText(listRes.get(position).getTime ());
        holder.distanse.setText(listRes.get(position).getDistanse ());
        return convertView;
    }

    static class ViewHolder {
        TextView time;
        TextView distanse;
    }

}