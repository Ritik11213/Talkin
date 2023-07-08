package com.example.talkin.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.talkin.R;
import com.example.talkin.TestNavActivity;
import com.example.talkin.databinding.FragmentDashboardBinding;
import com.example.talkin.ui.notifications.NotificationsFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ImageView npost;
    private EditText caption;
    private Button post, post1;
    private DatabaseReference reference;
    private FirebaseAuth mauth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri imageuri;
    private String myuri;
    private StorageTask uploadTask;
    private final int PICK_IMAGE_REQUEST = 22;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private StorageReference storageRef;

    FirebaseUser fuser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mauth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Post");
        storageRef = FirebaseStorage.getInstance().getReference().child("Post");
        npost = root.findViewById(R.id.npost);
        caption = root.findViewById(R.id.caption);
        post = root.findViewById(R.id.post);
        post1 = root.findViewById(R.id.post1);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });

        npost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                imageuri = result;
                npost.setImageURI(imageuri);
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openGallery();
            }
        });

        return root;
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        galleryLauncher.launch("image/*");
    }

    private void UploadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (imageuri!=null){

            storage = FirebaseStorage.getInstance();
            StorageReference fileReference=storage.getReference("Image1"+new Random().nextInt(500));
            uploadTask=fileReference.putFile(imageuri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {


                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful())
                    {
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();
                        reference=FirebaseDatabase.getInstance().getReference("Post");
                        fuser= FirebaseAuth.getInstance().getCurrentUser();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date currentDate = new Date();
                        String currentDateTime = sdf.format(currentDate);

                        HashMap<String,Object> map=new HashMap<>();
                        List <String> likes = new ArrayList<>();
                        List <List<String>> comments = new ArrayList<>();
                        String id ="Post"+ new Random().nextInt(5000);

                        map.put("id",id);
                        map.put("sender",fuser.getUid());
                        map.put("post",mUri);
                        map.put("caption",caption.getText().toString());
                        map.put("date",currentDateTime);
                        map.put("likes",likes);
                        map.put("comments",comments);

                        reference.child(id).setValue(map);
                        progressDialog.dismiss();
                        startActivity(new Intent(getContext(), TestNavActivity.class));

                    }else {


                        Toast.makeText(getContext(),"Failed!!",Toast.LENGTH_SHORT).show();


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }else {

            Toast.makeText(getContext(),"No Image Selected",Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}