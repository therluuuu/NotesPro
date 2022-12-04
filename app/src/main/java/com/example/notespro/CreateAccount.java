package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    EditText emailEdit, passwordEdit, confirmEdit;
    Button createAccount;
    ProgressBar progressBar;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account2);

        emailEdit = findViewById(R.id.email_edit_text);
        passwordEdit = findViewById(R.id.password_edit_text);
        confirmEdit = findViewById(R.id.confirm_password_edit_text);
        createAccount = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        login = findViewById(R.id.login_text_view_btn);

        createAccount.setOnClickListener(v -> createAccount());
        login.setOnClickListener(v -> finish());
    }

    void createAccount(){
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String confirm = confirmEdit.getText().toString();

        boolean isValidated = validateData(email, password, confirm);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(email, password);
    }

    void createAccountInFirebase(String email, String password){
    changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccount.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Utility.showToast(CreateAccount.this, "Account created successdfully, Check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else{
                            Utility.showToast(CreateAccount.this, task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccount.setVisibility(View.GONE);
        } else{
            progressBar.setVisibility(View.GONE);
            createAccount.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email, String password, String confirm){
        //validasi inputan
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError("Email Incalid");
            return false;
        }
        if(password.length()<6){
            passwordEdit.setError("Password lenght is Invalid");
            return false;
        }
        if(!password.equals(confirm)){
            confirmEdit.setError("Password not matched");
        }
        return true;
    }
}