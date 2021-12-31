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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    TextView alreadyihaveaccount;
    EditText name, email, password, conpassword, mobile;
    Button register;
    FirebaseAuth firebaseAuth;
    Dialog progress_dialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("NewApi")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Window window = this.getWindow();
        window.setNavigationBarColor(this.getResources().getColor(R.color.colorAccent));
        alreadyihaveaccount = findViewById(R.id.alreadyhaveaccount);
        firebaseAuth = FirebaseAuth.getInstance();
        alreadyihaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        conpassword = findViewById(R.id.editTextconPassword);
        mobile = findViewById(R.id.editTextMobile);
        register = findViewById(R.id.cirRegisterButton);
        progress_dialog = new Dialog(this);
        progress_dialog.setContentView(R.layout.progress_dialog);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedittexts();

            }
        });
    }

    private void checkedittexts() {
        String semail, smobilr, spass, sconpass, sname;
        semail = email.getText().toString().trim();
        sname = name.getText().toString().trim();
        spass = password.getText().toString().trim();
        sconpass = conpassword.getText().toString().trim();
        smobilr = mobile.getText().toString().trim();
        if (sname.isEmpty()) {
            name.setError("Please enter Your name");
        } else if (semail.isEmpty()) {
            email.setError("please Enter Email");
        } else if (!semail.contains("@gmail.com")) {
            email.setError("Email should contain @gmail.com");
        } else if (smobilr.isEmpty()) {
            mobile.setError("Please mobile Number");
        } else if (smobilr.length() <= 9) {
            mobile.setError("Please Enter Valid Mobile Number");
        } else if (spass.length() <= 8) {
            password.setError("Password Should be more than 8 characteristics");
        } else if (!sconpass.equals(spass)) {
            conpassword.setError("Password not matching");
        } else {
            CheckAndCreateAccount(semail, sconpass, sname,smobilr);
        }
    }

    private void CheckAndCreateAccount(String semail, String sconpass, String sname,String PhoneNumber) {
        progress_dialog.show();

        firebaseAuth.createUserWithEmailAndPassword(semail, sconpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                Database.storeUserData(semail, sname, currentuser.getUid(),PhoneNumber , new MycompleteListener() {
                    @Override
                    public void OnSuccess() {
                        progress_dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                        RegisterActivity.this.finish();
                        Toast.makeText(RegisterActivity.this, "Succefully Created Account", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnFailure() {
                        progress_dialog.dismiss();
                          Toast.makeText(RegisterActivity.this, "NotCreated Account", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress_dialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Please check Internet Connection\nand Try Agian", Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText(this, "Ready to Create Acount", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RegisterActivity.this.finish();
    }
}