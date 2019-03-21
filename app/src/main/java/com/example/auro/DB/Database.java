package com.example.auro.DB;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.EditText;
import android.widget.Toast;

import com.example.auro.Adapter.*;
import com.example.auro.Center_Incharge.Attendance;
import com.example.auro.Center_Incharge.Attendance_Student_List;
import com.example.auro.Center_Incharge.Center_Incharge_Batch_Report;
import com.example.auro.Center_Incharge.Create_Batch;
import com.example.auro.Center_Incharge.Enroll_Student;
import com.example.auro.Chat;
import com.example.auro.Director.Add_Standard;
import com.example.auro.Director.Assign_Batch;
import com.example.auro.Director.Director_Batch_Report;
import com.example.auro.Director.Director_Request_Details;
import com.example.auro.Director.Project_Manager_Details;
import com.example.auro.Director.Project_Manager_List;
import com.example.auro.Director.Registration;
import com.example.auro.HomePage;
import com.example.auro.MainActivity;
import com.example.auro.Pending_Batch_Request;
import com.example.auro.Pending_Student_Batch_List;
import com.example.auro.Private_message;
import com.example.auro.Project_Manager.Center_Incharge_List;
import com.example.auro.Project_Manager.ProjectManager_AssignBatch;
import com.example.auro.Project_Manager.Project_Manager_Batch_Report;
import com.example.auro.Project_Manager.Request_Batch_Details;
import com.example.auro.Project_Manager.Request_Student_list;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
    public static StorageReference sr = FirebaseStorage.getInstance().getReference();

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

    public static void getProjectManagerList(final String username,final Assign_Batch r,final Context c){
        dr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> projectManager = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    UserDetails u = postSnap.getValue(UserDetails.class);
                    if(u.getReporting().equals(username) && u.getDesignation().equals("Project Manager")){
                        projectManager.add(u.getName());
                    }
                }
                r.setProjectManagerSpinner(projectManager, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getStandards(final Assign_Batch r,final Context c){
        dr.child("CourseDetails").child("Standards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> std = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Standards s = postSnap.getValue(Standards.class);
                    std.add(s.getStd());
                }

                r.setStandardsSpinner(std, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void addAssignBatch(final String batch, final String projectManager, final String std, final Context c){

        AssignBatches ab = new AssignBatches();
        ab.setNoOfBatches(batch);
        ab.setProjectManager(projectManager);
        ab.setStd(std);
        ab.setBatchesCreated("0");

        dr.child("Batches").child("Assignment").child("Project Manager").child(projectManager).child(std).setValue(ab);

        Toast.makeText(c,"Success",Toast.LENGTH_SHORT).show();

        Intent i = new Intent(c, HomePage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }

    public static void getProjectManagerList(final String username, final Project_Manager_List r, final Context c){
        dr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserDetails> projectManager = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    UserDetails u = postSnap.getValue(UserDetails.class);
                    if(u.getReporting().equals(username) && u.getDesignation().equals("Project Manager")){

                        projectManager.add(u);

                    }
                }
                r.setProjectManagerList(projectManager, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getCenterInchargeList(final String username, final Project_Manager_Details r, final Context c){
        dr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserDetails> centerincharge = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    UserDetails u = postSnap.getValue(UserDetails.class);
                    if(u.getReporting().equals(username) && u.getDesignation().equals("Center Incharge")){

                        centerincharge.add(u);

                    }
                }
                r.setCenterInchargeList(centerincharge, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getProjectManagerDetails(final String proman, final Project_Manager_Details r, final Context c){
        dr.child("Batches").child("Assignment").child("Project Manager").child(proman).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AssignBatches> managerdetails = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    AssignBatches u = postSnap.getValue(AssignBatches.class);
                    managerdetails.add(u);
                }
                r.setProjectManagerDetails(managerdetails,c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getCenterInchargeList(final String username, final ProjectManager_AssignBatch r, final Context c){
        dr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> centerincharge = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    UserDetails u = postSnap.getValue(UserDetails.class);
                    if(u.getReporting().equals(username) && u.getDesignation().equals("Center Incharge")){
                        centerincharge.add(u.getName());
                    }
                }
                r.setCenterInchargeList(centerincharge, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getStandards(final ProjectManager_AssignBatch r, final Context c){
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

    public static void addAssignBatchCenterIncharge(final String batch, final String centerIncharge, final String std, final Context c){

        AssignBatches ab = new AssignBatches();
        ab.setNoOfBatches(batch);
        ab.setCenterIncharge(centerIncharge);
        ab.setStd(std);
        ab.setBatchesCreated("0");

        dr.child("Batches").child("Assignment").child("Center Incharge").child(centerIncharge).child(std).setValue(ab);

        Toast.makeText(c,"Success",Toast.LENGTH_SHORT).show();

        Intent i = new Intent(c, HomePage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }

    public static void getStandards(final Create_Batch r, final Context c){
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

    public static void createBatch(final String batch,final String sD,final String eD,final String sT,final String eT,final String limit,final String username,final String days,final String standard,final String reporting)
    {

        Batch bt = new Batch();
        bt.setBatch_name(batch);
        bt.setDays(days);
        bt.setStart_date(sD);
        bt.setEnd_date(eD);
        bt.setStart_time(sT);
        bt.setEnd_time(eT);
        bt.setStandard(standard);
        bt.setIncharge(username);
        bt.setStatus(reporting);
        bt.setStd_limit(limit);
        bt.setManager(reporting);

        dr.child("Batches").child("Batch Details").child(batch).setValue(bt);

        dr.child("CourseDetails").child("Lessons").child(standard).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    String data = postSnap.getValue().toString();
                    dr.child("Batches").child("Batch Progress").child(batch).child("Topics").child(data).setValue("Incomplete");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void enrollStudent(final String stdID, final String stdName, final String fN, final String fS, final String mN, final String mS, final String stdgender, final String batch,final String std,final String reporting, final String DOB, final String stdaddress,final String url,final Context c){
        StudentDetails sd = new StudentDetails();
        sd.setStudentID(stdID);
        sd.setStudentName(stdName);
        sd.setAddress(stdaddress);
        sd.setGender(stdgender);
        sd.setFatherName(fN);
        sd.setFatherStatus(fS);
        sd.setMotherName(mN);
        sd.setMotherStatus(mS);
        sd.setStandard(std);
        sd.setDob(DOB);
        sd.setStatus(reporting);
        sd.setBatch(batch);
        sd.setUrl(url);
        dr.child("Batches").child("Student Details").child(stdID).setValue(sd);

        Toast.makeText(c,"Student enrolled successfully",Toast.LENGTH_LONG).show();
    }

    public static void uploadDP(final Uri filepath, final String stdID, final Enroll_Student r, final Context con){
        final StorageReference ref = sr.child(stdID);
        ref.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downurl = taskSnapshot.getStorage().getDownloadUrl().toString();
                        r.setUrl(downurl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(con, "Upload Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                    }
                });
    }

    public static void getBatches(final String username, final Pending_Batch_Request r, final Context c){
        dr.child("Batches").child("Batch Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Batch> batches = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Batch u = postSnap.getValue(Batch.class);
                    if(u.getStatus().equals(username))
                    {
                        batches.add(u);
                    }
                }
                r.setBatch(batches, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatches(final String username, final Enroll_Student r, final Context c){
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> batches = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Batch u = postSnap.getValue(Batch.class);
                    if(u.getIncharge().equals(username) && u.getStatus().equals("Approved"))
                    {
                        batches.add(u.getBatch_name());
                    }
                }
                r.setBatch(batches, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getStandard(final String batch, final Enroll_Student r){
        dr.child("Batches").child("Batch Details").child(batch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Batch u = dataSnapshot.getValue(Batch.class);

                r.setStandard(u.getStandard());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchDetails(final String batch, final Request_Batch_Details r, final Context c){
        dr.child("Batches").child("Batch Details").child(batch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Batch bt = dataSnapshot.getValue(Batch.class);
                r.setBatchDetails(bt,c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void approveBatch(final String batch)
    {
        dr.child("Batches").child("Batch Details").child(batch).child("status").setValue("Approved");
    }

    public static void rejectBatch(final String batch,final String remark)
    {
        dr.child("Batches").child("Batch Details").child(batch).child("status").setValue("Rejected");
        dr.child("Batches").child("Batch Details").child(batch).child("Remark").setValue(remark);
    }

    public static void escalateBatch(final String batch, final String reporting)
    {
        dr.child("Batches").child("Batch Details").child(batch).child("status").setValue(reporting);
    }

    public static void getBatchDetails(final String batch, final Director_Request_Details r, final Context c){
        dr.child("Batches").child("Batch Details").child(batch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Batch bt = dataSnapshot.getValue(Batch.class);
                r.setBatchDetails(bt,c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchList(final String status, final Pending_Student_Batch_List r, Context c){
        dr.child("Batches").child("Student Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> batchlist = new ArrayList<>();

                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    StudentDetails u = postSnap.getValue(StudentDetails.class);
                    if(u.getStatus().equals(status))
                    {
                        if(!batchlist.contains(u.getBatch()))
                        {
                            batchlist.add(u.getBatch());
                        }
                    }
                }

                r.setBatch(batchlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getStudentDetails(final String batch,final String name, final Request_Student_list r, final Context c)
    {
        dr.child("Batches").child("Student Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<StudentDetails> list = new ArrayList<>();
                List<Status> list2 = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    StudentDetails u = postSnap.getValue(StudentDetails.class);
                    Status s = new Status();
                    s.setStatus(0);
                    if(u.getBatch().equals(batch) && u.getStatus().equals(name))
                    {
                        list.add(u);
                        list2.add(s);
                    }
                }
                r.setStudentDetails(list,list2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void approveStudent(final String id)
    {
        dr.child("Batches").child("Student Details").child(id).child("status").setValue("Approved");
    }

    public static void rejectStudent(final String id, final String remark)
    {
        dr.child("Batches").child("Student Details").child(id).child("status").setValue("Rejected");
        dr.child("Batches").child("Student Details").child(id).child("Remark").setValue(remark);
    }

    public static void escalateStudent(final String id, final String reporting)
    {
        dr.child("Batches").child("Student Details").child(id).child("status").setValue(reporting);
    }

    public static void getBatchList(final String incharge, final Center_Incharge_Batch_Report r, Context c){
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> batchlist = new ArrayList<>();
                batchlist.add("All");
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Batch u = postSnap.getValue(Batch.class);
                    if(u.getIncharge().equals(incharge) && u.getStatus().equals("Approved"))
                    {
                        batchlist.add(u.getBatch_name());
                    }
                }

                r.setBatch(batchlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getSelectedCenterBatch(final String batchs, final String username, final Center_Incharge_Batch_Report r, final Context c)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Batch> batch = new ArrayList<>();

                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getStatus().equals("Approved") && b.getIncharge().equals(username))
                    {
                        if(batchs.equals("All"))
                        {
                            batch.add(b);
                        }
                        else if(b.getBatch_name().equals(batchs))
                        {
                            batch.add(b);
                        }
                    }
                }

                r.setBatchDetailList(batch);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getCenterInchargeList(final String username, final Project_Manager_Batch_Report r, final Context c){
        dr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> centerincharge = new ArrayList<>();
                centerincharge.add("All");
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    UserDetails u = postSnap.getValue(UserDetails.class);
                    if(u.getReporting().equals(username) && u.getDesignation().equals("Center Incharge")){

                        centerincharge.add(u.getName());

                    }
                }
                r.setCenterInchargeList(centerincharge, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchList(final String incharge, final Project_Manager_Batch_Report r, final Context c)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> batch = new ArrayList<>();
                batch.add("All");
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getStatus().equals("Approved") && b.getIncharge().equals(incharge))
                    {
                        batch.add(b.getBatch_name());
                    }
                }

                r.setBatchList(batch);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchDetails(final String name, final Project_Manager_Batch_Report r, final Context c)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Batch> bat = new ArrayList<>();

                for(DataSnapshot postSnap : dataSnapshot.getChildren())
                {
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getManager().equals(name) && b.getStatus().equals("Approved"))
                    {
                        bat.add(b);
                    }
                }

                r.setBatchDetailList(bat,0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchDetails(final String batchs, final String username, final Project_Manager_Batch_Report r, final Context c)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Batch> batch = new ArrayList<>();

                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getStatus().equals("Approved") && b.getIncharge().equals(username))
                    {
                        if(batchs.equals("All"))
                        {
                            batch.add(b);
                        }
                        else if(b.getBatch_name().equals(batchs))
                        {
                            batch.add(b);
                        }
                    }
                }

                r.setBatchDetailList(batch,1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getProjectManagerList(final String username, final Director_Batch_Report r, final Context c){
        dr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> projectManager = new ArrayList<>();
                projectManager.add("All");
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    UserDetails u = postSnap.getValue(UserDetails.class);
                    if(u.getReporting().equals(username) && u.getDesignation().equals("Project Manager")){

                        projectManager.add(u.getName());

                    }
                }
                r.setProjectManagerList(projectManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchDetails(final Director_Batch_Report r, final Context c)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Batch> batch = new ArrayList<>();

                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getStatus().equals("Approved"))
                    {
                       batch.add(b);
                    }
                }
                r.setBatchDetailList(batch, 2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getCenterInchargeList(final String username, final Director_Batch_Report r, final Context c){
        dr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> centerincharge = new ArrayList<>();
                centerincharge.add("All");
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    UserDetails u = postSnap.getValue(UserDetails.class);
                    if(u.getReporting().equals(username) && u.getDesignation().equals("Center Incharge")){

                        centerincharge.add(u.getName());

                    }
                }
                r.setCenterInchargeList(centerincharge);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchDetails(final String name, final Director_Batch_Report r, final Context c)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Batch> bat = new ArrayList<>();

                for(DataSnapshot postSnap : dataSnapshot.getChildren())
                {
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getManager().equals(name) && b.getStatus().equals("Approved"))
                    {
                        bat.add(b);
                    }
                }

                r.setBatchDetailList(bat,1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchDetails(final String name, final Director_Batch_Report r)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Batch> bat = new ArrayList<>();

                for(DataSnapshot postSnap : dataSnapshot.getChildren())
                {
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getIncharge().equals(name) && b.getStatus().equals("Approved"))
                    {
                        bat.add(b);
                    }
                }

                r.setBatchDetailList(bat,0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchList(final String incharge, final Director_Batch_Report r, final Context c)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> batch = new ArrayList<>();
                batch.add("All");
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getStatus().equals("Approved") && b.getIncharge().equals(incharge))
                    {
                        batch.add(b.getBatch_name());
                    }
                }

                r.setBatchList(batch);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchDetail(final String batch_name, final Director_Batch_Report r)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Batch> bat = new ArrayList<>();

                for(DataSnapshot postSnap : dataSnapshot.getChildren())
                {
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getBatch_name().equals(batch_name) && b.getStatus().equals("Approved"))
                    {
                        bat.add(b);
                    }
                }

                r.setBatchDetailList(bat,0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBatchList(final String incharge, final String day, final Calendar dateCalendar, final Attendance a, final Context c)
    {
        dr.child("Batches").child("Batch Details").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();

                for(DataSnapshot postSnap : dataSnapshot.getChildren())
                {
                    try
                    {
                        Batch b = postSnap.getValue(Batch.class);

                        String start = b.getStart_date();
                        String end = b.getEnd_date();

                        java.util.Date startDate, endDate;
                        DateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

                        startDate = dt.parse(start);
                        endDate = dt.parse(end);

                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(startDate);

                        Calendar endCalendar = new GregorianCalendar();
                        endCalendar.setTime(endDate);

                        if(((calendar.before(dateCalendar) || calendar.equals(dateCalendar)) && (dateCalendar.equals(endCalendar) || dateCalendar.before(endCalendar))) && b.getDays().contains(day) && b.getIncharge().equals(incharge) && b.getStatus().equals("Approved") )
                        {
                            list.add(b.getBatch_name());
                        }

                    }
                    catch (Exception e){}
                }

                a.setBatchList(list,c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getStudentID(final String batch_name, final Attendance_Student_List a, final Context c)
    {
        dr.child("Batches").child("Student Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                List<Status> list2 = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren())
                {
                    StudentDetails s = postSnap.getValue(StudentDetails.class);
                    Status st = new Status();
                    st.setStatus(1);

                    if(s.getBatch().equals(batch_name) && s.getStatus().equals("Approved"))
                    {
                        list.add(s.getStudentID() + " : " + s.getStudentName());
                        list2.add(st);
                    }
                }

                a.setStudentList(list,list2,c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void putAttendance(final String batch, final String date, final List<String> list, final List<Status> list2,final String stat, final String topics_covered,final String incharge, final Context c)
    {
        if(stat.equals("Open"))
        {
            int size = list.size();
            for(int inc=0; inc<size ; inc++)
            {
                String id = list.get(inc);
                id = id.substring(0,id.indexOf(" : "));

                Status st = list2.get(inc);
                int status = st.getStatus();

                dr.child("Batches").child("Attendance").child(batch).child(date).child(id).setValue(status);
            }

            dr.child("Batches").child("Batch Progress").child(batch).child("Topics").child(topics_covered).setValue(date);
            dr.child("Batches").child("Batch Progress").child(batch).child("Status").child(date).setValue(stat);
        }
        else
        {
            dr.child("Batches").child("Batch Progress").child(batch).child("Status").child(date).setValue(stat);
        }
    }

    public static void getTopics(final String batch, final Attendance_Student_List a, final Context c)
    {
        dr.child("Batches").child("Batch Progress").child(batch).child("Topics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> list = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren())
                {
                    String data = postSnap.getValue().toString();

                    if(data.equals("Incomplete"))
                    {
                        list.add(postSnap.getKey());
                    }
                }


                a.setTopics(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getUserList(final String username, final String designation, final Chat chat, final Context c)
    {
        dr.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserDetails> list = new ArrayList<>();

                UserDetails ud = new UserDetails();
                ud.setName("BroadCast");
                ud.setDesignation("Group Chat");
                list.add(ud);

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    UserDetails u = snapshot.getValue(UserDetails.class);
                    if(!(u.getName().equals(username) && u.getDesignation().equals(designation)))
                    {
                        list.add(u);
                    }
                }

                chat.setUserList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void chat(final String msg, final String from, final String to)
    {
        Message m = new Message();

        m.setMessage(msg);
        m.setFrom(from);
        m.setTo(to);

        dr.child("Chat").push().setValue(m);
    }

    public static void getChat(final String from, final String to, final Private_message p, final Context c)
    {
        dr.child("Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Message> list = new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Message m = snapshot.getValue(Message.class);

                    if(m.getFrom().equals(from) && m.getTo().equals(to) || m.getFrom().equals(to) && m.getTo().equals(from) || to.equals("BroadCast") && m.getTo().equals(to))
                    {
                        list.add(m);
                    }
                }
                p.setChat(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        public static void getCenterInchargeList(final String username, final Center_Incharge_List r, final Context c){
        dr.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> centerincharge = new ArrayList<>();
                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    UserDetails u = postSnap.getValue(UserDetails.class);
                    if(u.getReporting().equals(username) && u.getDesignation().equals("Center Incharge")){
                        centerincharge.add(u.getName());
                    }
                }
                r.setCenterInchargeList(centerincharge, c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
