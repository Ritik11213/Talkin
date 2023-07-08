package com.example.talkin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ImageView banda=findViewById(R.id.imageView3);
        ImageView bandi=findViewById(R.id.imageView4);

        TextView line=findViewById(R.id.textView);

        Animation animbanda;
        Animation animbandi;
        Animation animtext;

        Animation animbtn;
        Button tt;
        tt=findViewById(R.id.buttontp);

        animbanda= AnimationUtils.loadAnimation(this,R.anim.man);
        banda.setAnimation(animbanda);

        animbandi= AnimationUtils.loadAnimation(this,R.anim.girl);
        bandi.setAnimation(animbandi);

        animtext= AnimationUtils.loadAnimation(this,R.anim.txt);
        line.setAnimation(animtext);



        animbtn= AnimationUtils.loadAnimation(this,R.anim.bbtn);
        tt.setAnimation(animbtn);



       tt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(MainActivity.this,LoginActivity.class);
               startActivity(i);
           }
       });

    }
}