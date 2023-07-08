package com.example.talkin.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.talkin.Model.Users;
import com.example.talkin.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Random;


public class ProfileFragment extends Fragment {



    public ProfileFragment() {
        // Required empty public constructor
    }

    TextView username;
    ImageView imageView;
    DatabaseReference reference;
    FirebaseUser fuser;
    FirebaseStorage storage;

    // profile image

    StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profile,container,false);


        imageView=view.findViewById(R.id.profile_image2);
        username=view.findViewById(R.id.usernamer);


        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user=snapshot.getValue(Users.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);

                }else{

                    Glide.with(getContext()).load(user.getImageURL()).into(imageView);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                imageUri = result;
                imageView.setImageURI(imageUri);
                UploadMyImage();
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openGallery();
            }
        });


        return view;
    }

    private void openGallery() {
        galleryLauncher.launch("image/*");
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            openGallery();
        }
    }


//    private String getFileExtension(Uri uri){
//
//        ContentResolver contentResolver=getContext().getContentResolver();
//        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
//
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//
//    }
    private void UploadMyImage(){

        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (imageUri!=null){

            storage = FirebaseStorage.getInstance();
            StorageReference fileReference=storage.getReference("Image1"+new Random().nextInt(50));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
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
                        reference=FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageURL",mUri);
                        reference.updateChildren(map);
                        progressDialog.dismiss();

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

//
//    private void uploadImageToFirebase(Uri imageUri) {
//
//        ProgressDialog dialog=new ProgressDialog(getContext());
//        dialog.setTitle("Uploading");
//        dialog.show();
//        // Get an instance of Firebase Storage
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//
//        // Create a storage reference
//        StorageReference storageRef = storage.getReference("Image1"+new Random().nextInt(50));
////        StorageReference imagesRef = storageRef.child("images"); // Choose a path for your image
//
//        // Get the file name from the Uri
//        String fileName = "image.jpg"; // Set your desired file name
//
//        // Create a reference to the file you want to upload
////        StorageReference imageRef = storageReference.child(fileName);
//
//        // Upload the file to Firebase Storage
//        UploadTask uploadTask = storageRef.putFile(imageUri);
//
//        // Register observers to listen for when the upload is done or if it fails
//        uploadTask.addOnSuccessListener(taskSnapshot -> {
//            // Handle successful upload
//            // Get the download URL of the uploaded image
//            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                String imageUrl = uri.toString();
//                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
//                // Save the download URL to the Firebase Realtime Database or perform any other actions
//            });
//        }).addOnFailureListener(e -> {
//            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            // Handle unsuccessful upload
//            // ...
//        });
//    }

}