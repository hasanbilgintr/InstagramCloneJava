package com.hasanbilgin.instagramclonejava.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.hasanbilgin.instagramclonejava.databinding.RecyclerviewRowBinding;
import com.hasanbilgin.instagramclonejava.model.PostModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private ArrayList<PostModel> postModelArrayList;
    //context sadece glide için eklenmişti
    private Context context;

    public PostAdapter(ArrayList<PostModel> postModelArrayList,Context context) {
        this.postModelArrayList = postModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRowBinding recyclerviewRowBinding = RecyclerviewRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostHolder(recyclerviewRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.recyclerviewRowBinding.emailTextView.setText(postModelArrayList.get(position).getEmail());
        holder.recyclerviewRowBinding.commnentTextView.setText(postModelArrayList.get(position).getComment());
        Picasso.get().load(postModelArrayList.get(position).getDowsloadUrl()).into(holder.recyclerviewRowBinding.pictureImageView);
        //glide ile netten (sunucudan) resim çekme
        //Glide.with(context).load(postModelArrayList.get(position).getDowsloadUrl()).into(holder.recyclerviewRowBinding.pictureImageView);


    }

    @Override
    public int getItemCount() {
        return postModelArrayList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {
        RecyclerviewRowBinding recyclerviewRowBinding;

        public PostHolder(RecyclerviewRowBinding recyclerviewRowBinding) {
            super(recyclerviewRowBinding.getRoot());
            this.recyclerviewRowBinding = recyclerviewRowBinding;
        }
    }

}
