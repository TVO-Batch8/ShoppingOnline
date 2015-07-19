package tvo.tinhvan.mrkuteo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;

import java.io.IOException;

import tvo.tinhvan.mrkuteo.database.ShopDatabase;
import tvo.tinhvan.mrkuteo.task.LoadDataAsyncTask;

public class LoadDataActivity extends Activity {

    ShopDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);

        initDatabase();

        LoadDataAsyncTask task = new LoadDataAsyncTask(this, new LoadDataAsyncTask.onResultCallback() {
            @Override
            public void onResult() {
                startActivity(new Intent(LoadDataActivity.this, ShopActivity.class));
            }
        });

        task.execute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void initDatabase() {
        db = new ShopDatabase(this);
        try {
            db.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            db.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
