package tvo.tinhvan.mrkuteo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.support.DataCart;
import tvo.tinhvan.mrkuteo.support.DataCategories;
import tvo.tinhvan.mrkuteo.support.DataHistory;
import tvo.tinhvan.mrkuteo.support.DataPayment;
import tvo.tinhvan.mrkuteo.support.DataProducts;

public class ShopDatabase extends SQLiteOpenHelper {

    public static final String PRO_NAME = "PRO_NAME";
    public static final String PRO_PRICE = "PRO_PRICE";
    public static final String PRO_IMAGE = "PRO_IMAGE";
    //Các cột bảng PRODUCTS
    public static final String PRO_ID = "PRO_ID";

    private static final String LOG_INSERT = "INSERT: ";
    private static final String DB_NAME = "SHOPPINGDB.s3db";
    private static final int VERSION = 1;
    private static final String SELECT = "select * from ";
    private static final String TABLE_CART = "CART";
    private static final String TABLE_CATEGORIES = "CATEGORIES";
    private static final String TABLE_PAYMENT = "PAYMENT";
    private static final String TABLE_PAYMENT_ITEMS = "PAYMENT_ITEMS";
    private static final String TABLE_PRODUCTS = "PRODUCTS";
    private static final String TABLE_PRODUCT_IMAGES = "PRODUCT_IMAGES";
    //Các cột bảng CART
    private static final String CAR_ID = "CAR_ID";
    private static final String CAR_NAME = "CAR_NAME";
    private static final String CAR_COUNT = "CAR_COUNT";
    private static final String CAR_PRICE = "CAR_PRICE";
    private static final String CAR_TOTAL_PRICE = "CAR_TOTAL_PRICE";
    private static final String FOR_CAR_PRO_ID = "FOR_PRO_ID";
    //Các cột bảng CATEGORIES
    private static final String CAT_ID = "CAT_ID";
    private static final String CAT_NAME = "CAT_NAME";
    private static final String CAT_IMAGE = "CAT_IMAGE";
    private static final String CAT_ID_SERVER = "CAT_ID_SERVER";
    //Các cột bảng PAYMENT
    private static final String PAY_ID = "PAY_ID";
    private static final String PAY_NAME = "PAY_NAME";
    private static final String PAY_ADDRESS = "PAY_ADDRESS";
    private static final String PAY_PHONE = "PAY_PHONE";
    private static final String PAY_EMAIL = "PAY_EMAIL";
    //Các cột bảng PAYMENT_ITEMS
    private static final String PIT_ID = "PIT_ID";
    private static final String PIT_NAME = "PIT_NAME";
    private static final String PIT_QUANTITY = "PIT_QUANTITY";
    private static final String PIT_PRICE = "PIT_PRICE";
    private static final String PIT_TOTAL_PRICE = "PIT_TOTAL_PRICE";
    private static final String PIT_DATE = "PIT_DATE";
    private static final String FOR_PAY_ID = "PAY_ID";

    private static final String PRO_DESCRIPTION = "PRO_DESCRIPTION";
    private static final String PRO_HTML = "PRO_HTML";
    private static final String PRO_KEYWORD_ASCII = "PRO_KEYWORD_ASCII";
    private static final String FOR_CATE_ID = "CATE_ID";

    //Các cột bảng PRODUCT_IMAGES
    private static final String PIM_ID = "PIM_ID";
    private static final String PIM_IMAGE = "PIM_IMAGE";
    private static final String FOR_PRO_ID = "PRO_ID";

    Context mContext;
    SQLiteDatabase db;

    public ShopDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

