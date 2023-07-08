package com.example.talkin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        TextView test1,test2;

        test1=findViewById(R.id.t1);
        test2=findViewById(R.id.t2);

        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChoiceActivity.this,MainActivity2.class));
            }
        });

        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChoiceActivity.this,TestNavActivity.class));
            }
        });



    }
}