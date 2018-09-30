import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarun.maats.LoginActivity;
import com.example.tarun.maats.R;

public class Registration extends AppCompatActivity {

    private EditText userName,userPassword,userEmailId;
    private ImageButton register_btn;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    //Uploads data to the database

                    String user_email = userEmailId.getText().toString().trim();
                    String user_emailPassword = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_emailPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendEmailVerification();
                            }else{
                                Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this,LoginActivity.class));
            }
        });
    }
    private void setupUIViews(){
        userName = (EditText) findViewById(R.id.reg_username);
        userPassword = (EditText) findViewById(R.id.reg_password);
        userEmailId = (EditText) findViewById(R.id.reg_email);
        register_btn= (ImageButton) findViewById(R.id.reg_btn);
        userLogin = (TextView) findViewById(R.id.goto_login);
    }

    private Boolean validate(){

        Boolean result = false;

        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmailId.getText().toString();

        if(name.isEmpty()||password.isEmpty()||email.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;
    }

    private void sendEmailVerification(){

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Registration.this, "Successfully Registered,Verification mail sent", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Registration.this,LoginActivity.class));
                    }else{
                        Toast.makeText(Registration.this, "Verification mail not sent , Try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