    public boolean checkDatabase() {
        boolean checkDB = false;
        try {
            String myPath = mContext.getDatabasePath(DB_NAME).getParent() + "/" + DB_NAME;
            //String myPath = "/data/data/" + mContext.getPackageName() + "/databases/MINIGAME.s3db";
            File f = new File(myPath);
            checkDB = f.exists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.e("CheckDB", checkDB + "");
        return checkDB;
    }

    public void copyDatabase() throws IOException {
        try {
            InputStream myInput = mContext.getAssets().open(DB_NAME);
            String outFileName = mContext.getDatabasePath(DB_NAME).getParent()
                    + "/" + DB_NAME;
            Log.e("DBLocation", outFileName);
            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openDatabase() {
        String myPath = mContext.getDatabasePath(DB_NAME).getParent() + "/"
                + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READONLY);
    }

    public void createDatabase() throws IOException {
        boolean doExist = checkDatabase();
        if (!doExist) {
            this.getReadableDatabase();
            try {
                copyDatabase();
                Log.e("Copy Database", "Copy Success!");
            } catch (IOException e) {
                throw new Error("Error copying database");
            } finally {
                this.close();
            }
            Log.e("createDB", "Create success!");
        } else
            Log.e("Copy Database", "Exist");
    }

    //Insert name và image vào bảng CATEGORIES
    public void insertCategories(int idServer, String name, byte[] image) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.putNull(CAT_ID);
        values.put(CAT_ID_SERVER, idServer);
        values.put(CAT_NAME, name);
        values.put(CAT_IMAGE, image);

        db.insertOrThrow(TABLE_CATEGORIES, null, values);
        db.close();
        Log.d(LOG_INSERT, "Insert Categories Success");
    }

    //Đưa dữ liệu vào bảng Products
    public void insertProducts(String name, String description, int price, byte[] image, int cate_id, String html, String keyword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.putNull(PRO_ID);
        values.put(PRO_NAME, name);
        values.put(PRO_DESCRIPTION, description);
        values.put(PRO_PRICE, price);
        values.put(PRO_IMAGE, image);
        values.put(FOR_CATE_ID, cate_id);
        values.put(PRO_HTML, html);
        values.put(PRO_KEYWORD_ASCII, keyword);

        db.insertOrThrow(TABLE_PRODUCTS, null, values);
        db.close();
        Log.d(LOG_INSERT, "Insert Products Success");
    }

    //Đưa dữ liệu trong giỏ hàng vào bảng Cart
    public void insertCart(String name, int price, int forProId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.putNull(CAR_ID);
        values.put(CAR_NAME, name);
        values.put(CAR_PRICE, price);
        values.put(FOR_CAR_PRO_ID, forProId);

        db.insertOrThrow(TABLE_CART, null, values);
        db.close();
        Log.d(LOG_INSERT, "Insert Cart Success");
    }

    //Lưu trữ thông tin khách hàng
    public void insertPayment(String name, String address, int phone, String mail) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.putNull(PAY_ID);
        values.put(PAY_NAME, name);
        values.put(PAY_ADDRESS, address);
        values.put(PAY_PHONE, phone);
        values.put(PAY_EMAIL, mail);

        db.insertOrThrow(TABLE_PAYMENT, null, values);
        db.close();
    }

    //Lưu lịch sử sản phẩm khách hàng đã mua
    public void insertPaymentItem(String name, int quantity, int price, int totalPrice, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.putNull(PIT_ID);
        values.put(PIT_NAME, name);
        values.put(PIT_QUANTITY, quantity);
        values.put(PIT_PRICE, price);
        values.put(PIT_TOTAL_PRICE, totalPrice);
        values.put(PIT_DATE, date);

        db.insertOrThrow(TABLE_PAYMENT_ITEMS, null, values);
        db.close();
    }

    //Lấy danh mục trong bảng CATEGORIES
    public ArrayList<DataCategories> getListCategories() {
        ArrayList<DataCategories> arrayList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELECT + TABLE_CATEGORIES, null);

        if (c.moveToFirst()) {
            do {
                DataCategories data = new DataCategories();
                data.setId(c.getInt(c.getColumnIndex(CAT_ID_SERVER)));
                data.setName(c.getString(c.getColumnIndex(CAT_NAME)));
                data.setImage(c.getBlob(c.getColumnIndex(CAT_IMAGE)));
                arrayList.add(data);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return arrayList;
    }

    //Lấy sản phẩm trong bảng Cart
    public ArrayList<DataCart> getListCart() {
        ArrayList<DataCart> arr = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELECT + TABLE_CART, null);

        if (c.moveToFirst()) {
            do {
                DataCart dataCart = new DataCart();
                dataCart.setForIdPro(c.getInt(c.getColumnIndex(FOR_CAR_PRO_ID)));
                dataCart.setName(c.getString(c.getColumnIndex(CAR_NAME)));
                dataCart.setPrice(c.getInt(c.getColumnIndex(CAR_PRICE)));
                arr.add(dataCart);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return arr;
    }

    //Lấy

    //Lấy tên danh mục theo id
    public String getNameCategories(int id) {
        String name = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CATEGORIES, new String[]{CAT_NAME}, CAT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (c.moveToFirst()) {
            name = c.getString(c.getColumnIndex(CAT_NAME));
        }
        c.close();
        db.close();
        return name;
    }

    //Lấy số dòng của bảng Categories
    public int getCountCategories() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELECT + TABLE_CATEGORIES, null);
        if (c.moveToFirst()) {
            count = c.getCount();
        }
        c.close();
        db.close();
        return count;
    }

