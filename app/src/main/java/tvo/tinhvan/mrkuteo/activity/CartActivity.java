package tvo.tinhvan.mrkuteo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.adapter.CartAdapter;
import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.support.DataCart;
import tvo.tinhvan.mrkuteo.support.PathConst;

public class CartActivity extends Activity implements View.OnClickListener,CartAdapter.onSpinnerChangeItemSelected {

    ListView lv_cart;
    Button btn_payment;
    TextView txtTotalPricePro;

    ArrayList<DataCart> listDataCart;
    CartAdapter adapter;

    ShopDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = new ShopDatabase(this);
        db.openDatabase();

        txtTotalPricePro = (TextView) findViewById(R.id.txt_total_price_pro);
        lv_cart = (ListView) findViewById(R.id.lv_cart);
        btn_payment = (Button) findViewById(R.id.btn_payment);
        btn_payment.setOnClickListener(this);

        listDataCart = db.getListCart();
        adapter = new CartAdapter(this, R.layout.item_list_cart, listDataCart,this);
        lv_cart.setAdapter(adapter);
        registerForContextMenu(lv_cart);

        txtTotalPricePro.setText(String.valueOf(getTotalPrice()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        if (v.getId() == R.id.lv_cart) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(listDataCart.get(info.position).getName());
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_delete:
                db.deleteProductInCart(listDataCart.get(info.position).getForIdPro());
                listDataCart.remove(info.position);
                adapter.notifyDataSetChanged();
                txtTotalPricePro.setText(String.valueOf(getTotalPrice()));
                PathConst.COUNT_CART -= 1;
                return true;
            case R.id.menu_delete_all:
                listDataCart.clear();
                adapter.notifyDataSetChanged();
                db.deleteCart();
                txtTotalPricePro.setText(String.valueOf(getTotalPrice()));
                PathConst.COUNT_CART = 0;
                return true;
            default:
                return false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private int getTotalPrice() {
        int total = 0;
        View parent;
        for (int i = 0; i < lv_cart.getCount(); ++i) {
            parent = lv_cart.getAdapter().getView(i,null,null);
            TextView t = (TextView) parent.findViewById(R.id.txt_total_price_cart);
            total += Integer.parseInt(t.getText().toString());
        }
        return total;
    }

    private ArrayList<Integer> getQuantityPrice() {
        ArrayList<Integer> arr = new ArrayList<>();
        View parent;
        for (int i = 0; i < lv_cart.getCount(); ++i) {
            parent = lv_cart.getAdapter().getView(i, null, null);
            Spinner spinner = (Spinner) parent.findViewById(R.id.sp_cart);
            int value = Integer.parseInt(spinner.getSelectedItem().toString()) ;
            arr.add(value);
        }
        return arr;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
        intent.putExtra(PathConst.KEY_TOTAL_PRICE, getQuantityPrice());
        startActivity(intent);
    }

    @Override
    public void onSpinnerChangeItem(int totalPrice) {
        txtTotalPricePro.setText(String.valueOf(totalPrice));
    }
}
