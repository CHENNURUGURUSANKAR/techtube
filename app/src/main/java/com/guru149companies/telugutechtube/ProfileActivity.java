package com.guru149companies.telugutechtube;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guru149companies.telugutechtube.Database.db;


public class ProfileActivity extends AppCompatActivity {
    RelativeLayout pick_profile_img;
    ImageView addpost, profile_img;
    Button btn_logout, btn_edit_profile;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser current_user = firebaseAuth.getCurrentUser();
    ArrayList<UploadVideoModel> videoslist = new ArrayList<>();
    String UID, profile_url;
    Uri profile_uri;
    TextView user_name;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProfilePostAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<UploadVideoModel> mypost = new ArrayList<>();
    String username = null;
    String Description = null;
    String web = null;
    long viewcount,watchtime;
    TextView nameText, desTxt, website,post_count,view_count,watch_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        post_count=findViewById(R.id.post_count);
        view_count=findViewById(R.id.view_count);
        watch_count=findViewById(R.id.watch_count);

//firebse int
        UID = current_user.getUid();
        Checkprofilepic();
//recyclerView
        recyclerView = findViewById(R.id.recycler_view_profile);
        recyclerView.setAdapter(adapter);

        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);*/
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//read data from fire raltime data base
        String uid = UID;
        adapter = new ProfilePostAdapter(mypost, this);
        recyclerView.setAdapter(adapter);
        setName();


        db.collection("Videos").whereEqualTo("uid",UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                videoslist.clear();
                List<DocumentSnapshot> videoslist1 = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : videoslist1) {
                    UploadVideoModel uploadVideoModel = snapshot.toObject(UploadVideoModel.class);
                    mypost.add(uploadVideoModel);
                    int postcount=mypost.size();
                    post_count.setText(String.valueOf(postcount));
                }
                adapter.notifyDataSetChanged();
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
      /*  mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                videoslist.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (snapshot.child("uid").getValue(UploadVideoModel.class).equals(UID)) {
                            UploadVideoModel uploadVideoModel = dataSnapshot.getValue(UploadVideoModel.class);
                            videoslist.add(uploadVideoModel);
                        }
                    }
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
//intiview
        user_name = findViewById(R.id.user_name);
        pick_profile_img = findViewById(R.id.profile_img_layout);
        addpost = findViewById(R.id.upload_btn);
        profile_img = findViewById(R.id.profile_img);
        btn_logout = findViewById(R.id.edit_logout);
//view clicks
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        pick_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select profile Image"), 149);
            }
        });
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Post Video"), 1490);
            }
        });
    }

    private void setName() {
        db.collection("USERS").whereEqualTo("UID", UID).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> userdoc = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : userdoc) {
                    username = snapshot.getString("NAME");
                    Description = snapshot.getString("Description");
                    web = snapshot.getString("website");
                    viewcount=(long)snapshot.getLong("ViewsTotal");
                    watchtime=(long)snapshot.getLong("WatchTimeTotal");

                }
                nameText = findViewById(R.id.user_name);
                nameText.setText("Name: "+username);
                desTxt = findViewById(R.id.description);
                desTxt.setText("Description: "+ Description);
                website = findViewById(R.id.website);
                website.setText("Website: "+ web);
                view_count.setText(String.valueOf(viewcount));
                watch_count.setText(String.valueOf(watchtime));

            }
        });
    }

    private void Checkprofilepic() {
        firebaseFirestore.collection("USERS").whereEqualTo("UID", UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : documentSnapshots) {

                    profile_url = snapshot.getString("ProfilePic");

                }
                if (profile_url != "") {

                    profile_uri = Uri.parse(profile_url);
                    Log.d("tag", profile_url);
                    Glide.with(getApplicationContext()).load(profile_uri).into(profile_img);
                } else {
                    profile_img.setImageResource(R.mipmap.ic_launcher);
                    Toast.makeText(getApplicationContext(), "change Profile", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setCancelable(false);
        if (resultCode == RESULT_OK) {
            if (requestCode == 149) {
                Uri Imageuri = data.getData();
                progressDialog.show();
                UID = current_user.getUid();
                StorageReference ProfileUploader = FirebaseStorage.getInstance().getReference("Profiles").child(UID).child(System.currentTimeMillis() + "");
                ProfileUploader.putFile(Imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        ProfileUploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Map<String, Object> Imageuplader = new HashMap<>();
                                Imageuplader.put("ProfilePic", uri.toString());
                                DocumentReference imgdoc = db.collection("USERS").document(UID);
                                imgdoc.update("ProfilePic", uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Glide.with(getApplicationContext()).load(Imageuri).into(profile_img);
                                        Toast.makeText(ProfileActivity.this, "Uploaded profile", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this, "Sorry Unable to Upload try Again", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Sorry Unable to Upload try Again", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float peruploaded = (100 * snapshot.getBytesTransferred()) / (snapshot.getTotalByteCount());
                        progressDialog.setMessage("completed :" + (int) peruploaded + "%");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Sorry Unable to Upload try Again", Toast.LENGTH_SHORT).show();

                    }
                });
                //here we need to upload imag to server right now we will add to profile photo


            } else if (requestCode == 1490) {
                Uri videouri = data.getData();
                Intent intent = new Intent(getApplicationContext(), VideoPreviewActivity.class);
                intent.putExtra(MainActivity.EXTRA_VIDEO_PATH, videouri.toString());
                startActivity(intent);
            }

        }
    }
}