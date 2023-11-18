package com.example.wordy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPage extends AppCompatActivity {


    Button nextPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        nextPage = findViewById(R.id.NextPageID);
        nextPage.setOnClickListener(newPage);
    }

    private View.OnClickListener newPage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainPage.this, WordyActivity.class);
            startActivity(intent);
//


        }
    };




}