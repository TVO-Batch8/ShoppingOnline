package tvo.tinhvan.mrkuteo.task;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;

import java.net.Proxy;

import tvo.tinhvan.mrkuteo.activity.R;
import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.support.ClearFormat;
import tvo.tinhvan.mrkuteo.support.PathConst;


public class LoadDataAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String URL = "http://shoppingonline.somee.com/ServicerData.asmx?WSDL";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String METHOD_NAME_CATEGORIES = "getlistCategories";
    private static final String METHOD_NAME_PRODUCTS = "getlistProduct";
    private static final String SOAP_ACTION_CATEGORIES = NAMESPACE + METHOD_NAME_CATEGORIES;
    private static final String SOAP_ACTION_PRODUCTS = NAMESPACE + METHOD_NAME_PRODUCTS;
    private static final String KEY_CATEGORIES = "KEY_CATEGORIES";
    private static final String KEY_PRODUCTS = "KEY_PRODUCTS";

    private static final String CAT_NAME = "CAT_NAME";
    private static final String CAT_IMAGES = "CAT_IMAGES";
    private static final String CAT_ID = "ID";

    private static final String PRO_NAME = "PRO_NAME";
    private static final String PRO_DESCRIPTION = "PRO_DESCRIPTION";
    private static final String PRO_PRICE = "PRO_PRICE";
    private static final String PRO_IMAGE = "PRO_IMAGE";
    private static final String PRO_HTML = "PRO_HTML";
    private static final String PRO_KEYWORD_ASCII = "PRO_KEYWORD_ASCII";
    private static final String CATE_ID = "CATE_ID";

    onResultCallback callback;

    Context mContext;
    Dialog dialog;

    ShopDatabase db;

    public LoadDataAsyncTask(Context context, onResultCallback callback) {
        this.mContext = context;
        this.callback = callback;
        dialog = new Dialog(mContext);

        db = new ShopDatabase(mContext);
        db.openDatabase();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.load_data_dialog);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setTitle("Loading...!");
        dialog.show();

    }

    @Override
    protected Void doInBackground(Void... params) {

        loadDataFromServer(METHOD_NAME_CATEGORIES, SOAP_ACTION_CATEGORIES, KEY_CATEGORIES);
        loadDataFromServer(METHOD_NAME_PRODUCTS, SOAP_ACTION_PRODUCTS, KEY_PRODUCTS);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.cancel();
        callback.onResult();
    }

    private void loadDataFromServer(String methodName, String soapAction, String key) {
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);

        HttpTransportSE transportSE = new HttpTransportSE(Proxy.NO_PROXY, URL, 5000);
        transportSE.debug = true;
        transportSE.setXmlVersionTag("<!--?xml version=\\\"1.0\\\" encoding= \\\"UTF-8\\\" ?-->");

        try {
            transportSE.call(soapAction, envelope);
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "No connect Server");
        }
        SoapObject soapArray = null;
        try {
            soapArray = (SoapObject) envelope.getResponse();
        } catch (SoapFault soapFault) {
            Log.e("SoapFault", "ERROR: " + soapFault.getMessage());
        }

        if (soapArray != null) {
            switch (key) {
                case KEY_CATEGORIES:
                    insertToCategories(soapArray);
                    break;
                case KEY_PRODUCTS:
                    insertToProducts(soapArray);
                    break;
            }
        }

    }

    private void insertToCategories(SoapObject soapArray) {

        int size = soapArray.getPropertyCount();
        int count = db.getCountCategories();

        Log.e("SIZE ", size + "");
        Log.e("COUNT ", count + "");

        if (size != count) {
            db.deleteCategories();
            for (int i = 0; i < size; ++i) {
                SoapObject soapItem = (SoapObject) soapArray.getProperty(i);

                String catName = soapItem.getProperty(CAT_NAME).toString();
                String catImage = soapItem.getProperty(CAT_IMAGES).toString();
                String catId = soapItem.getProperty(CAT_ID).toString();

                byte[] image = convertToByteArray(catImage, PathConst.URL_IMAGE_CAT);

                db.insertCategories(Integer.parseInt(catId), catName, image);
            }
        }
    }

    private void insertToProducts(SoapObject soapArray) {

        int size = soapArray.getPropertyCount();
        int count = db.getCountProducts();

        Log.e("SIZE ", size + "");
        Log.e("COUNT ", count + "");

        if (size != count) {
            db.deleteProducts();
            for (int i = 0; i < size; ++i) {
                SoapObject soapItem = (SoapObject) soapArray.getProperty(i);

                String proName = soapItem.getProperty(PRO_NAME).toString();
                String proDescription = soapItem.getProperty(PRO_DESCRIPTION).toString();
                String proPrice = soapItem.getProperty(PRO_PRICE).toString();
                String proImage = soapItem.getProperty(PRO_IMAGE).toString();
                String proHtml = soapItem.getProperty(PRO_HTML).toString();
                String proKeyword = soapItem.getProperty(PRO_KEYWORD_ASCII).toString();
                String cateId = soapItem.getProperty(CATE_ID).toString();

                byte[] image = convertToByteArray(proImage, PathConst.URL_IMAGE_PRO);

                db.insertProducts(proName, proDescription, Integer.parseInt(proPrice), image, Integer.parseInt(cateId), proHtml, proKeyword);
            }
        }
    }

    private byte[] convertToByteArray(String image, String url) {
        Bitmap bitmap = ClearFormat.getImageFromURL(url + image);
        return ClearFormat.convertBitmapToBytes(bitmap);
    }

    public interface onResultCallback {
        void onResult();
    }
}



