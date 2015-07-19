package tvo.tinhvan.mrkuteo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.activity.R;
import tvo.tinhvan.mrkuteo.adapter.ProductsAdapter;
import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.support.DataProducts;

public class HomeFragment extends Fragment {

    onItemClickCallback callback;

    GridView gridHome;
    ArrayList<DataProducts> listAllProduct;
    ProductsAdapter adapter;

    ShopDatabase db;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (onItemClickCallback) activity;
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
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        gridHome = (GridView) view.findViewById(R.id.grid_home);
        listAllProduct = db.getAllProducts();
        adapter = new ProductsAdapter(getActivity(),R.layout.grid_item,listAllProduct);
        gridHome.setAdapter(adapter);

        gridHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.onClickCallback(id);
            }
        });

        return view;
    }

    public interface onItemClickCallback {
        void onClickCallback(long id);
    }
}
