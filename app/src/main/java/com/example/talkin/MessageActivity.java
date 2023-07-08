package com.example.talkin;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.talkin.Adapters.MessageAdapter;
import com.example.talkin.Model.Chat;
import com.example.talkin.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MessageActivity extends AppCompatActivity {

      TextView username;
      ImageView imageView;
      FirebaseUser fuser;
      Intent intent;
      DatabaseReference reference;
      RecyclerView recyclerView;
      EditText msg_editText;
      ImageView sendbtn;

      MessageAdapter messageAdapter;
      List<Chat>mchat;
      RecyclerView recyclerVieww;
      String userid;
      ValueEventListener seenListener;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_message);

        imageView=findViewById(R.id.imageview_profile);
        username=findViewById(R.id.username);

        sendbtn=findViewById(R.id.btn_send);
        msg_editText=findViewById(R.id.text_send);


        recyclerVieww=findViewById(R.id.recyclerview1);
        recyclerVieww.setHasFixedSize(true);
              LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
              linearLayoutManager.setStackFromEnd(true);
              recyclerVieww.setLayoutManager(linearLayoutManager);


        /*Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
 */

          intent=getIntent();
           userid=intent.getStringExtra("userid");
          fuser= FirebaseAuth.getInstance().getCurrentUser();
          reference= FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);
          reference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  Users user=snapshot.getValue(Users.class);
                  username.setText(user.getUsername());
                  if (user.getImageURL().equals("default")){
                      imageView.setImageResource(R.mipmap.ic_launcher);
                  }else{

                      Glide.with(MessageActivity.this)
                              .load(user.getImageURL())
                              .into(imageView);

                  }
                  readMessages(fuser.getUid(),userid,user.getImageURL());

              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });

          sendbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  String msg=msg_editText.getText().toString();
                  if (!msg.equals("")){

                      sendMessage(fuser.getUid(),userid,msg);
                  }else{
                      Toast.makeText(MessageActivity.this,"Empty Message!!",Toast.LENGTH_SHORT).show();
                  }
                  msg_editText.setText("");
              }
          });
          SeenMessage(userid);

    }

    private void setSupportActionBar(Toolbar toolbar) {


    }

     private void SeenMessage(String userid)
     {
           reference=FirebaseDatabase.getInstance().getReference("Chats");
           seenListener=reference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   for (DataSnapshot snapshot:dataSnapshot.getChildren())
                   {

                       Chat chat=snapshot.getValue(Chat.class);
                       if (chat.getReceiver().equals(fuser.getUid())&&chat.getSender().equals(userid))
                       {
                              HashMap<String,Object> hashMap=new HashMap<>();
                              hashMap.put("isseen",true);
                               snapshot.getRef().updateChildren(hashMap);
                       }


                   }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });




     }




    private void sendMessage(String sender,String receiver,String Message)
    {
           DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",Message);
        hashMap.put("isseen",false);
        reference.child("Chats").push().setValue(hashMap);


        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fuser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){

               chatRef.child("id").setValue(userid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

private void readMessages(String myid,String userid,String imgurl)
{

  mchat=new ArrayList<>();
  reference=FirebaseDatabase.getInstance().getReference("Chats");
  reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
          mchat.clear();
          for (DataSnapshot ss:snapshot.getChildren()){

             Chat chat=ss.getValue(Chat.class);
             if ((chat.getReceiver().equals(myid) && chat.getSender().equals(userid)) || (chat.getReceiver().equals(userid)&&chat.getSender().equals(myid)))
             {

             mchat.add(chat);


             }

             messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imgurl);
             recyclerVieww.setAdapter(messageAdapter);









          }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
  });






}
    private void CheckStatus(String status)
    {   fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);



    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        CheckStatus("offline");
    }





}