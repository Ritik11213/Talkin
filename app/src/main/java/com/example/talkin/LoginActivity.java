package com.example.talkin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
     FirebaseAuth auth;
     FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null)
        {
            Intent i=new Intent(LoginActivity.this,ChoiceActivity.class);  // already logged in
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView logintxt=findViewById(R.id.textView11);
        EditText emailtxt=findViewById(R.id.et11);
        EditText passtxt=findViewById(R.id.et22);
        Button login_btn=findViewById(R.id.button11);
        Button sgnup=findViewById(R.id.button2);
        sgnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        auth=FirebaseAuth.getInstance();



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_text=emailtxt.getText().toString();
                String pass_text=passtxt.getText().toString();

                if (TextUtils.isEmpty(email_text)||TextUtils.isEmpty(pass_text))
                {
                    Toast.makeText(LoginActivity.this,"Enter All Details",Toast.LENGTH_SHORT).show();
                }else{

                    auth.signInWithEmailAndPassword(email_text,pass_text)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                  if(task.isSuccessful()){


                                      Intent i=new Intent(LoginActivity.this,ChoiceActivity.class);
                                      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                      startActivity(i);
                                      finish();            //sign in
                                  }else{

                                      Toast.makeText(LoginActivity.this,"INVALID DETAILS",Toast.LENGTH_SHORT).show();
                                  }
                                }
                            });

                }
            }
        });
    }
}