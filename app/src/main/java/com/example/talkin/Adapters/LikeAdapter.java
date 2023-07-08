package com.example.talkin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talkin.Model.Likes;
import com.example.talkin.Model.Post;
import com.example.talkin.Model.Users;
import com.example.talkin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {
    private Context context;
    private List<Likes> likes;

    DatabaseReference ref;

    public LikeAdapter(Context context, List<Likes> likes) {

        this.context = context;
        this.likes = likes;
    }

    @NonNull
    @Override
    public LikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.like_item, parent, false);
        return new LikeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeAdapter.ViewHolder holder, int position) {

        Likes like = likes.get(position);
        ref= FirebaseDatabase.getInstance().getReference("MyUsers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshott:snapshot.getChildren() )
                {
                    Users user=snapshott.getValue(Users.class);
                    if(user.getId().equals(like.getLikeby()))
                    {
                        String ppic = user.getImageURL();
                        String naam = user.getUsername();
                        holder.username.setText(naam);
                        Glide.with(context).load(ppic).into(holder.profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        holder.username.setText(like.getLikeby());

    }

    @Override
    public int getItemCount() {
        return likes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile;
        TextView username;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile = itemView.findViewById(R.id.profile);
        }
    }



}
