package my.edu.utar.notetask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ForgotPassword extends AppCompatActivity {

    private EditText forgotpassword;
    private Button passwordcoverbutton;
    private TextView gobacktologin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

       /* getSupportActionBar().hide();*/

        forgotpassword = findViewById(R.id.forgotPassword);
        passwordcoverbutton = findViewById(R.id.passwordRecoverButton);
        gobacktologin = findViewById(R.id.gotologin);
        firebaseAuth = FirebaseAuth.getInstance();

        gobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        passwordcoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = forgotpassword.getText().toString().trim();
                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(getApplicationContext(), "Enter your email first", Toast.LENGTH_SHORT).show();
                } else {
                    // Call Firebase method to send password reset email
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ForgotPassword.this, "Error sending reset email", Toast.LENGTH_SHORT).show();
                                showRetryDialog();

                            }
                        }
                    });
                }
            }
        });
    }


    private void showRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
        builder.setMessage("Error sending reset email. What would you like to do?")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Retry button, so do nothing and let the user retry
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Back to Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Back to Login button, so navigate to LoginActivity
                        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Close the ForgotPassword activity
                    }
                });
        // Create and show the dialog
        AlertDialog alert = builder.create();
        alert.show();
    }
}