    //Lấy số dòng của bảng Products
    public int getCountProducts() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELECT + TABLE_PRODUCTS, null);
        if (c.moveToFirst()) {
            count = c.getCount();
        }
        c.close();
        db.close();
        return count;
    }

    //Lấy danh sách sản phẩm tùy theo id hoặc chuỗi input từ search danh mục
    public ArrayList<DataProducts> getListProducts(int idServer, String input) {
        ArrayList<DataProducts> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        if (idServer > 0)
            c = db.query(TABLE_PRODUCTS, new String[]{PRO_NAME, PRO_IMAGE, PRO_ID}, FOR_CATE_ID + "=?", new String[]{String.valueOf(idServer)}, null, null, null);
        else
            c = db.query(TABLE_PRODUCTS, new String[]{PRO_NAME, PRO_IMAGE, PRO_ID}, PRO_KEYWORD_ASCII + " like '%" + input + "%'", null, null, null, null);
        if (c.moveToFirst()) {
            do {
                DataProducts data = new DataProducts();
                data.setId(c.getInt(c.getColumnIndex(PRO_ID)));
                data.setName(c.getString(c.getColumnIndex(PRO_NAME)));
                data.setImage(c.getBlob(c.getColumnIndex(PRO_IMAGE)));
                arr.add(data);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return arr;
    }

    public ArrayList<DataProducts> getAllProducts() {
        ArrayList<DataProducts> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELECT+TABLE_PRODUCTS,null);
        if (c.moveToFirst()) {
            do {
                DataProducts data = new DataProducts();
                data.setId(c.getInt(c.getColumnIndex(PRO_ID)));
                data.setName(c.getString(c.getColumnIndex(PRO_NAME)));
                data.setImage(c.getBlob(c.getColumnIndex(PRO_IMAGE)));
                arr.add(data);
            } while (c.moveToNext());
        }
        return arr;
    }

    //Lấy danh sách chi tiết của sản phẩm
    public ArrayList<DataProducts> getListDetailProducts(int id) {
        ArrayList<DataProducts> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PRODUCTS, new String[]{PRO_NAME, PRO_IMAGE, PRO_PRICE, PRO_HTML, PRO_DESCRIPTION}, PRO_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (c.moveToFirst()) {
            DataProducts data = new DataProducts();
            data.setName(c.getString(c.getColumnIndex(PRO_NAME)));
            data.setImage(c.getBlob(c.getColumnIndex(PRO_IMAGE)));
            data.setDescription(c.getString(c.getColumnIndex(PRO_DESCRIPTION)));
            data.setPrice(c.getInt(c.getColumnIndex(PRO_PRICE)));
            data.setHtml(c.getString(c.getColumnIndex(PRO_HTML)));
            arr.add(data);
        }
        c.close();
        db.close();
        return arr;
    }

    //Lấy thông tin của khách hàng
    public ArrayList<DataPayment> getInforCustomer() {
        ArrayList<DataPayment> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELECT + TABLE_PAYMENT, null);
        if (c.moveToFirst()) {
            DataPayment data = new DataPayment();
            data.setName(c.getString(c.getColumnIndex(PAY_NAME)));
            data.setAddress(c.getString(c.getColumnIndex(PAY_ADDRESS)));
            data.setPhone(c.getInt(c.getColumnIndex(PAY_PHONE)));
            data.setMail(c.getString(c.getColumnIndex(PAY_EMAIL)));
            arr.add(data);
        }
        c.close();
        db.close();
        return arr;
    }

    //Lấy danh sách các sản phẩm đã mua
    public ArrayList<DataHistory> getListPaymentItem() {
        ArrayList<DataHistory> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        //Cursor c = db.rawQuery(SELECT+TABLE_PAYMENT_ITEMS,null);
        Cursor c = db.query(TABLE_PAYMENT_ITEMS, null, null, null, null, null, PIT_ID + " DESC");
        if (c.moveToFirst()) {
            do {
                DataHistory data = new DataHistory();
                data.setName(c.getString(c.getColumnIndex(PIT_NAME)));
                data.setPrice(c.getInt(c.getColumnIndex(PIT_PRICE)));
                data.setQuantity(c.getInt(c.getColumnIndex(PIT_QUANTITY)));
                data.setTotal(c.getInt(c.getColumnIndex(PIT_TOTAL_PRICE)));
                data.setDate(c.getString(c.getColumnIndex(PIT_DATE)));
                arr.add(data);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return arr;
    }

    //Kiểm tra sản phẩm có tồn tại trong giỏ hàng hay không
    public boolean checkNameCart(int id) {
        boolean check = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CART, null, FOR_CAR_PRO_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (c.moveToFirst()) {
            check = true;
        }
        c.close();
        return check;
    }

    //Xóa hết các dòng bảng Categories
    public void deleteCategories() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CATEGORIES, null, null);
        db.close();
    }

    //Xóa hết các dòng bảng Products
    public void deleteProducts() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PRODUCTS, null, null);
        db.close();
    }

    //Xóa sản phẩm trong giỏ hàng
    public void deleteProductInCart(int forID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CART, FOR_CAR_PRO_ID + "=?", new String[]{String.valueOf(forID)});
        db.close();
    }

    //Xóa tất cả sản phẩm trong giỏ hàng
    public void deleteCart() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }

    public Cursor searchProducts(String input) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        if (input == null || input.length() == 0) {
            cursor = db.query(TABLE_PRODUCTS, new String[]{PRO_ID + " as _id," + PRO_NAME + "," + PRO_IMAGE + "," + PRO_PRICE + "," + PRO_ID}, null, null, null, null, null);
        } else {
            cursor = db.query(true, TABLE_PRODUCTS, new String[]{PRO_ID + " as _id," + PRO_NAME + "," + PRO_IMAGE + "," + PRO_PRICE + "," + PRO_ID}, PRO_KEYWORD_ASCII + " like '%" + input + "%'", null, null, null, null, null);
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
