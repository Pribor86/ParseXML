package com.example.parsexml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle b = getIntent().getExtras();
        String name = b.getString("Name");
        String range = b.getString("Range");
        String text = b.getString("Text");

        ((TextView)findViewById(R.id.name)).setText(name);
        ((TextView)findViewById(R.id.range)).setText(range);
        ((TextView)findViewById(R.id.text)).setText(text);
    }
}