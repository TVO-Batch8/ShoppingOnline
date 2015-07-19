package tvo.tinhvan.mrkuteo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import tvo.tinhvan.mrkuteo.activity.R;
import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.fragment.Rotate3DAnimation;
import tvo.tinhvan.mrkuteo.support.ClearFormat;

public class SearchAdapter extends CursorAdapter {

    public SearchAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.search_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.img_search);
        TextView txt_search_name = (TextView) view.findViewById(R.id.txt_search_name);
        TextView txt_search_price = (TextView) view.findViewById(R.id.txt_search_price);

        imageView.setImageBitmap(ClearFormat.convertBytesToBitmap(cursor.getBlob(cursor.getColumnIndex(ShopDatabase.PRO_IMAGE))));
        txt_search_name.setText(cursor.getString(cursor.getColumnIndex(ShopDatabase.PRO_NAME)));
        txt_search_price.setText(cursor.getString(cursor.getColumnIndex(ShopDatabase.PRO_PRICE)));

        Animation animation = new Rotate3DAnimation(180.0f,0.0f,100.0f,false,view);
        animation.setDuration(800l);
        view.startAnimation(animation);
    }
}
