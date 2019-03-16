package com.example.auro.DB;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;

import com.example.auro.Adapter.Standards;
import com.example.auro.Adapter.UserDetails;
import com.example.auro.Director.Add_Standard;
import com.example.auro.Director.Registration;
import com.example.auro.HomePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Database {

    public static DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    public static void login(final String userName, final String password, final Context con, final EditText name, final EditText pass){
        dr.child("Users").child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   UserDetails ud = dataSnapshot.getValue(UserDetails.class);
                   if(ud == null){
                       name.setError("User not found");
                       name.requestFocus();
                   }
                    else if(userName.equals(ud.getName()) && password.equals(ud.getPass())){
                        Toast.makeText(con, "Login Successfull", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = con.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE).edit();
                        editor.putString("UserName",ud.getName());
                        editor.putString("Designation",ud.getDesignation());
                        editor.putString("Reporting",ud.getReporting());
                        editor.apply();

                       Intent i = new Intent(con, HomePage.class);
                       i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       con.startActivity(i);

                    }
                    else{
                        pass.setError("Invalid password");
                        pass.requestFocus();
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void register(final String userName, final String pass, final String desig, final String reporting, final Context con){
        dr.child("Users").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails u = dataSnapshot.getValue(UserDetails.class);

                if(u == null){

                    UserDetails ud = new UserDetails();
                    ud.setName(userName);
                    ud.setPass(pass);
                    ud.setDesignation(desig);
                    ud.setReporting(reporting);

                    dr.child("Users").child(userName).setValue(ud);
                    Toast.makeText(con, "Registeration Successfull", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(con, HomePage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    con.startActivity(i);

                }else{
                    Toast.makeText(con, "User Id already Exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void changePassword(final String username, final String oldpass, final String newpass, final Context con){
        dr.child("Users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails ud = dataSnapshot.getValue(UserDetails.class);

                if(oldpass.equals(ud.getPass()))
                {
                    ud.setPass(newpass);
                    dr.child("Users").child(username).setValue(ud);

                    Intent i = new Intent(con, HomePage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    con.startActivity(i);
                }
                else
                {
                    Toast.makeText(con,"Old pass mismatch",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getProjectManagerList(final Registration r, final Context c){
        dr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<String>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    UserDetails u = postSnap.getValue(UserDetails.class);
                    if(u.getDesignation().equals("Project Manager")){
                        list.add(u.getName());
                    }
                }
                r.setProjectManagerSpinner(list, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getStandards(final Add_Standard r, final Context c){
        dr.child("CourseDetails").child("Standards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<String>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Standards u = postSnap.getValue(Standards.class);

                    list.add(u.getStd());

                }
                r.setStandardsSpinner(list, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void createStandard(final String standard, final Context con){

        Standards sd = new Standards();

        sd.setStd(standard);

        dr.child("CourseDetails").child("Standards").child(standard).setValue(sd);

    }

    public static void createTopic(final String standard,final String topic, final Context con){

        dr.child("CourseDetails").child("Lessons").child(standard).child(topic).setValue(topic);

    }

    public static void getTopic(final String std, final Add_Standard as, final Context con)
    {
        dr.child("CourseDetails").child("Lessons").child(std).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuffer top = new StringBuffer();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    top.append(dataSnapshot1.getValue()+ " \n");
                }

                as.setTop(top,con);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
