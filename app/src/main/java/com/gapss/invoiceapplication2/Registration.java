package com.gapss.invoiceapplication2;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Registration extends AppCompatActivity implements View.OnClickListener{
    private TextView banner, registerUser;
    private FirebaseAuth mAuth;
    private EditText et_FullName, et_Age, et_Email, et_Pass;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView)findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.reg_button);
        registerUser.setOnClickListener(this);

        et_FullName = (EditText)findViewById(R.id.et_name_reg);
        et_Age = (EditText)findViewById(R.id.et_age_reg);
        et_Email = (EditText)findViewById(R.id.et_email_reg);
        et_Pass = (EditText)findViewById(R.id.et_pass_reg);

        progressBar = (ProgressBar)findViewById(R.id.progressBar_reg);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.reg_button:
                registerUser();
                break;
        }
    }

    private void registerUser(){
        String email = et_Email.getText().toString();
        String password = et_Pass.getText().toString();
        String full_name = et_FullName.getText().toString();
        String age = et_Age.getText().toString();

        if(full_name.isEmpty()){
            et_FullName.setError("Full name is required!");
            et_FullName.requestFocus();
            return;
        }

        if(age.isEmpty()){
            et_Age.setError("Age is required!");
            et_Age.requestFocus();
            return;
        }

        if(email.isEmpty()){
            et_Email.setError("Email is required!");
            et_Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_Email.setError("Please provide valid email!");
            et_Email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            et_Pass.setError("Password is required!");
            et_Pass.requestFocus();
            return;
        }

        if(password.length() < 6){
            et_Pass.setError("Min password length should be 6 characters!");
            et_Pass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(full_name,age,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Registration.this,"User has been registred successfully!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else{
                                        Toast.makeText(Registration.this,"Registration is failed!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(Registration.this,"Registration is failed!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}