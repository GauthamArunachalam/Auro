package com.example.auro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.auro.Center_Incharge.Center_Incharge_Home_Page;
import com.example.auro.Director.Director_Home_Page;
import com.example.auro.Project_Manager.Project_Manager_Home_Page;

public class HomePage extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username, designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},1);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        designation = prefs.getString("Designation",null);

        if(designation.equals("Director"))
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Director_Home_Page director_home_page = new Director_Home_Page();
            fragmentTransaction.add(R.id.fragment_container,director_home_page,null);
            fragmentTransaction.commit();
        }
        else if(designation.equals("Center Incharge"))
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Center_Incharge_Home_Page center_incharge_home_page = new Center_Incharge_Home_Page();
            fragmentTransaction.add(R.id.fragment_container,center_incharge_home_page,null);
            fragmentTransaction.commit();
        }
        else if(designation.equals("Project Manager"))
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Project_Manager_Home_Page project_manager_home_page = new Project_Manager_Home_Page();
            fragmentTransaction.add(R.id.fragment_container,project_manager_home_page,null);
            fragmentTransaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.popup_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.logout){

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString("UserName",null);
            editor.putString("Designation",null);
            editor.putString("Reporting",null);
            editor.apply();

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();


        }else if(id == R.id.changePassword){
            startActivity(new Intent(getApplicationContext(),Change_Password.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
