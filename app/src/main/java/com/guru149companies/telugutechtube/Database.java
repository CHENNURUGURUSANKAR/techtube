package com.guru149companies.telugutechtube;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static int likes = 0, views = 0, shares = 0, downloads = 0;

    public static ArrayList<File> allVideoList = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static File directory;
    public static String[] allpath;
    public static List<RecyclerVideoModel> recyclerVideoModelList = new ArrayList<>();
public static String username,profilepic;

    public static void storeUserData(String email, String name, String UID,String PhoneNumber ,final MycompleteListener mycompleteListener) {
        Map<String, Object> user = new HashMap<>();
        user.put(" EMAIL_ID", email);
        user.put("NAME", name);
        user.put("UID", UID);
        user.put("ProfilePic", "null");
        user.put("PhoneNumber", PhoneNumber);
        user.put("ViewsTotal", 0);
        user.put("PostsTotal", 0);
        user.put("WatchTimeTotal", 0);
        user.put("website", "website");
        user.put("Description", "null");

        DocumentReference userdoc = db.collection("USERS").document(UID);
        userdoc.set(user).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mycompleteListener.OnFailure();

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mycompleteListener.OnSuccess();

            }
        });

    }


  }

    /*public static void storeVideo(String title, String description, String videouri, final MycompleteListener mycompleteListener) {
        Map<String, Object> videos = new HashMap<>();
        videos.put("title", title);
        videos.put("description", description);
        videos.put("videoUri", videouri);
        videos.put("likes", likes);
        videos.put("shares", shares);
        videos.put("views", views);
        videos.put("downloads", downloads);

        String docId = com.gu  mAuth = FirebaseAuth.getInstance();
                                    FirebaseUser currentuser = mAuth.getCurrentUser();
                                    int uploadedtime = (int) System.currentTimeMillis();
                                    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                                    Map<String ,Object> videosdoc=new HashMap<>();
                                    videosdoc.put("comments","");
                                    videosdoc.put("description",sdesc);
                                    videosdoc.put("likes","0");
                                    videosdoc.put("profilePic",profilepic);
                                    videosdoc.put("tags",stag);
                                    videosdoc.put("tittle",stittle);
                                    videosdoc.put("uid",UID);
                                    videosdoc.put("videoUri",uri.toString());
                                    videosdoc.put("uploadedTime",uploadedtime);
                                    firebaseFirestore.collection("USERS").whereEqualTo("UID",UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot snapshot : documentSnapshots) {

                                                username= snapshot.getString("NAME");
                                                profilepic=snapshot.getString("ProfilePic");

                                            }
                                            videosdoc.put("uploaderName",username);
                                            videosdoc.put("videoStatus","UnderReview");
                                            videosdoc.put("view","0");.cuckoo.Database.db.collection("Videos").document().getId();
        videos.put("videoId", docId);
        Task<Void> videoref = db.collection("Videos").document(docId).set(videos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mycompleteListener.OnSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mycompleteListener.OnFailure();
            }
        });
    }
*/

    /*public static void loadAllVideos(Context context, final MycompleteListener mycompleteListener) {
        allpath = StorageUtil.getStorageDirectories(context);
        for (String path : allpath) {
            directory = new File(path);
            Method.loadDirectory(directory);
        }
        mycompleteListener.OnSuccess();
    }*/

/*
    public static void loadDatabaseVideo(final MycompleteListener mycompleteListener) {
        recyclerVideoModelList.clear();
        db.collection("Videos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                String title, description, videoId, videoUri;
                int likes, shares, downloads, coments;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    title = doc.getString("title");
                    description = doc.getString("description");
                    videoId = doc.getString("videoId");
                    videoUri = doc.getString("videoUri");
                    likes=doc.getLong("likes").intValue();
                    downloads=doc.getLong("downloads").intValue();
                    shares=doc.getLong("shares").intValue();
                    coments=doc.getLong("views").intValue();
                    recyclerVideoModelList.add(new RecyclerVideoModel(videoId,videoUri,title,description,likes,shares,downloads,coments));
                }

                mycompleteListener.OnSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mycompleteListener.OnFailure();
            }
        });
    }*/
