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
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //Log In class
    private TextView register, forgotPass;
    private EditText et_Email_log, et_Pass_log;
    private Button btn_Sign;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }

        register = (TextView) findViewById(R.id.tv_reg);
        register.setOnClickListener(this);

        forgotPass = (TextView) findViewById(R.id.tv_fp);
        forgotPass.setOnClickListener(this);

        btn_Sign = (Button)findViewById(R.id.btn_sign);
        btn_Sign.setOnClickListener(this);

        et_Email_log = (EditText) findViewById(R.id.et_email);
        et_Pass_log = (EditText) findViewById(R.id.et_pass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_reg:
                startActivity(new Intent(this, Registration.class));
                break;
            case R.id.tv_fp:
                startActivity(new Intent(this, ResetPassActivity.class));
                break;
            case R.id.btn_sign:
                userLogin();
                break;

        }
    }

    private void userLogin(){
        String email = et_Email_log.getText().toString();
        String password = et_Pass_log.getText().toString();

        if(email.isEmpty()){
            et_Email_log.setError("Email is required!");
            et_Email_log.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_Email_log.setError("Please enter a valid Email");
            et_Email_log.requestFocus();
            return;
        }


        if(password.isEmpty()){
            et_Pass_log.setError("Password is required!");
            et_Pass_log.requestFocus();
            return;
        }

        if(password.length() < 6){
            et_Pass_log.setError("Wrong length of password!");
            et_Pass_log.requestFocus();
            return;
        }

        progressBar.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, InvoiceActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your Email to verify your account", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your creditionals", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}