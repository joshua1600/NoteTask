package my.edu.utar.notetask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText newPassword, confirmPassword;
    private Button resetPasswordButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        firebaseAuth = FirebaseAuth.getInstance();

        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String newPass = newPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmPass)) {
                    Toast.makeText(getApplicationContext(), "Please enter both password fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!newPass.equals(confirmPass)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    confirmPassword.setText("");  // Clear the confirm password field
                    return;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Passwords is match", Toast.LENGTH_SHORT).show();


                }
// Assuming user is already authenticated
                firebaseAuth.getCurrentUser().updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPassword.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ResetPassword.this, "Password update failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}