package com.example.talkin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.talkin.Fragments.ChatsFragment;
import com.example.talkin.Fragments.ProfileFragment;
import com.example.talkin.Fragments.UsersFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private String[] titles=new String[]{"Chats","Users","Profile"};
    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new ChatsFragment();
                case 1:
                    return new UsersFragment();
            case 2:
                return new ProfileFragment();
        }
        return new ChatsFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
