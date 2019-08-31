package com.example.githubapi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.githubapi.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    EditText username ;
    MaterialButton submit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username_et) ;
        submit = findViewById(R.id.submit_btn) ;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this,"Please Enter Username", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(MainActivity.this, BasicDetails.class);
                    intent.putExtra("Username", username.getText().toString()) ;
                    startActivity(intent) ;
                }
            }
        });
    }
}
