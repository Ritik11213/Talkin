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
import com.example.talkin.Model.Comments;
import com.example.talkin.Model.Users;
import com.example.talkin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List <Comments> comm;


    DatabaseReference ref;

    public CommentAdapter(Context context, List<Comments> comm) {

        this.context = context;
        this.comm = comm;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

        Comments com = comm.get(position);
        ref= FirebaseDatabase.getInstance().getReference("MyUsers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshott:snapshot.getChildren() )
                {
                    Users user=snapshott.getValue(Users.class);
                    if(user.getId().equals(com.getCommentby()))
                    {
                        String ppic = user.getImageURL();
                        String naam = user.getUsername();
                        holder.postuname.setText(naam);
                        Glide.with(context).load(ppic).into(holder.postpic);
                        holder.comment.setText(com.getComment());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView postpic;
        TextView postuname, comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postuname = itemView.findViewById(R.id.postuname);
            postpic = itemView.findViewById(R.id.postpic);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    @Override
    public int getItemCount() {
        return comm.size();
    }
}
