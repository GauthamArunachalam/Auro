package com.example.auro;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.auro.DB.Database;

public class Change_Password extends AppCompatActivity {
    private EditText OldPassword,NewPassword,RePassword;
    private String oldPassword,newPassword,rePassword;
    private Button Change;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String username, designation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);

        OldPassword = (EditText) findViewById(R.id.oldPass);
        NewPassword = (EditText)findViewById(R.id.newPass);
        RePassword = (EditText)findViewById(R.id.rePass);
        Change = (Button)findViewById(R.id.change);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString("UserName",null);
        designation = prefs.getString("Designation",null);

        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = OldPassword.getText().toString();
                newPassword = NewPassword.getText().toString();
                rePassword = RePassword.getText().toString();

                if(newPassword.equals(rePassword)){

                    Toast.makeText(getApplicationContext(),"pass",Toast.LENGTH_LONG).show();

                    Database.changePassword(username,oldPassword,newPassword,getApplicationContext());
                }
            }
        });


    }
}
