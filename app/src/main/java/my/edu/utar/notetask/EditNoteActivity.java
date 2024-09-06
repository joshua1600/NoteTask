package my.edu.utar.notetask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditNoteActivity extends AppCompatActivity {

    private EditText editTextSubject, editTextContent;
    private Button buttonSave;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editTextSubject = findViewById(R.id.editTextSubject);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);

        Intent intent = getIntent();
        noteId = intent.getStringExtra("noteId");
        String noteSubject = intent.getStringExtra("noteSubject");
        String noteContent = intent.getStringExtra("noteContent");

        editTextSubject.setText(noteSubject);
        editTextContent.setText(noteContent);

        buttonSave.setOnClickListener(v -> saveNoteChanges());

        editTextSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    editTextSubject.setHint("");
                } else {
                    editTextSubject.setHint("Title");
                }
            }
        });

        editTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    editTextContent.setHint("");
                } else {
                    editTextContent.setHint("Write something");
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_home); // Ensure you have a home icon in your drawable resources
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Create an intent to navigate back to HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("openHomeFragment", true);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void saveNoteChanges() {
        String updatedSubject = editTextSubject.getText().toString();
        String updatedContent = editTextContent.getText().toString();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("notes").child(noteId);

            noteRef.child("subject").setValue(updatedSubject);
            noteRef.child("content").setValue(updatedContent).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditNoteActivity.this, "Note updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditNoteActivity.this, "Failed to update note", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
