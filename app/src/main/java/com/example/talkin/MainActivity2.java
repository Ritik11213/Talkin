package com.example.talkin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.talkin.Fragments.ChatsFragment;
import com.example.talkin.Fragments.UsersFragment;
import com.example.talkin.Model.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
       ViewPagerFragmentAdapter viewPagerFragmentAdapter;
       TabLayout tabLayout;                                   //fragment
       ViewPager2 viewPager2;
    FirebaseUser firebaseUser;  //logout
    DatabaseReference myref;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

         String[] titles=new String[]{"Chats","Users","Profile"};

            viewPager2=findViewById(R.id.view_pager);       // fragment
            tabLayout=findViewById(R.id.tab_layout);
            viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(this);
            viewPager2.setAdapter(viewPagerFragmentAdapter);

            new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(titles[position]))).attach();





        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        myref= FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());





        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                                                                          //log out
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



        });











    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu); //logout
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity2.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                return true;
        }                            //logout
        return false;
    }

    private void CheckStatus(String status)
    {
        myref=FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        myref.updateChildren(hashMap);



    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CheckStatus("offline");
    }





}