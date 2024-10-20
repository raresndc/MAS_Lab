package ro.ase.ie.appactivity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        registerActivityLifecycleCallbacks(new MyApp());
    }
    private String ActivityTag = "ActivityTag";

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
        Log.d(ActivityTag, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(ActivityTag, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(ActivityTag, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(ActivityTag, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ActivityTag, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ActivityTag, "onDestroy");
    }
}