package com.guru149companies.telugutechtube;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {
    ArrayList<UploadVideoModel> post;
    Context context;

    public ProfilePostAdapter() {
    }

    public ProfilePostAdapter(ArrayList<UploadVideoModel> post, Context context) {
        this.post = post;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UploadVideoModel uploadVideoModel = post.get(position);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(Uri.parse(uploadVideoModel.getVideoUri())).apply(options).into(holder.post_pic);

    }

    @Override
    public int getItemCount() {
        return post.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView post_pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_pic=itemView.findViewById(R.id.profile_post_img);
        }
    }
}
