package com.guru149companies.telugutechtube;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.guru149companies.telugutechtube.Database.db;

//import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BottomSheetDialog bottomSheetDialog;
    Dialog progressDialog;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentuser;
    public static final String EXTRA_VIDEO_PATH = "149";
    ImageView search, cancel;
    TextInputEditText searchtext;
    public static RecyclerView recyc_main;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Videos");
    MainRecAdadpter adapter;
    String UID;
    ArrayList<UploadVideoModel> videoslist = new ArrayList<>();
    ArrayList<UploadVideoModel> filterlist = new ArrayList<>();
    private int videoid = 0;

    private void enablePersistence() {
        // [START rtdb_enable_persistence]
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // [END rtdb_enable_persistence]
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //permissions

//recview
        search = findViewById(R.id.ic_search);
        searchtext = findViewById(R.id.text_search);
        searchtext.setVisibility(View.INVISIBLE);
        cancel = findViewById(R.id.ic_cancel);
        cancel.setVisibility(View.INVISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchtext.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchtext.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
            }
        });

        recyc_main = findViewById(R.id.mainrecyckerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyc_main.setLayoutManager(linearLayoutManager);
// progressbar
        progressDialog = new Dialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
//Action bar checking
        ProgressBar progressBar = progressDialog.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);
        adapter = new MainRecAdadpter(this, videoslist);
        recyc_main.setAdapter(adapter);

        db.collection("Videos").orderBy("uploadedTime", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                videoslist.clear();
                List<DocumentSnapshot> videoslist1 = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : videoslist1) {
                    UploadVideoModel uploadVideoModel = snapshot.toObject(UploadVideoModel.class);
                    videoslist.add(uploadVideoModel);
                }
                adapter.notifyDataSetChanged();
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();

            }
        });


        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < videoslist.size(); i++) {
                            if (videoslist.get(i).getTittle().contains(s.toString())) {
                                Log.d("vidlist", videoslist.get(i).getTittle());
                                filterlist.add(videoslist.get(i));
                                Log.d("flist",filterlist.toString());
                                adapter = new MainRecAdadpter(getApplicationContext(), filterlist);

                            } else {
                                Log.d("List", s.toString());
                            }
                        }
                    }
                }, 10);


                adapter.notifyDataSetChanged();

            }
        });
       /* mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                videoslist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UploadVideoModel uploadVideoModel = dataSnapshot.getValue(UploadVideoModel.class);
                    videoslist.add(uploadVideoModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        Window window = this.getWindow();
        View view = findViewById(R.id.mylayout);
        // view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        //bottom sheet
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.option_upload);
        Window window1 = bottomSheetDialog.getWindow();
        window1.setNavigationBarColor(bottomSheetDialog.getContext().getResources().getColor(R.color.colorAccent));
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));

        LinearLayout UploaFRomDevice = bottomSheetDialog.findViewById(R.id.option_upload_form_device);

        UploaFRomDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                currentuser = firebaseAuth.getCurrentUser();
                if (currentuser == null) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Video"), 149);
                }

            }
        });
        adapter.setStateRestorationPolicy(adapter.getStateRestorationPolicy());
        bottomNavigationView = findViewById(R.id.mainbottomnav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.upload:
                        bottomSheetDialog.show();
                        break;
                    case R.id.profile:
                        Checkuser();
//                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });


    }


    private void Checkuser() {

        currentuser = firebaseAuth.getCurrentUser();
        if (currentuser == null) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);

        }
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 149) {
                Uri videouri = data.getData();

                Window window = getWindow();

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));

                Intent intent = new Intent(getApplicationContext(), VideoPreviewActivity.class);
                intent.putExtra(MainActivity.EXTRA_VIDEO_PATH, videouri.toString());

                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoslist = videoslist;

    }

    @Override
    public void onBackPressed() {
        searchtext.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }

    private void setSnaphelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyc_main);
        recyc_main.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                adapter.notifyDataSetChanged();
                View view = snapHelper.findSnapView(recyc_main.getLayoutManager());
                videoid = recyc_main.getLayoutManager().getPosition(view);


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

}
