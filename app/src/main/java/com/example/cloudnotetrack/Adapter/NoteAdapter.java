package com.example.cloudnotetrack.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudnotetrack.Model.Category;
import com.example.cloudnotetrack.Model.Notee;
import com.example.cloudnotetrack.Note;
import com.example.cloudnotetrack.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private FirebaseAnalytics mFirebaseAnalytics;

    Context context;
    ArrayList<Notee> noteArrayList;
    private NoteAdapter.ItemClickListener mClickListener2;

    public NoteAdapter(Context context, ArrayList<Notee> noteArrayList, ItemClickListener mClickListener2) {
        this.context = context;
        this.noteArrayList = noteArrayList;
        this.mClickListener2 = mClickListener2;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

    }


    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item,parent,false);
        return new NoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Notee n =noteArrayList.get(position);
        holder.note_name.setText(n.getNote_name());
        holder.note_detail.setText(n.getNote_detail());
        holder.card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener2.onItemClick(holder.getAdapterPosition(),noteArrayList.get(position).getId());
                btnEvent("n","note","track_card note");

            }
        });

    }


    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView note_name;
        TextView note_detail;

        CardView card2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.note_name = itemView.findViewById(R.id.note_name);
            this.note_detail = itemView.findViewById(R.id.note_detail);

            this.card2=itemView.findViewById(R.id.card2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }

    }
    public interface ItemClickListener {
        void onItemClick(int position, String id);
    }
    void  setmClickListener2(ItemClickListener mClickListener2){
        this.mClickListener2=mClickListener2;
    }
    Notee getItem(int id) {
        return noteArrayList.get(id);
    }

    public void btnEvent (String id , String name , String content){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID , id);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE , content);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME , name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);



    }

}
