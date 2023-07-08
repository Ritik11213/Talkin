package com.example.talkin.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talkin.Adapters.PostAdapter;
import com.example.talkin.Model.Post;
import com.example.talkin.Model.Users;
import com.example.talkin.R;
import com.example.talkin.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> posts;

    ImageView profile;
    TextView username;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser fuser = firebaseAuth.getCurrentUser();
    String userId = fuser.getUid();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profile= root.findViewById(R.id.profile);
        username= root.findViewById(R.id.username);

        recyclerView =root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("MyUsers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshott:snapshot.getChildren() )
                {
                    Users user=snapshott.getValue(Users.class);
                    assert user!=null;
                    if(user.getId().equals(userId)) {
                        String ppic = user.getImageURL();
                        String naam = user.getUsername();
                        username.setText(naam);
                        Glide.with(getContext()).load(ppic).into(profile);
                        posts =new ArrayList<>();
                        ReadPosts();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    private void ReadPosts(){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Post");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                for(DataSnapshot snapshott:snapshot.getChildren() )
                {
                    Post post=snapshott.getValue(Post.class);
                    assert posts!=null;
                    if(post.getSender().equals(userId)) {
                        posts.add(post);
                    }
                    postAdapter = new PostAdapter(getContext(), posts);
                    recyclerView.setAdapter(postAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}