package com.example.cloudnotetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;

public class Detail extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    Calendar calender = Calendar.getInstance();
    int hour =calender.get(Calendar.HOUR);
    int min =calender.get(Calendar.MINUTE);
    int sec =calender.get(Calendar.SECOND);
    FirebaseFirestore db = FirebaseFirestore.getInstance();





    TextView d_name;
    TextView d_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference().child("images/download.jpg");
        ImageView imageView = findViewById(R.id.imageDetail);
        Glide.with(this)
                .load(storageRef)
                .into(imageView);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        screenTrack("DetailAnalysis");

        d_name=findViewById(R.id.detail_name);
        d_detail=findViewById(R.id.detail_detail);

        d_name.setText(getIntent().getStringExtra("name").toString());
        d_detail.setText(getIntent().getStringExtra("detail").toString());

        d_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEvent("d","detail button","Button");
            }
        });
        d_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEvent("d","name button","Button");
            }
        });

    }
    public void screenTrack(String screen){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME , screen);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS , "DetailAnalysis");
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
        add.put("name", "DetailAnalysis");
        add.put("hours",h);
        add.put("minutes",m);
        add.put("seconds",s);
        db.collection("DetailAnalysis").add(add)
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
    public void btnEvent (String id , String name , String content){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID , id);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE , content);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME , name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);



    }
}