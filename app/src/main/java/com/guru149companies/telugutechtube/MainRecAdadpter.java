package com.guru149companies.telugutechtube;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.guru149companies.telugutechtube.Database.db;
import static com.guru149companies.telugutechtube.MainActivity.recyc_main;

public class MainRecAdadpter extends RecyclerView.Adapter<MainRecAdadpter.ViewHolder> implements MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = mAuth.getCurrentUser();
    int currentvideoposition = 0;
    Context context;
    public static UploadVideoModel uploadVideoModel = new UploadVideoModel();

    ArrayList<UploadVideoModel> videoModels;

    public MainRecAdadpter(Context context, ArrayList<UploadVideoModel> videoModels) {
        this.context = context;
        this.videoModels = videoModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainrecyclerview, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        uploadVideoModel = videoModels.get(position);


        holder.videoView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onViewAttachedToWindow(View v) {


                Uri uri = Uri.parse(uploadVideoModel.getVideoUri());
                holder.videoView.setVideoURI(uri);
                holder.videoView.seekTo(1);

                holder.play_icon.setVisibility(View.INVISIBLE);
                Log.d("uri",String.valueOf(uri));
                if (holder.getAbsoluteAdapterPosition()==0)
                {
                    holder.videoView.start();
                }
                else {

                }
                Log.d("TAG att", String.valueOf(holder.getAbsoluteAdapterPosition()));
                Log.d("TAG att", String.valueOf(holder.getBindingAdapterPosition()));
                Log.d("TAG att", String.valueOf(holder.getOldPosition()));
                Log.d("TAG att", String.valueOf(holder.getItemId()));
                Log.d("TAG att", String.valueOf(holder.videoView.getVisibility()));
                Log.d("TAG att", String.valueOf(holder.videoView.getSystemUiVisibility()));
                Log.d("TAG attl0", String.valueOf(holder.videoView.getWindowSystemUiVisibility()));
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                currentvideoposition = holder.videoView.getCurrentPosition();
                holder.videoView.pause();
                holder.play_icon.setVisibility(View.VISIBLE);

            }
        });
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                recyc_main.smoothScrollToPosition(position + 1);
            }
        });
        holder.vidrecy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                if (holder.videoView.isPlaying()) {
                    holder.videoView.pause();
                    holder.play_icon.setVisibility(View.VISIBLE);
                } else {
                    holder.videoView.start();
                    holder.play_icon.setVisibility(View.INVISIBLE);
                }

            }
        });
        holder.download_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request downloadrequest = new DownloadManager.Request(Uri.parse(uploadVideoModel.getVideoUri()))
                        .setTitle("Telugu Tech Tube").setDescription("Doenloading Video...")
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + System.currentTimeMillis())
                        .setShowRunningNotification(true)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);


                downloadManager.enqueue(downloadrequest);
            }
        });
        holder.username.setText(uploadVideoModel.getUploaderName());
        holder.like_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Liked Video", Toast.LENGTH_SHORT).show();
            }
        });
        holder.under_review_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentuser != null) {
                    String UID = currentuser.getUid();
                    if (!UID.equals("hqGRfi6IuJc8QPzVKOoTgDFkwt02")) {
                        Toast.makeText(context, "You are not a Admin", Toast.LENGTH_SHORT).show();
                    } else {
                        PopupMenu popup = new PopupMenu(context, holder.under_review_ll);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.popup_under_review, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                DocumentReference updatevideostatus = db.collection("Videos").document(uploadVideoModel.getPushKey());

                                updatevideostatus.update("videoStatus", item.getTitle().toString());

                                Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });
                        //showing popup menu
                        popup.show();
                        Toast.makeText(context, " You are a Admin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        CheckVideoStatus(holder, uploadVideoModel);
        holder.titlle.setText(uploadVideoModel.getTittle());
        CheckProfileic(holder, uploadVideoModel);
        CheckVideoStatus(holder, uploadVideoModel);
        HideViews(holder, uploadVideoModel);
    }


    private void HideViews(ViewHolder holder, UploadVideoModel uploadVideoModel) {


        if (currentuser == null) {
            holder.main_rec_ll.removeView(holder.download_ll);
            holder.main_rec_ll.removeView(holder.add_manager_ll);
            holder.main_rec_ll.setGravity(Gravity.CENTER_HORIZONTAL);
           /* holder.download_ll.setVisibility(View.INVISIBLE);
            holder.add_manager_ll.setVisibility(View.INVISIBLE);*/

        } else {
            if (currentuser != null) {
                String UID = currentuser.getUid();
                if (!UID.equals("hqGRfi6IuJc8QPzVKOoTgDFkwt02")) {

                    holder.main_rec_ll.removeView(holder.download_ll);
                    holder.main_rec_ll.removeView(holder.add_manager_ll);
                    holder.main_rec_ll.setGravity(Gravity.CENTER_HORIZONTAL);
              /*  holder.download_ll.removeAllViews();
                holder.add_manager_ll.removeAllViews();*/

                /*
                holder.download_ll.setVisibility(View.INVISIBLE);
                holder.add_manager_ll.setVisibility(View.INVISIBLE)*/
                    ;
                } else {

                    Toast.makeText(context, "Your Admin", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void CheckProfileic(ViewHolder holder, UploadVideoModel uploadVideoModel) {
        if (uploadVideoModel.getProfilepic().equals("null")) {
        } else {
            Glide.with(context).load(Uri.parse(uploadVideoModel.getProfilepic().toString())).into(holder.profilepi_pic);
        }
    }

    private void CheckVideoStatus(ViewHolder holder, UploadVideoModel uploadVideoModel) {

        String videostatus = uploadVideoModel.getVideoStatus().toString();
        if (videostatus.equals("Approved")) {
            Log.d("tag1 app", uploadVideoModel.getVideoStatus());
            holder.ic_review.setImageResource(R.drawable.ic_approved);
            holder.review_txt.setText(videostatus);
        } else if (videostatus.equals("Recjected")) {
            Log.d("tag1 rej", uploadVideoModel.getVideoStatus());
            holder.ic_review.setImageResource(R.drawable.ic_declined);
            holder.review_txt.setText(videostatus);
        } else {
            Log.d("tag1 els", uploadVideoModel.getVideoStatus());
            holder.ic_review.setImageResource(R.drawable.ic_under_review);
            holder.review_txt.setText(videostatus);
        }
    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("TAg Buf", String.valueOf(percent));

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        recyc_main.smoothScrollToPosition(getItemCount() - 1);


    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.d("TAg CHW", String.valueOf(width));
        Log.d("TAg CHH", String.valueOf(height));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        VideoView videoView;
        RelativeLayout vidrecy;
        CardView play_icon;
        TextView titlle, views, likes, username, manager_txt, download_txt, review_txt;
        ImageView profilepi_pic, ic_like, ic_view, ic_review, ic_download, ic_addmanager;
        LinearLayout like_ll, view_ll, under_review_ll, download_ll, add_manager_ll, main_rec_ll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vidrecy = itemView.findViewById(R.id.vidrecy);
            videoView = itemView.findViewById(R.id.recyclervideoview);
            play_icon = itemView.findViewById(R.id.recycler_view_video_icon);
            profilepi_pic = itemView.findViewById(R.id.profile_rec);
            titlle = itemView.findViewById(R.id.tittle_rec);
            views = itemView.findViewById(R.id.views_rec_txt);
            likes = itemView.findViewById(R.id.likes_rec_txt);
            username = itemView.findViewById(R.id.user_name_rec);
            ic_like = itemView.findViewById(R.id.ic_like);
            ic_view = itemView.findViewById(R.id.ic_view_rec);
            ic_review = itemView.findViewById(R.id.ic_review);
            download_txt = itemView.findViewById(R.id.txt_download_txt);
            manager_txt = itemView.findViewById(R.id.txt_add_manager_txt);
            ic_download = itemView.findViewById(R.id.ic_doewnload_rec);
            ic_addmanager = itemView.findViewById(R.id.ic_add_manager_rec);
            like_ll = itemView.findViewById(R.id.like_ll);
            view_ll = itemView.findViewById(R.id.view_ll);
            under_review_ll = itemView.findViewById(R.id.review_ll);
            download_ll = itemView.findViewById(R.id.download_ll);
            add_manager_ll = itemView.findViewById(R.id.add_manager_ll);
            review_txt = itemView.findViewById(R.id.rec_txt_review);
            main_rec_ll = itemView.findViewById(R.id.main_rec_ll);

        }
    }
}



