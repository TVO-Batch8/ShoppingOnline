package tvo.tinhvan.mrkuteo.support;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class ClearFormat {

    //Get hình ảnh từ địa chỉ URL
    public static Bitmap getImageFromURL(String imageURL) {
        Bitmap bitmap = null;
        URL url;

        try {
            //Tạo và mở kết nối
            url = new URL(imageURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            int response = urlConnection.getResponseCode();

            //Kiểm tra kết nối
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //Chuyển đổi Bitmap thành mảng byte
    public static byte[] convertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    //Chuyển đổi mảng byte thành Bitmap
    public static Bitmap convertBytesToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    //Chuyển các kí tự unicode về non-unicode
    public static StringBuilder clearUnicode(String input) {
        String[] a = new String[]{"á", "ã", "ả", "ạ", "à", "ă", "ắ", "ằ", "ặ", "ẵ", "ẳ", "â", "ấ", "ầ", "ẫ", "ẩ", "ậ"};
        String[] o = new String[]{"ó", "õ", "ỏ", "ọ", "ò", "ô", "ố", "ồ", "ộ", "ỗ", "ổ", "ơ", "ớ", "ờ", "ỡ", "ở", "ợ"};
        String[] e = new String[]{"é", "ẽ", "ẻ", "ẹ", "è", "ê", "ế", "ề", "ệ", "ễ", "ể"};
        String[] u = new String[]{"ú", "ũ", "ủ", "ụ", "ù", "ư", "ứ", "ừ", "ự", "ữ", "ử"};
        String[] i = new String[]{"í", "ĩ", "ỉ", "ị", "ì"};
        String[] y = new String[]{"ý", "ỹ", "ỷ", "ỵ", "ỳ"};
        String[] d = new String[]{"đ"};

        StringBuilder result = null;

        for (int run = 0; run < input.length(); ++run) {
            String _temp = String.valueOf(input.charAt(run));
            if (Arrays.asList(a).contains(_temp)) {
                _temp = "a";
            }
            if (Arrays.asList(o).contains(_temp)) {
                _temp = "o";
            }
            if (Arrays.asList(e).contains(_temp)) {
                _temp = "e";
            }
            if (Arrays.asList(u).contains(_temp)) {
                _temp = "u";
            }
            if (Arrays.asList(i).contains(_temp)) {
                _temp = "i";
            }
            if (Arrays.asList(y).contains(_temp)) {
                _temp = "y";
            }
            if (Arrays.asList(d).contains(_temp)) {
                _temp = "d";
            }
            result.append(_temp);
        }
        return result;
    }
}
