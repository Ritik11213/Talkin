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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView signin =findViewById(R.id.textView113);
        EditText username=findViewById(R.id.editTextTextPersonName);
        EditText email=findViewById(R.id.et122);
        EditText password=findViewById(R.id.et123);
        Button register=findViewById(R.id.button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_text=username.getText().toString();
                String email_text=email.getText().toString();
                String password_text=password.getText().toString();

                if(TextUtils.isEmpty(username_text)||TextUtils.isEmpty(email_text)||TextUtils.isEmpty(password_text))
                {
                    Toast.makeText(RegisterActivity.this,"Please Fill All Details",Toast.LENGTH_SHORT).show();
                }
                else {
                    int num = 0;
                    int alpha1 = 0;
                    int alpha2 = 0;
                    int alpha3 = 0;
                    for (int i = 0; i < password_text.length(); i++) {
                        if (password_text.charAt(i) >= '0' && password_text.charAt(i) <= '9') num++;
                        else if (password_text.charAt(i) >= 'A' && password_text.charAt(i) <= 'Z')
                            alpha1++;
                        else if (password_text.charAt(i) >= 'a' && password_text.charAt(i) <= 'z')
                            alpha2++;
                        else alpha3++;
                    }
                    if (num == 0 || alpha1==0 || alpha2==0 || alpha3==0)
                    {
                        Toast.makeText(RegisterActivity.this, "Constraint Not Fulfilled", Toast.LENGTH_SHORT).show();
                    }
                    else{
                            RegisterNow(username_text, email_text, password_text);
                        }
                }
            }
        });


        auth=FirebaseAuth.getInstance();



    }
    private void RegisterNow(final String username,String email,String password){
                                                                //register
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userid=firebaseUser.getUid();
                            myref= FirebaseDatabase.getInstance()
                                    .getReference("MyUsers")
                                    .child(userid);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("status","offline");

                            myref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        Intent i=new Intent(RegisterActivity.this,ChoiceActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        }else{
                      Toast.makeText(RegisterActivity.this,"Failed! Try Again",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }
}