package com.example.cloudnotetrack.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudnotetrack.Model.Category;
import com.example.cloudnotetrack.Note;
import com.example.cloudnotetrack.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private FirebaseAnalytics mFirebaseAnalytics;


    Context context;
    ArrayList<Category> CATArrayList;
    private ItemClickListener mClickListener;

    public CategoryAdapter(Context context, ArrayList<Category> CATArrayList , ItemClickListener mClickListener ) {
        this.context = context;
        this.CATArrayList = CATArrayList;
        this.mClickListener = mClickListener;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Category a = CATArrayList.get(position);
        holder.cat_name.setText(a.getCat_name());
        holder.card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mClickListener.onItemClick(holder.getAdapterPosition(),CATArrayList.get(position).getId());
               btnEvent("c","category","track_card");
            }
        });

    }

    @Override
    public int getItemCount() {
        return CATArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView cat_name;
        CardView card1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cat_name = itemView.findViewById(R.id.cat_name);
            this.card1 = itemView.findViewById(R.id.cat_card);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(int position, String id);
    }
    Category getItem(int id) {
        return CATArrayList.get(id);
    }


    public void btnEvent (String id , String name , String content){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID , id);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE , content);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME , name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);



    }

}

