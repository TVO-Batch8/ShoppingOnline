package tvo.tinhvan.mrkuteo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.support.ClearFormat;
import tvo.tinhvan.mrkuteo.support.DataProducts;
import tvo.tinhvan.mrkuteo.support.PathConst;

public class DetailProductActivity extends Activity implements View.OnClickListener{

    ShopDatabase db;

    ImageView img_product;
    TextView txt_name, txt_price, txt_description;
    Button btn_buy;
    WebView wv_html;

    long idPro;

    ArrayList<DataProducts> listDetailProduct;

    String name;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        initDatabase();
        initView();
        showUI();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void initView() {
        img_product = (ImageView) findViewById(R.id.img_product);

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_price = (TextView) findViewById(R.id.txt_price);
        txt_description = (TextView) findViewById(R.id.txt_description);

        btn_buy = (Button) findViewById(R.id.btn_buy);

        wv_html = (WebView) findViewById(R.id.wv_html);

        idPro = getIntent().getLongExtra(PathConst.KEY_ID_PRO, -1);

        listDetailProduct = db.getListDetailProducts((int) idPro);

        btn_buy.setOnClickListener(this);
    }

    private void initDatabase() {
        db = new ShopDatabase(this);
        db.openDatabase();
    }

    private void showUI() {
        DataProducts dataProducts = listDetailProduct.get(0);

        name = dataProducts.getName();
        price = dataProducts.getPrice();

        img_product.setImageBitmap(ClearFormat.convertBytesToBitmap(dataProducts.getImage()));
        txt_name.setText(name);
        txt_price.setText(String.valueOf(price)+" VND");
        txt_description.setText(dataProducts.getDescription());
        wv_html.loadUrl(PathConst.URL_IMAGE_PRO + dataProducts.getHtml());
    }

    @Override
    public void onClick(View v) {
        if (!db.checkNameCart((int) idPro)) {
            db.insertCart(name, price, (int) idPro);
            Toast.makeText(DetailProductActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }else
            Toast.makeText(DetailProductActivity.this, "Sản phẩm đã được mua", Toast.LENGTH_SHORT).show();

    }

}
