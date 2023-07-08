package com.example.talkin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talkin.CommentActivity;
import com.example.talkin.LikeActivity;
import com.example.talkin.Model.Chat;
import com.example.talkin.Model.Post;
import com.example.talkin.Model.Users;
import com.example.talkin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private Context context;
    private List<Post> posts;
    public List <String> likes;

    public List<List<String>> comments;
    DatabaseReference reference;

    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    String userid, naam="", ppic="";

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.post, parent, false);
        return new PostAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {


        Post post= posts.get(position);
        userid= post.getSender();
        reference= FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user=snapshot.getValue(Users.class);
                naam = user.getUsername();
                ppic = user.getImageURL();
                holder.username.setText(naam);
                holder.uname.setText(naam);
                Glide.with(context).load(post.getPost()).into(holder.post1);
                Glide.with(context).load(ppic).into(holder.profile);
                holder.caption.setText(post.getCaption());
                holder.uname.setText(naam);
                holder.date.setText(post.getDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = holder.getAdapterPosition();
                String id = posts.get(pos).getId();
                Intent intent=new Intent(context, LikeActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);


            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = holder.getAdapterPosition();
                String id = posts.get(pos).getId();
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void like () {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile,post1,like,comment,fav,menu;
        public TextView uname, username, caption, date;

        public ViewHolder(@NonNull View item ){
            super(item);
            profile = item.findViewById(R.id.profile);
            post1 = item.findViewById(R.id.post1);
            like = item.findViewById(R.id.like);
            comment = item.findViewById(R.id.comment);
            uname = item.findViewById(R.id.uname);
            username = item.findViewById(R.id.username);
            caption = item.findViewById(R.id.caption);
            date = item.findViewById(R.id.date);
        }

    }
}
