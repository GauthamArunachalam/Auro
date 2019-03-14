package com.example.auro.DB;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.auro.Adapter.UserDetails;
import com.example.auro.HomePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {

    public static DatabaseReference dr = FirebaseDatabase.getInstance().getReference();


    public static void login(final String userName, final String password, final Context con){
        dr.child("Users").child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   UserDetails ud = dataSnapshot.getValue(UserDetails.class);
                   if(ud == null){
                       Toast.makeText(con, "No Account", Toast.LENGTH_SHORT).show();
                   }
                    else if(userName.equals(ud.getName()) && password.equals(ud.getPass())){
                        Toast.makeText(con, "Login Successfull", Toast.LENGTH_SHORT).show();

                        //shared Preference
                       Intent i = new Intent(con, HomePage.class);
                       i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       con.startActivity(i);
                    }
                    else{
                        Toast.makeText(con, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
