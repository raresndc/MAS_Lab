package com.example.mihai.restapiexample.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mihai.restapiexample.R;
import com.example.mihai.restapiexample.SSLConnection;
import com.example.mihai.restapiexample.model.DataObject;
import com.example.mihai.restapiexample.model.ObjectCollection;
import com.example.mihai.restapiexample.web.adapter.RestApiWebAdapter;
import com.example.mihai.restapiexample.web.service.DataService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void getObject(View view) {
        SSLConnection.allowAllSSL();
        DataService dataService = new RestApiWebAdapter().getData();
        dataService.getDataObjects(new Callback<List<DataObject>>() {
            @Override
            public void success(List<DataObject> dataObjects, Response response) {
                Log.d("success", "Value is:" + dataObjects.size());

                for (DataObject dataObject : dataObjects) {
                    String title = dataObject.title;
                    Toast.makeText(MainActivity.this, "Title is: " + title, Toast.LENGTH_LONG).show();
                    Log.d("success", "Title is: " + title);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("failure", error.getMessage());
            }
        });
    }

}
