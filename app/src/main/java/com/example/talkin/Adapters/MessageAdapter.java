package com.example.talkin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talkin.MessageActivity;
import com.example.talkin.Model.Chat;
import com.example.talkin.Model.Users;
import com.example.talkin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private List<Chat> mChats;
    private String imgURL;

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    FirebaseUser fuser;


    public MessageAdapter(Context context, List<Chat> mChats,String imgURL) {
        this.context = context;
        this.mChats=mChats;
        this.imgURL=imgURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=mChats.get(position);
        holder.show_message.setText(chat.getMessage());
        if (imgURL.equals("default")){

            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        }else{
            Glide.with(context).load(imgURL).into(holder.profile_image);



        }

        if (position==mChats.size()-1)
        {
              if (chat.isIsseen()){

                  holder.txt_seen.setText("Seen");
              }else{

                  holder.txt_seen.setText("Delivered");
              }


        }else{

            holder.txt_seen.setVisibility(View.GONE);
        }





    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            txt_seen=itemView.findViewById(R.id.txt_seen);
        }
    }


    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}

