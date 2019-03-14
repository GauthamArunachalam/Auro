package com.example.auro;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.auro.Adapter.UserDetails;
import com.example.auro.DB.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static Button login;
    public static EditText name, pass, desig;

    public static String userName, password, desi;

    public static DatabaseReference df = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.login);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.pass);

        System.out.println("Hello World");
        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String password = pass.getText().toString();
                Context con = getApplicationContext();
                Database.login(userName, password, con);
            }
        });
    }
}
