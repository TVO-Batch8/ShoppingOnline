package tvo.tinhvan.mrkuteo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tvo.tinhvan.mrkuteo.activity.R;
import tvo.tinhvan.mrkuteo.support.DataCart;

public class CartAdapter extends BaseAdapter {

    onSpinnerChangeItemSelected mListener;

    Context mContext;
    int layout;
    ArrayList<DataCart> arrDataCart;
    int[] arrQuantitySpin;
    List<Integer> listSpin;

    ViewHolder holder;

    public CartAdapter(Context context, int layout, ArrayList<DataCart> arrayList, onSpinnerChangeItemSelected listener) {
        this.mContext = context;
        this.layout = layout;
        this.arrDataCart = arrayList;
        this.arrQuantitySpin = mContext.getResources().getIntArray(R.array.quantity_product);
        this.mListener = listener;

        listSpin = new ArrayList<>(arrQuantitySpin.length);
        for (int anArrQuantitySpin : arrQuantitySpin) {
            listSpin.add(anArrQuantitySpin);
        }
    }

    @Override
    public int getCount() {
        return arrDataCart.size();
    }

    @Override
    public Object getItem(int position) {
        return arrDataCart.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder = new ViewHolder();
            holder.txt_name_cart = (TextView) convertView.findViewById(R.id.txt_name_cart);
            holder.txt_price_cart = (TextView) convertView.findViewById(R.id.txt_price_cart);
            holder.txt_total_price_cart = (TextView) convertView.findViewById(R.id.txt_total_price_cart);
            holder.spinner = (Spinner) convertView.findViewById(R.id.sp_cart);

            ArrayAdapter<Integer> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, listSpin);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            holder.spinner.setAdapter(adapter);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        DataCart dataCart = arrDataCart.get(position);
        holder.txt_name_cart.setText(dataCart.getName());
        holder.txt_price_cart.setText(String.valueOf(dataCart.getPrice()));
        holder.txt_total_price_cart.setText(String.valueOf(dataCart.getPrice()));

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                DataCart data = arrDataCart.get(position);
                int price = data.getPrice();
                int quantity = Integer.parseInt(listSpin.get(pos).toString());
                holder.txt_total_price_cart = (TextView) viewGroup.getChildAt(position).findViewById(R.id.txt_total_price_cart);
                holder.txt_total_price_cart.setText(String.valueOf(price * quantity));

                mListener.onSpinnerChangeItem(getTotalPrice(viewGroup));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }

    private int getTotalPrice(ViewGroup viewGroup) {
        int total = 0;
        for (int i = 0; i < arrDataCart.size(); ++i) {
            holder.txt_total_price_cart = (TextView) viewGroup.getChildAt(i).findViewById(R.id.txt_total_price_cart);
            total += Integer.parseInt(holder.txt_total_price_cart.getText().toString());
        }
        return total;
    }

    public interface onSpinnerChangeItemSelected {
        void onSpinnerChangeItem(int totalPrice);
    }

    private class ViewHolder {
        TextView txt_name_cart, txt_price_cart, txt_total_price_cart;
        Spinner spinner;
    }
}
