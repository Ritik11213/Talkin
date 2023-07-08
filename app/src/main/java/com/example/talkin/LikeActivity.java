package com.example.talkin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.talkin.Adapters.LikeAdapter;
import com.example.talkin.Adapters.PostAdapter;
import com.example.talkin.Model.Likes;
import com.example.talkin.Model.Post;
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

public class LikeActivity extends AppCompatActivity {

    FirebaseDatabase ref;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser fuser = firebaseAuth.getCurrentUser();

    private LikeAdapter likeAdapter;

    List<Likes> likess;

    private RecyclerView recyclerView;

    ImageView likes;
    boolean c=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);;
        String userid =fuser.getUid();

        Intent intent=getIntent();
        String postid=intent.getStringExtra("id");

        likes = findViewById(R.id.like);
        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Likes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshott:snapshot.getChildren() )
                {
                    Likes like=snapshott.getValue(Likes.class);
                    if(like.getPostid().equals(postid) && like.getLikeby().equals(userid)){
                        likes.setImageResource(R.drawable.flike);
                        c=false;
                        break;
                    }
                }
                likess = new ArrayList<>();
                ReadLikes(postid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LikeActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c) AddLike(userid, postid);
                else
                {
                    Toast.makeText(LikeActivity.this, "Already Likes", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void AddLike(String userid, String postid){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Likes");
        String id ="Likes"+ new Random().nextInt(5000);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("id",id);
        hashMap.put("likeby", userid);
        hashMap.put("postid", postid);
        reference.child(id).setValue(hashMap);
    }
    public void ReadLikes(String postid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Likes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likess.clear();
                for(DataSnapshot snapshott:snapshot.getChildren() )
                {
                    Likes like=snapshott.getValue(Likes.class);
                    assert likess!=null;
                    if(like.getPostid().equals(postid)) likess.add(like);
                    likeAdapter=new LikeAdapter(LikeActivity.this,likess);
                    recyclerView.setAdapter(likeAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(LikeActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}