package com.myapp.lekkerlocationsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
 TextView nameTv;
 RadioButton  kmRb,mRb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameTv=findViewById(R.id.nameTv);
        mRb=findViewById(R.id.mRb);
        kmRb=findViewById(R.id.kmRb);
        loadNAme();
        loadSettings();

        mRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRb.setChecked(true);
                kmRb.setChecked(false);
                Map setMap=new HashMap();
                setMap.put("setting","mile");
                FirebaseFirestore.getInstance().collection("Settings").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(setMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SettingsActivity.this, "Setting changed successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        kmRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRb.setChecked(false);
                kmRb.setChecked(true);

                Map setMap=new HashMap();
                setMap.put("setting","km");
                FirebaseFirestore.getInstance().collection("Settings").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(setMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SettingsActivity.this, "Setting changed successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadSettings() {
        FirebaseFirestore.getInstance().collection("Settings").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.getString("setting").equals("km")){
                        kmRb.setChecked(true);
                        mRb.setChecked(false);
                    }
                    else  if (documentSnapshot.getString("setting").equals("mile")){
                        kmRb.setChecked(false);
                        mRb.setChecked(true);
                    }
                }
                else {
                    kmRb.setChecked(true);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNAme() {
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nameTv.setText(documentSnapshot.getString("name"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logout(View view) {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
       finish();
    }
}