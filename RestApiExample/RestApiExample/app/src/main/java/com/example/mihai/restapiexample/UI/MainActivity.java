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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void getObject(View view)
    {
        SSLConnection.allowAllSSL();
        DataService dataService = new RestApiWebAdapter().getData();
        dataService.getDataObjects(new Callback<ObjectCollection>() {
            @Override
            public void success(ObjectCollection objectCollection, Response response) {
                Log.d("success","Value is:" + objectCollection.items.length);
                /* The json file retrieved from server - you can adapt the model to any json config
                * { "valori":
                      [
                        { "value":"Testing1"},
                       { "value":"Testing2"}
                      ]
                    }
                * */
                for (DataObject dataObject:objectCollection.items) {
                    String value = dataObject.value;
                    Toast.makeText(MainActivity.this, "Value is:" + value, Toast.LENGTH_LONG).show();
                    Log.d("success","Value is:" + value);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("failure", error.getMessage());
            }
        });
    }
}
