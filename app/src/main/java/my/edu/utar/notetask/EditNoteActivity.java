package my.edu.utar.notetask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
                    editTextSubject.setHint("Edit Title");
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
                    editTextContent.setHint("Edit Content");
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            // Create an intent to navigate back to HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("openHomeFragment", true);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_move_to_bin) {
            moveToBin();
            return true;
        } else if (item.getItemId() == R.id.action_add_to_bookmark){
            toggleBookmark();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem bookmarkItem = menu.findItem(R.id.action_add_to_bookmark);
        // Update the icon based on the bookmark status
        boolean isBookmarked = getIntent().getBooleanExtra("noteBookmarked", false);
        bookmarkItem.setIcon(isBookmarked ? R.drawable.nav_bookmark : R.drawable.add_bookmark);
        return super.onPrepareOptionsMenu(menu);
    }

    private void toggleBookmark() {
        // Get the note ID from the intent
        String noteId = getIntent().getStringExtra("noteId");
        boolean isBookmarked = getIntent().getBooleanExtra("noteBookmarked", false);

        // Toggle the bookmark status
        isBookmarked = !isBookmarked;
        updateBookmarkStatus(noteId, isBookmarked);

        // Update the intent and menu icon
        getIntent().putExtra("noteBookmarked", isBookmarked);
        invalidateOptionsMenu();

        // Send a broadcast to notify fragments of the change
        Intent intent = new Intent("BOOKMARK_STATUS_CHANGED");
        intent.putExtra("noteId", noteId);
        intent.putExtra("isBookmarked", isBookmarked);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void updateBookmarkStatus(String noteId, boolean isBookmarked) {
        DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference("notes").child(noteId);
        noteRef.child("bookmarked").setValue(isBookmarked);
    }





    private void moveToBin() {
        // Get the note ID from the intent
        String noteId = getIntent().getStringExtra("noteId");

        // Move the note to the bin (you can implement this method to update your database)
        moveNoteToBin(noteId);

        // Finish the activity and return to the previous screen
        finish();
    }

    private void moveNoteToBin(String noteId) {
        // Get the current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Reference to the specific note in the user's notes
            DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("notes").child(noteId);
            // Update the 'deleted' flag to true
            noteRef.child("deleted").setValue(true);
        } else {
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show();
        }
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
