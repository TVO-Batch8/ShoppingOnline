package tvo.tinhvan.mrkuteo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.support.DataCart;
import tvo.tinhvan.mrkuteo.support.DataPayment;
import tvo.tinhvan.mrkuteo.support.PathConst;

public class PaymentActivity extends Activity implements View.OnClickListener {

    Button btnConfirm, btnCancel;
    EditText editName, editAddress, editPhone, editMail;
    RadioGroup rdgPayment;

    ShopDatabase db;

    String name, address, mail, date, phone;

    //Mảng số lượng từng sản phẩm đã mua
    ArrayList<Integer> arrQuantity;
    //Mảng tên và đơn giá sản phẩm đã mua
    ArrayList<DataCart> arrCart;
    //Mảng tổng giá trị của mỗi sản phẩm
    ArrayList<Integer> arrTotalPricePro;
    //Mảng lưu trữ thông tin khách hàng
    ArrayList<DataPayment> listInfoCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initDatabase();
        initView();
        setUpPaymentItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void initView() {
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        editAddress = (EditText) findViewById(R.id.enter_address);
        editPhone = (EditText) findViewById(R.id.enter_phone);
        editName = (EditText) findViewById(R.id.enter_name);
        editMail = (EditText) findViewById(R.id.enter_email);

        rdgPayment = (RadioGroup) findViewById(R.id.rdg_payment);
        rdgPayment.check(R.id.rb_cash_payment);

        listInfoCustomer = db.getInforCustomer();
        if (!listInfoCustomer.isEmpty()) {
            DataPayment dataPayment = listInfoCustomer.get(0);
            editName.setText(dataPayment.getName());
            editAddress.setText(dataPayment.getAddress());
            editPhone.setText(String.valueOf(dataPayment.getPhone()));
            editMail.setText(dataPayment.getMail());

            editName.setEnabled(false);
            editAddress.setEnabled(false);
            editPhone.setEnabled(false);
            editMail.setEnabled(false);
        }

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initDatabase() {
        db = new ShopDatabase(this);
        db.openDatabase();
    }

    private void setUpPaymentItem() {
        arrTotalPricePro = new ArrayList<>();
        arrQuantity = getIntent().getIntegerArrayListExtra(PathConst.KEY_TOTAL_PRICE);
        arrCart = db.getListCart();
        for (int i = 0; i < arrCart.size(); ++i) {
            int total = arrQuantity.get(i) * arrCart.get(i).getPrice();
            arrTotalPricePro.add(total);
        }
    }

    private void insertInfoAndPayment() {

        date = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss").format(Calendar.getInstance().getTime());

        db.insertPayment(name, address, Integer.parseInt(phone), mail);
        for (int i = 0; i < arrCart.size(); ++i) {
            String name = arrCart.get(i).getName();
            int quantity = arrQuantity.get(i);
            int price = arrCart.get(i).getPrice();
            int total = arrTotalPricePro.get(i);
            db.insertPaymentItem(name, quantity, price, total, date);
        }
        db.deleteCart();
    }

    private boolean checkNetwork() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    private boolean checkInfoCustomer() {

        name = editName.getText().toString();
        phone = editPhone.getText().toString();
        address = editAddress.getText().toString();
        mail = editMail.getText().toString();

        return !(name.equals("") || phone.equals("") || address.equals("") || mail.equals(""));
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(PaymentActivity.this, ShopActivity.class);
        intent.putExtra(PathConst.KEY_TOTAL_PRODUCT, "");

        if (v.getId() == R.id.btn_confirm) {
            if (checkNetwork()) {
                if (checkInfoCustomer()) {

                    insertInfoAndPayment();

                    PathConst.COUNT_CART = 0;

                    Toast.makeText(PaymentActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else
                    Toast.makeText(PaymentActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PaymentActivity.this, "Thanh toán thất bại! \n Vui lòng kết nối internet", Toast.LENGTH_SHORT).show();
            }
        } else {
            startActivity(new Intent(PaymentActivity.this, CartActivity.class));
        }
    }
}
