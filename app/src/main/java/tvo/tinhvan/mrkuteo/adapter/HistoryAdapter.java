package tvo.tinhvan.mrkuteo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.activity.R;
import tvo.tinhvan.mrkuteo.support.DataHistory;

public class HistoryAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<DataHistory> arr;
    int layout;

    public HistoryAdapter(Context context, int layout, ArrayList<DataHistory> arrayList) {
        this.mContext = context;
        this.layout = layout;
        this.arr = arrayList;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.txt_his_name);
            holder.price = (TextView) convertView.findViewById(R.id.txt_his_price);
            holder.quantity = (TextView) convertView.findViewById(R.id.txt_his_quantity);
            holder.total = (TextView) convertView.findViewById(R.id.txt_his_total);
            holder.date = (TextView) convertView.findViewById(R.id.txt_his_date);

            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();

        DataHistory data = arr.get(position);
        holder.name.setText(data.getName());
        holder.price.setText(String.valueOf(data.getPrice()));
        holder.quantity.setText(String.valueOf(data.getQuantity()));
        holder.total.setText(String.valueOf(data.getTotal()));
        holder.date.setText(data.getDate());

        return convertView;
    }

    private class ViewHolder {
        TextView name,price,quantity,total,date;
    }
}
