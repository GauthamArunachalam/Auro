package com.example.auro.DB;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;

import com.example.auro.Adapter.*;
import com.example.auro.Center_Incharge.Center_Incharge_Batch_Report;
import com.example.auro.Center_Incharge.Create_Batch;
import com.example.auro.Center_Incharge.Enroll_Student;
import com.example.auro.Director.Add_Standard;
import com.example.auro.Director.Assign_Batch;
import com.example.auro.Director.Director_Request_Details;
import com.example.auro.Director.Project_Manager_Details;
import com.example.auro.Director.Project_Manager_List;
import com.example.auro.Director.Registration;
import com.example.auro.HomePage;
import com.example.auro.Pending_Batch_Request;
import com.example.auro.Pending_Student_Batch_List;
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

                for(DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Batch b = postSnap.getValue(Batch.class);

                    if(b.getManager().equals(name) && b.getStatus().equals("Approved"))
                    {
                        bat.add(b);
                    }
                }

                r.setBatchDetailList(bat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
