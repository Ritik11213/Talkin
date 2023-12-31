package com.example.talkin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.*;

import com.example.talkin.Adapters.UserAdapter;
import com.example.talkin.Model.Users;
import com.example.talkin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class UsersFragment extends Fragment {

     private RecyclerView recyclerView;
     private UserAdapter userAdapter;
     private List<Users> mUsers;

  public UsersFragment(){


  }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_users,container,false);
        recyclerView =view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mUsers =new ArrayList<>();
        ReadUsers();
        return view;
    }

    private void ReadUsers(){

    final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("MyUsers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot snapshott:snapshot.getChildren() )
                {
                      Users user=snapshott.getValue(Users.class);
                      assert user!=null;

                    if (!user.getId().equals(firebaseUser.getUid())) {

                        mUsers.add(user);
                    }
                      userAdapter=new UserAdapter(getContext(),mUsers,true);
                      recyclerView.setAdapter(userAdapter);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}