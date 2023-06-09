package com.example.cloudnotetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.cloudnotetrack.Adapter.CategoryAdapter;
import com.example.cloudnotetrack.Model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.ItemClickListener {
    //fire analysis
    private FirebaseAnalytics mFirebaseAnalytics;
    Calendar calender = Calendar.getInstance();
    int hour =calender.get(Calendar.HOUR);
    int min =calender.get(Calendar.MINUTE);
    int sec =calender.get(Calendar.SECOND);

    //fire store
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerCat;
    CategoryAdapter adapterCAT;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    ArrayList<Category> cat_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // trak
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        screenTrack("CategoryAnalysis");
        //recycelr
        recyclerCat = findViewById(R.id.cat_recycler);
        cat_items = new ArrayList<Category>();
        adapterCAT = new CategoryAdapter(this,cat_items ,this);
        recyclerCat.setAdapter(adapterCAT);

        GetCategory();
    }
    private void GetCategory() {

        db.collection("category").get()
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
                                    String CAT = documentSnapshot.getString("name");


                                    Category c = new Category(id,CAT);
                                    cat_items.add(c);

                                    recyclerCat.setLayoutManager(layoutManager);
                                    recyclerCat.setHasFixedSize(true);
                                    recyclerCat.setAdapter(adapterCAT);
                                    ;
                                    adapterCAT.notifyDataSetChanged();
                                    Log.e("get", cat_items.toString());

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

        Intent intent = new Intent(this, Note.class);
        intent.putExtra("name",cat_items.get(position).getCat_name());

        //   Log.e("id ",add_items.get(position).getId());
        startActivity(intent);
    }

    public void screenTrack(String screen){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME , screen);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS , "CategoryAnalysis");
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
        add.put("name", "CategoryAnalysis");
        add.put("hours",h);
        add.put("minutes",m);
        add.put("seconds",s);
        db.collection("CategoryAnalysis").add(add)
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