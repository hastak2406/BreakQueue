package com.example.parik.demomm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                  //  Toast.makeText(getApplicationContext(), "Welcome !!!", Toast.LENGTH_LONG).show();
                        Intent ie = new Intent(MainActivity.this, LoginActivity.class);
                        ie.addCategory(Intent.CATEGORY_HOME);
                        ie.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(ie);
                        finish();
            }
        }, 2500);
    }
}
