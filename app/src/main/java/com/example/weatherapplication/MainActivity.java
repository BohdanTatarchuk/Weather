package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button_search);
        editText = findViewById(R.id.type_your_city);
    }

    public void onClick(View view) {
        Context context = MainActivity.this;
        String city = editText.getText().toString();
        if(city.trim().equals("")){
            Toast.makeText(context, R.string.toast, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, NotMainActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, city);
            startActivity(intent);
        }
    }
}