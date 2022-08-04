package tuan.aprotrain.projectpetcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.User;


public class RegisterActivity extends AppCompatActivity {

    private TextView alreadyHaveAccount;
    private EditText inputName, inputEmail, inputPhone, inputPass, inputRepass;
    private Button btnRegister;
    private User user;
    DatabaseReference referenceUsers;
    //long userId = 0;
    private Boolean isUpdating = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveAccount = findViewById(R.id.textLogin);

        inputEmail = findViewById(R.id.user_email);
        inputPass = findViewById(R.id.pass);
        inputRepass = findViewById(R.id.re_password);

        btnRegister = findViewById(R.id.loginBtn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUpdating = true;
                String uEmail = inputEmail.getText().toString().trim();
                String uPass = inputPass.getText().toString().trim();
                String confirm = inputRepass.getText().toString().trim();

                if (validateInput(uEmail, uPass, confirm) == false)
                    Toast.makeText(RegisterActivity.this, "Wrong Validation", Toast.LENGTH_LONG).show();
                else
                    register( uEmail, uPass);
            }
        });

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private boolean validateInput( String email, String password, String confirmpass) {//
        int t = 0;
        if (email.isEmpty()) {
            inputEmail.setError("Required");
            inputEmail.requestFocus();
            t++;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Invalid email form !");
            t++;
        }

        if (password.isEmpty()) {
            inputPass.setError("Required");
            inputPass.requestFocus();
            t++;
        }
        if (confirmpass.isEmpty()) {
            inputRepass.setError("Required");
            inputRepass.requestFocus();
            t++;
        }
        if (!password.equals(confirmpass)){
            inputRepass.setError("The password confirmation does not match.");
            t++;
        }
        if (t>0){
            isUpdating = false;
            return false;
        }else {
            return true;
        }
    }


    public void register(String email,String pass){
        referenceUsers = FirebaseDatabase.getInstance().getReference().child(User.TABLE_NAME);

        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user = new User(FirebaseAuth.getInstance().getUid(),email.toLowerCase());
                    referenceUsers.child(FirebaseAuth.getInstance().getUid()).setValue(user);
                    Toast.makeText(RegisterActivity.this,"User registered successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }else {
                    Toast.makeText(RegisterActivity.this, "Register error: "+ task.getException().getMessage()  , Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}