package com.example.talkin;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.talkin.Adapters.CommentAdapter;
import com.example.talkin.Model.Comments;
import com.example.talkin.Model.Post;
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
import java.util.Random;

public class CommentActivity extends AppCompatActivity {

    ImageView postpic, upic, sent;
    EditText cmnts;
    String ans;
    private RecyclerView recyclerView;

    private CommentAdapter commentAdapter;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser fuser = firebaseAuth.getCurrentUser();

    private List<Comments> comm;
    TextView postuname, postcap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent=getIntent();
        String postid=intent.getStringExtra("id");

        postpic = findViewById(R.id.ppostpic);
        postuname = findViewById(R.id.ppostuname);
        postcap = findViewById(R.id.ppostcap);

        upic = findViewById(R.id.upic);
        cmnts = findViewById(R.id.cmnts);
        sent  = findViewById(R.id.sent);

        String userid = fuser.getUid();

        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);



        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshott:snapshot.getChildren() )
                {
                    Post post=snapshott.getValue(Post.class);
                    if(post.getId().equals(postid)){

                        String ppic = post.getPost();
                        String sendid= post.getSender();
                        String cap = post.getCaption();

                        DatabaseReference reference2= FirebaseDatabase.getInstance().getReference("MyUsers");
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot snapshott:snapshot.getChildren() )
                                {
                                    Users users=snapshott.getValue(Users.class);
                                    if(users.getId().equals(sendid)){

                                        String ans = users.getUsername();
                                        Glide.with(CommentActivity.this).load(ppic).into(postpic);
                                        postuname.setText(ans);
                                        postcap.setText(cap);

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("MyUsers");
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot snapshott:snapshot.getChildren() )
                        {
                            Users users=snapshott.getValue(Users.class);
                            if(users.getId().equals(userid)){

                                String ppic = users.getImageURL();
                                Glide.with(CommentActivity.this).load(ppic).into(upic);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                comm = new ArrayList<>();
                ReadComments(postid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommentActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = cmnts.getText().toString();
                WriteComment(userid, postid, msg);
            }
        });
    }
    public void ReadComments(String postid){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Comments");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comm.clear();
                for(DataSnapshot snapshott:snapshot.getChildren() )
                {
                    Comments com=snapshott.getValue(Comments.class);
                    assert comm!=null;
                    if(com.getPostid().equals(postid)) comm.add(com);
                    commentAdapter=new CommentAdapter(CommentActivity.this,comm);
                    recyclerView.setAdapter(commentAdapter);
                    Log.d(TAG, "onDataChange: "+ comm.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(CommentActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void WriteComment(String userid, String postid, String msg){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Comments");
        String id ="Comments"+ new Random().nextInt(5000);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("id",id);
        hashMap.put("commentby", userid);
        hashMap.put("postid", postid);
        hashMap.put("comment", msg);
        reference.child(id).setValue(hashMap);
    }
}