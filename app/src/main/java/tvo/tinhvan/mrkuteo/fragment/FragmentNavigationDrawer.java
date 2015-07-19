package tvo.tinhvan.mrkuteo.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import tvo.tinhvan.mrkuteo.activity.R;
import tvo.tinhvan.mrkuteo.adapter.NavDrawerAdapter;
import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.support.DataCategories;

public class FragmentNavigationDrawer extends Fragment implements AdapterView.OnItemClickListener {
    public static final String PREF_FILE_NAME = "pref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    setItemClickListener mListener;

    DrawerLayout mDrawerLayout;
    ArrayList<DataCategories> mArrayNavDrawers;
    NavDrawerAdapter adapter;

    DataCategories dataCategories;
    ListView lvSliderCategories;
    LinearLayout linearLayout;

    boolean mUserLearnedDrawer;
    boolean mFromSavedInstanceState;

    ShopDatabase db;

    private ActionBarDrawerToggle mDrawerToggle;

    public static void saveToPreferences(Context context, String preferenceName, String preferencenValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferencenValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String preferencenValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, preferencenValue);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (setItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement setItemClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
        db = new ShopDatabase(getActivity());
        db.openDatabase();
        dataCategories = new DataCategories();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        lvSliderCategories = (ListView) view.findViewById(R.id.list_slider_categories);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_drawer);

        addNavDrawerItems();

        return view;
    }



    public void setUp(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer) {
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, String.valueOf(true));
                    mUserLearnedDrawer = true;
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        if (mUserLearnedDrawer && mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(getActivity().findViewById(R.id.frag_nav_drawer));
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    private void addNavDrawerItems() {
        mArrayNavDrawers = db.getListCategories();
        lvSliderCategories.addHeaderView(View.inflate(getActivity(), R.layout.header_categories, null));
        adapter = new NavDrawerAdapter(getActivity().getApplicationContext(), mArrayNavDrawers);
        lvSliderCategories.setAdapter(adapter);
        lvSliderCategories.setOnItemClickListener(this);
    }

    public void openNavDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeNavDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUp(mDrawerLayout);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDrawerLayout.closeDrawer(linearLayout);
            lvSliderCategories.setItemChecked(position, true);
            lvSliderCategories.setSelection(position);

            mListener.onItemClick(id);


    }

    public interface setItemClickListener {
        void onItemClick(long id);
    }
}
