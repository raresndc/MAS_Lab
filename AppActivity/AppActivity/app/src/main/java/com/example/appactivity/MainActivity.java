package com.example.appactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private String ActivityTag = "ActivityTag";
    @SuppressLint("NewApi")
    public MainActivity(){
        registerActivityLifecycleCallbacks(new MyApp());
    }

    ActivityResultLauncher<Intent> launcherActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(ActivityTag,"onCreate");

        launcherActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            String key = data.getStringExtra("key");
                            Log.d(ActivityTag,"Value received "+key);
                        }
                    }
                });
    }

    public void btnStart(View view){
        Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
        intent.putExtra("key","Message sent from MainActivity");
        launcherActivity.launch(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(ActivityTag,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ActivityTag,"onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ActivityTag,"onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(ActivityTag,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(ActivityTag,"onStop");
    }
}