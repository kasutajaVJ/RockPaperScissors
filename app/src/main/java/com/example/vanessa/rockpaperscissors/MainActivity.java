package com.example.vanessa.rockpaperscissors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText userName;
    SharedPreferences.Editor editor;

    private final int SHOW_RPS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.user_name);
    }

    public void saveDataNextPage(View view) {
        if (TextUtils.isEmpty(userName.getText())) {
            userName.setError("Username is required!");
        } else {
            SharedPreferences userData = getSharedPreferences(SharedPreference.TAG.name(), Context.MODE_PRIVATE);
            editor = userData.edit();
            editor.putString(SharedPreference.TAG_NAME.name(), userName.getText().toString());
            editor.apply();
            userName.setText("");

            Toast.makeText(this, "Username saved", Toast.LENGTH_LONG).show();

            goToGamePageWithDelay();
        }
    }

    public void goToGamePageWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivity = new Intent(MainActivity.this, GamePage.class);
                MainActivity.this.startActivity(mainActivity);
                MainActivity.this.finish();
            }
        }, SHOW_RPS);
    }
}
