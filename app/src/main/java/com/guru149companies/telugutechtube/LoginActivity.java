package com.guru149companies.telugutechtube;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView donothaveaccount;
    EditText email, password;
    String semail, spass;
    Button Login;
    FirebaseAuth firebaseAuth;
    Dialog progress_dialog;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.setNavigationBarColor(this.getResources().getColor(R.color.colorAccent));
        donothaveaccount = findViewById(R.id.donothaveaccount);
        firebaseAuth=FirebaseAuth.getInstance();
        email = findViewById(R.id.logeditTextEmail);
        Login = findViewById(R.id.cirLoginButton);
        progress_dialog=new Dialog(this);
        progress_dialog.setContentView(R.layout.progress_dialog);
        password = findViewById(R.id.logeditTextPassword);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedittexts();
            }
        });

        donothaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkedittexts() {
        semail = email.getText().toString().trim();
        spass = password.getText().toString().trim();
        if (semail.isEmpty()) {
            email.setError("Please enter Email");
        } else if (!semail.contains("gmail.com")) {
            email.setError("Email shoul contain @gmail.com");
        } else if (spass.length() < 8) {
                password.setError("Please enter Valid Details");
        }else
        {
            CheckAndCreateAccount(semail,spass);
        }

    }

    private void CheckAndCreateAccount(String semail, String spass) {
        firebaseAuth.signInWithEmailAndPassword(semail,spass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progress_dialog.dismiss();
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress_dialog.dismiss();
                Toast.makeText(LoginActivity.this, "Please check Internet Connection\nand Try Agian", Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText(this, "Ready to Create Acount", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LoginActivity.this.finish();
    }
}