package com.guru149companies.telugutechtube;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guru149companies.telugutechtube.Database.db;

public class VideoPreviewActivity extends AppCompatActivity {
    VideoView video_view;
    ImageView playicon;
    Button btn_upload;
    StorageReference storageReferrence;
    FirebaseAuth mAuth;
    FirebaseUser currentuser;
    String UID;
    EditText tittle, description, tags;
    String sdesc, stag, stittle, username, profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        video_view = findViewById(R.id.videoview);
        playicon = findViewById(R.id.play_vide_icon);
        btn_upload = findViewById(R.id.btn_upload);
        //firebase things
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        UID = currentuser.getUid();

        Intent intent = getIntent();
        Uri videouri = Uri.parse(intent.getStringExtra(MainActivity.EXTRA_VIDEO_PATH));
        storageReferrence = FirebaseStorage.getInstance().getReference();
        playicon.setVisibility(View.VISIBLE);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Videoprocssing(videouri);
            }
        });
        video_view.setVideoURI(videouri);
        RelativeLayout video_view_layout = findViewById(R.id.viedo_view_layout);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video_view);
        video_view.setMediaController(mediaController);
        video_view_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_view.isPlaying()) {
                    video_view.pause();
                    playicon.setVisibility(View.VISIBLE);
                } else {
                    video_view.start();
                    playicon.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public String getExtension() {

        Intent intent = getIntent();
        Uri videouri = Uri.parse(intent.getStringExtra(MainActivity.EXTRA_VIDEO_PATH));
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(videouri));
    }

    public void Videoprocssing(Uri videouri) {
        tittle = findViewById(R.id.edittittle);
        description = findViewById(R.id.editdescription);
        tags = findViewById(R.id.edittags);
        sdesc = description.getText().toString().trim();
        stag = tags.getText().toString().trim();
        stittle = tittle.getText().toString().trim();
        if (stittle.isEmpty()) {
            tittle.setError("Please Give Tittle");
        } else if (sdesc.isEmpty()) {
            description.setError("Please Give Description");
        } else if (stag.isEmpty()) {
            tags.setError("Please enter tags");
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StorageReference upload = storageReferrence.child("Videos/" + System.currentTimeMillis() + "." + getExtension());
            upload.putFile(videouri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map<String,Object> videodetailes=new HashMap<>();
                                    videodetailes.put("comments"," ");
                                    videodetailes.put("description"," ");
                                    videodetailes.put("likes",0);
                                    videodetailes.put("videoUri",uri.toString());
                                    videodetailes.put("tags",stag);
                                    videodetailes.put("tittle",stittle);
                                    videodetailes.put("uid",UID);
                                    int uploadedTime= (int) System.currentTimeMillis();
                                    videodetailes.put("uploadedTime",uploadedTime);
                                    videodetailes.put("view",0);

                                    db.collection("USERS").whereEqualTo("UID",UID).get().addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(VideoPreviewActivity.this, "Fail"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> userdoc=queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot snapshot:userdoc)
                                            {
                                                username = snapshot.getString("NAME");
                                                profilepic = snapshot.getString("ProfilePic");
                                            }
                                            videodetailes.put("uploaderName",username);
                                            videodetailes.put("profilepic",profilepic);
                                            videodetailes.put("videoStatus","UnderReview");
                                            String pushKey =db.collection("Videos").document().getId().toString();
                                            videodetailes.put("pushKey",pushKey);
                                            videodetailes.put("viewertype","viewer");
                                            DocumentReference vidoc=db.collection("Videos").document(pushKey);
                                            vidoc.set(videodetailes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(VideoPreviewActivity.this, "Sucessfully added Video for Review", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(VideoPreviewActivity.this, "Fail"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    });



                                     /*    Log.d("tags", "Success");
                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();


                                    Database.addVideo(stittle, sdesc, stag,UID,uri , new MycompleteListener() {
                                        @Override
                                        public void OnSuccess() {
                                            Toast.makeText(VideoPreviewActivity.this, "On Sucess", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void OnFailure() {
                                            Toast.makeText(VideoPreviewActivity.this, "OnFaiure", Toast.LENGTH_SHORT).show();

                                        }
                                    });*/
                                    /*firebaseFirestore.collection("USERS").whereEqualTo("UID",UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot snapshot : documentSnapshots) {

                                                   username= snapshot.getString("NAME");
                                                   profilepic=snapshot.getString("ProfilePic");

                                            }

                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            DatabaseReference databaseReference = firebaseDatabase.getReference("Videos");
                                            String pushkey=databaseReference.child(currentuser.getUid()).push().getKey().toString();
                                            UploadVideoModel uploadVideoModel = new UploadVideoModel("null",sdesc,"0",profilepic,stag,stittle,currentuser.getUid().toString(),String.valueOf(uploadedtime),username,"UnderReView",uri.toString(),"0",pushkey );

                                            databaseReference.child(pushkey).setValue(uploadVideoModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(VideoPreviewActivity.this, "Uploaded Sucessfully ", Toast.LENGTH_SHORT).show();
                                                    Log.d("Tag", "instertd");
                                                }
                                            });

                                        }
                                    });*/


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(VideoPreviewActivity.this, "not uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float peruploaded = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("completed:" + (int) peruploaded + "%");

                        }
                    });
        }
    }
}