package tvo.tinhvan.mrkuteo.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import tvo.tinhvan.mrkuteo.adapter.SearchAdapter;
import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.fragment.FragmentNavigationDrawer;
import tvo.tinhvan.mrkuteo.fragment.FragmentProducts;
import tvo.tinhvan.mrkuteo.fragment.HomeFragment;
import tvo.tinhvan.mrkuteo.support.PathConst;

public class ShopActivity extends FragmentActivity implements FragmentNavigationDrawer.setItemClickListener,
        FragmentProducts.onGridItemClickCallback,
        HomeFragment.onItemClickCallback,
        SearchView.OnQueryTextListener,
        SearchView.OnCloseListener,
        SearchView.OnSuggestionListener {

    ActionBar actionBar;
    FragmentManager fragmentManager;
    FragmentNavigationDrawer fragment;

    ShopDatabase db;
    Cursor cursor;

    SearchView searchView;

    TextView txtQuantityPro;
    ImageView imgCart;

    boolean isOpenNavDrawer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setLogo(R.drawable.shopping);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        fragmentManager = getFragmentManager();

        Fragment fragmentHome = new HomeFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragmentHome);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

        fragment = (FragmentNavigationDrawer) fragmentManager.findFragmentById(R.id.frag_nav_drawer);
        fragment.setUp((DrawerLayout) findViewById(R.id.layout_drawer));

        initDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shop, menu);

        MenuItem cart = menu.findItem(R.id.action_cart);
        txtQuantityPro = (TextView) cart.getActionView().findViewById(R.id.txt_quantity_pro);

        imgCart = (ImageView) cart.getActionView().findViewById(R.id.img_cart);

        if (PathConst.COUNT_CART != 0)
            txtQuantityPro.setText(String.valueOf(PathConst.COUNT_CART));
        else
            txtQuantityPro.setText("");

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopActivity.this, CartActivity.class));
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setOnSuggestionListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (!isOpenNavDrawer)
                    fragment.openNavDrawer();
                else
                    fragment.closeNavDrawer();
                isOpenNavDrawer = !isOpenNavDrawer;
                break;
            case R.id.action_cart:
                break;
            case R.id.action_history:
                startActivity(new Intent(this,HistoryActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initDatabase() {
        db = new ShopDatabase(this);
        db.openDatabase();
    }

    private void displaySearchProducts(String input) {
        cursor = db.searchProducts(input);
        if (cursor.getCount() > 0) {
            SearchAdapter adapter = new SearchAdapter(this, cursor, 0);
            searchView.setSuggestionsAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(long id) {

        replaceFragmentProduct(id, "");
    }

    private void replaceFragmentProduct(long id, String input) {
        String nameCategories = db.getNameCategories((int) id);

        Bundle bundle = new Bundle();
        bundle.putLong(PathConst.KEY_ID_CAT, id);
        bundle.putString(PathConst.KEY_SEARCH_NAME, input);

        Fragment fragment = new FragmentProducts();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

        actionBar.setTitle(nameCategories);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public void onGridItemClick(long id) {
        Intent intent = new Intent(ShopActivity.this, DetailProductActivity.class);
        intent.putExtra(PathConst.KEY_ID_PRO, id);
        startActivityForResult(intent, PathConst.REQUEST_CODE_1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PathConst.REQUEST_CODE_1 && resultCode == RESULT_OK) {
            txtQuantityPro.setText(String.valueOf(++PathConst.COUNT_CART));
        }
    }

    @Override
    public boolean onClose() {
        displaySearchProducts("");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        replaceFragmentProduct(-1, query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        displaySearchProducts(newText);
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return true;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        long idProduct = cursor.getInt(cursor.getColumnIndex(ShopDatabase.PRO_ID));
        Intent intent = new Intent(ShopActivity.this, DetailProductActivity.class);
        intent.putExtra(PathConst.KEY_ID_PRO, idProduct);
        startActivity(intent);
        return true;
    }

    @Override
    public void onClickCallback(long id) {
        Intent intent = new Intent(ShopActivity.this, DetailProductActivity.class);
        intent.putExtra(PathConst.KEY_ID_PRO, id);
        startActivityForResult(intent, PathConst.REQUEST_CODE_1);
    }
}
