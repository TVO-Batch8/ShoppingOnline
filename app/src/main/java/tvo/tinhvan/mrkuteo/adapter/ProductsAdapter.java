package tvo.tinhvan.mrkuteo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.activity.R;
import tvo.tinhvan.mrkuteo.support.ClearFormat;
import tvo.tinhvan.mrkuteo.support.DataProducts;

public class ProductsAdapter extends BaseAdapter {

    Context mContext;
    int layout;
    ArrayList<DataProducts> productArrayList;

    public ProductsAdapter(Context context, int layout, ArrayList<DataProducts> arrayList) {
        this.mContext = context;
        this.layout = layout;
        this.productArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return productArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return productArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img_icon);
            holder.txt = (TextView) convertView.findViewById(R.id.txt_title);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.img.setImageBitmap(ClearFormat.convertBytesToBitmap(productArrayList.get(position).getImage()));
        holder.txt.setText(productArrayList.get(position).getName());

        return convertView;
    }

    private class ViewHolder {
        ImageView img;
        TextView txt;
    }
}
