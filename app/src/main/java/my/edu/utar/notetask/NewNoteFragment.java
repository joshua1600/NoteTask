package my.edu.utar.notetask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewNoteFragment extends Fragment {
    private Button saveNoteBtn;
    private TextInputEditText subjectEditText, contentEditText;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newnote, container, false);

        // Initialize views
        saveNoteBtn = view.findViewById(R.id.saveNoteBtn);
        subjectEditText = view.findViewById(R.id.subjectEditText);
        contentEditText = view.findViewById(R.id.contentEditText);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Set onclick listener for save note button
        saveNoteBtn.setOnClickListener(v -> saveNote());

        return view;
    }

    private void saveNote() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String subject = subjectEditText.getText().toString();
            String content = contentEditText.getText().toString();

            // Save data to Firebase Realtime Database
            DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("notes");
            String noteId = notesRef.push().getKey(); // Generate unique ID for the note
            Note note = new Note(subject, content);

            notesRef.child(noteId).setValue(note)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Note saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to save note", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No user is logged in", Toast.LENGTH_SHORT).show();
        }
    }

    // Note class to match the structure in Firebase
    public static class Note {
        private String subject;
        private String content;

        public Note() {
        } // Default constructor required for calls to DataSnapshot.getValue(Note.class)

        public Note(String subject, String content) {
            this.subject = subject;
            this.content = content;
        }

        public String getSubject() {
            return subject;
        }

        public String getContent() {
            return content;
        }
    }
}