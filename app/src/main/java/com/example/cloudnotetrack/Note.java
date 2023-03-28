package com.example.cloudnotetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.cloudnotetrack.Adapter.CategoryAdapter;
import com.example.cloudnotetrack.Adapter.NoteAdapter;
import com.example.cloudnotetrack.Model.Category;
import com.example.cloudnotetrack.Model.Notee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Note extends AppCompatActivity  implements NoteAdapter.ItemClickListener {

    //fire analysis
    private FirebaseAnalytics mFirebaseAnalytics;
    Calendar calender = Calendar.getInstance();
    int hour =calender.get(Calendar.HOUR);
    int min =calender.get(Calendar.MINUTE);
    int sec =calender.get(Calendar.SECOND);

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView recyclerNOT;
    NoteAdapter adapterNOT;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    ArrayList<Notee> not_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        screenTrack("NoteAnalysis");

        recyclerNOT = findViewById(R.id.note_recyler);
        not_items = new ArrayList<Notee>();
        adapterNOT = new NoteAdapter(this,not_items,this);
        recyclerNOT.setAdapter(adapterNOT);

        String nn = getIntent().getStringExtra("name");
        GetNote(nn);
    }
    private void GetNote(String name) {

        db.collection("note").whereEqualTo("name",name).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("get", "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    String id = documentSnapshot.getId();
                                    String note = documentSnapshot.getString("name");
                                    String detail = documentSnapshot.getString("detail");



                                    Notee c = new Notee(id,note,detail);
                                    not_items.add(c);

                                    recyclerNOT.setLayoutManager(layoutManager);
                                    recyclerNOT.setHasFixedSize(true);
                                    recyclerNOT.setAdapter(adapterNOT);
                                    ;
                                    adapterNOT.notifyDataSetChanged();
                                    Log.e("get", not_items.toString());

                                }
                            }
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("get", "get failed ");


                    }
                });
    }

    @Override
    public void onItemClick(int position, String id) {

        Intent intent = new Intent(this, Detail.class);
        intent.putExtra("name",not_items.get(position).getNote_name());
        intent.putExtra("detail",not_items.get(position).getNote_detail());


        //   Log.e("id ",add_items.get(position).getId());
        startActivity(intent);


    }
    public void screenTrack(String screen){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME , screen);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS , "NoteAnalysis");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,bundle);


    }

    @Override
    protected void onPause() {
        Calendar calender = Calendar.getInstance();
        int hour2 =calender.get(Calendar.HOUR);
        int min2 =calender.get(Calendar.MINUTE);
        int sec2 =calender.get(Calendar.SECOND);

        int h = hour2-hour;
        int m = min2-min;
        int s = sec2-sec;

        HashMap<String , Object> add = new HashMap<>();
        add.put("name", "NoteAnalysis");
        add.put("hours",h);
        add.put("minutes",m);
        add.put("seconds",s);
        db.collection("NoteAnalysis").add(add)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("dana", "true");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("dana", "false");
                    }
                });
        Log.e("hour",String.valueOf(h));
        Log.e("minute",String.valueOf(m));
        Log.e("second",String.valueOf(s));
        super.onPause();
    }
}