package tvo.tinhvan.mrkuteo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.activity.R;
import tvo.tinhvan.mrkuteo.fragment.Rotate3DAnimation;
import tvo.tinhvan.mrkuteo.support.ClearFormat;
import tvo.tinhvan.mrkuteo.support.DataCategories;

public class NavDrawerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DataCategories> arrDrawer;

    public NavDrawerAdapter(Context context, ArrayList<DataCategories> arr) {
        mContext = context;
        arrDrawer = arr;
    }

    @Override
    public int getCount() {
        return arrDrawer.size();
    }

    @Override
    public Object getItem(int position) {
        return arrDrawer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrDrawer.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.icon_list);
        TextView txt = (TextView) convertView.findViewById(R.id.txt_category);

        img.setImageBitmap(ClearFormat.convertBytesToBitmap(arrDrawer.get(position).getImage()));
        txt.setText(arrDrawer.get(position).getName());

        Animation animation = new Rotate3DAnimation(90.0f, 0.0f, 100.0f, false, convertView);
        animation.setDuration(500l);
        convertView.startAnimation(animation);

        return convertView;
    }
}
