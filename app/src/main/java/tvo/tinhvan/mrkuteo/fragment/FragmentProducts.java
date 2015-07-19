package tvo.tinhvan.mrkuteo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.activity.R;
import tvo.tinhvan.mrkuteo.activity.ShopActivity;
import tvo.tinhvan.mrkuteo.adapter.ProductsAdapter;
import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.support.DataProducts;
import tvo.tinhvan.mrkuteo.support.PathConst;

public class FragmentProducts extends Fragment {

    onGridItemClickCallback callback;

    ProductsAdapter adapter;
    ArrayList<DataProducts> productArrayList;

    GridView grid_product;

    ShopDatabase db;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (onGridItemClickCallback) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ShopDatabase(getActivity());
        db.openDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        Bundle bundle = getArguments();
        long id  = bundle.getLong(PathConst.KEY_ID_CAT);
        String input = bundle.getString(PathConst.KEY_SEARCH_NAME);

        grid_product = (GridView) view.findViewById(R.id.grid_products);

        productArrayList = db.getListProducts((int) id,input);
        adapter = new ProductsAdapter(getActivity(),R.layout.grid_item,productArrayList);
        grid_product.setAdapter(adapter);

        grid_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.onGridItemClick(id);
            }
        });

        return view;
    }

    public interface onGridItemClickCallback {
        void onGridItemClick(long id);
    }
}
