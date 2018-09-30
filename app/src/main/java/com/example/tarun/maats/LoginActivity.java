package com.example.tarun.maats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText emailAddress,loginPassword;
    private TextView Info;
    private ImageButton loginButton;
    private int count = 5;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddress = (EditText) findViewById(R.id.email_address);
        loginPassword = (EditText) findViewById(R.id.login_password);
        Info = (TextView) findViewById(R.id.info_attempts);
        loginButton = (ImageButton) findViewById(R.id.login_btn);
        userRegistration = (TextView) findViewById(R.id.new_user);

        Info.setText("No. of attempts remaining : 5");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user!=null){
            finish();
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(emailAddress.getText().toString(),loginPassword.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,Registration.class));
            }
        });
    }

    private void validate(String userEmailAdd,String userPassword){
        progressDialog.setMessage("You can subscribe to my site until you are verified!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmailAdd,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    checkEmailVerification();
                }else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    count--;
                    progressDialog.dismiss();
                }
                Info.setText("No. of attempts remaining :"+ count);

                if(count==0){
                    loginButton.setEnabled(false);
                }
            }
        });

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean email_flag = firebaseUser.isEmailVerified();

        if (email_flag){
            finish();
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }else{
            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

}
