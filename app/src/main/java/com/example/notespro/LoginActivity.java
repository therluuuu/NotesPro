package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEdit, passwordEdit;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = findViewById(R.id.email_edit_text);
        passwordEdit = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountTextViewBtn = findViewById(R.id.create_account_text_view_btn);

        loginBtn.setOnClickListener((v) -> loginUser());
        createAccountTextViewBtn.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccount.class)));
    }

    void loginUser(){
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        boolean isValidated = validateData(email, password);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email, password);
    }

    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else {
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email, String password){
        //validasi inputan
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError("Email Incalid");
            return false;
        }
        if(password.length()<6){
            passwordEdit.setError("Password lenght is Invalid");
            return false;
        }

        return true;
    }
}