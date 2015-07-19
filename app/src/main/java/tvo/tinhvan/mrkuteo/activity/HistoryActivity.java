package tvo.tinhvan.mrkuteo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.adapter.HistoryAdapter;
import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.support.DataHistory;
import tvo.tinhvan.mrkuteo.support.DataPayment;

public class HistoryActivity extends Activity {

    TextView txtNameCus,txtAddressCus,txtPhoneCus,txtMailCus;
    ListView lvHistory;

    ArrayList<DataHistory> listPaymentHistory;
    ArrayList<DataPayment> listInfoCustomer;
    HistoryAdapter adapter;

    ShopDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initDatabase();
        initView();
        setUpinfoCustomer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    private void initView() {
        txtNameCus = (TextView) findViewById(R.id.txt_name_cus);
        txtAddressCus = (TextView) findViewById(R.id.txt_address_cus);
        txtPhoneCus = (TextView) findViewById(R.id.txt_phone_cus);
        txtMailCus = (TextView) findViewById(R.id.txt_mail_cus);

        lvHistory = (ListView) findViewById(R.id.lv_history);
        listPaymentHistory = db.getListPaymentItem();

        if (!listPaymentHistory.isEmpty()) {
            adapter = new HistoryAdapter(this,R.layout.item_list_history,listPaymentHistory);
            lvHistory.setAdapter(adapter);
        }

    }

    private void initDatabase() {
        db = new ShopDatabase(this);
        db.openDatabase();
    }

    private void setUpinfoCustomer() {
        listInfoCustomer = db.getInforCustomer();
        DataPayment data = listInfoCustomer.get(0);
        txtNameCus.setText(data.getName());
        txtPhoneCus.setText(String.valueOf(data.getPhone()));
        txtAddressCus.setText(data.getAddress());
        txtMailCus.setText(data.getMail());
    }
}
