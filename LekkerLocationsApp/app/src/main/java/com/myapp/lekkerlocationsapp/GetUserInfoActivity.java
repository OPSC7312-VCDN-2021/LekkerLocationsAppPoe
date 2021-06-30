package com.myapp.lekkerlocationsapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GetUserInfoActivity extends AppCompatActivity {
    SweetAlertDialog pDialog;

    EditText nameEt,surnamemEt,dobEt,waEt,haEt;
      String uid;
    FirebaseFirestore rootRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info);


        nameEt=findViewById(R.id.nameEt);
        surnamemEt=findViewById(R.id.surnameEt);
        dobEt=findViewById(R.id.dobEt);
        waEt=findViewById(R.id.waEt);
        haEt=findViewById(R.id.haEt);

        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef = FirebaseFirestore.getInstance();




    }



    public void continueBtn(View view) {

        if (nameEt.getText().toString().length()<1){
            nameEt.setError("This field must be filled");

            return;
        }
        if (surnamemEt.getText().toString().length()<1){
            surnamemEt.setError("This field must be filled");
            return;
        }
        if (dobEt.getText().toString().length()<1){
            dobEt.setError("This field must be filled");
            return;
        }
        if (waEt.getText().toString().length()<1){
            waEt.setError("This field must be filled");
            return;
        }
        if (haEt.getText().toString().length()<1){
            haEt.setError("This field must be filled");
            return;
        }



        saveData();

    }

    private void saveData() {



        pDialog = new SweetAlertDialog(   GetUserInfoActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#CD5C5C"));
        pDialog.setTitleText("Creating Account...");
        pDialog.setContentText("Please wait !");
        pDialog.setCancelable(false);
        if (pDialog!=null)
            pDialog.show();







        String name=nameEt.getText().toString();
        String srname=surnamemEt.getText().toString();
        String dob=dobEt.getText().toString();
        String wa=waEt.getText().toString();
        String ha=haEt.getText().toString();
        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();




        Map<String,String> userMapDetailed=new HashMap<>();

        userMapDetailed.put("uid",uid);
        userMapDetailed.put("name",name);
        userMapDetailed.put("dob",dob);
        userMapDetailed.put("workAddress",wa);
        userMapDetailed.put("email",email);
        userMapDetailed.put("houseAddress",ha);
        userMapDetailed.put("surName",srname);


         rootRef.collection("Users").document(uid).set(userMapDetailed).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 pDialog.dismissWithAnimation();
                 startActivity(new Intent(getApplicationContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                 finish();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 pDialog.dismissWithAnimation();
                 Toast.makeText( GetUserInfoActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

             }
         });










    }
}