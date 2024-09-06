package my.edu.utar.notetask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewNoteFragment extends Fragment {
    private Button saveNoteBtn;
    private TextInputEditText subjectEditText, contentEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newnote, container, false);

        // Initialize views using the 'view' object
        saveNoteBtn = view.findViewById(R.id.saveNoteBtn);
        subjectEditText = view.findViewById(R.id.subjectEditText);
        contentEditText = view.findViewById(R.id.contentEditText);


        // Set onClickListener for save note button
        saveNoteBtn.setOnClickListener(v -> {
            String subject = subjectEditText.getText().toString();
            String content = contentEditText.getText().toString();

            // Save note with subject and content only
            saveNote(subject, content);

            // Get the activity and cast it to HomeActivity
            HomeActivity activity = (HomeActivity) getActivity();
            if (activity != null) {
                // Call the openFragment method
                activity.openFragment(new HomeFragment());
            }
        });

        return view;
    }

    private void saveNote(String subject, String content) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Create a new note object
            Note note = new Note(subject, content, null); // No image URL needed

            // Save data to Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference notesRef = database.getReference("users").child(userId).child("notes");

            // Add note to database (include show feedback)
            notesRef.push().setValue(note, (error, ref) -> {
                if (error != null) {
                    // If save file fails, show error message
                    Toast.makeText(getActivity(), "Failed to save note: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // If save successful, show successful message
                    Toast.makeText(getActivity(), "Note saved successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "User not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

    // Note class model for Firebase Database
    class Note {
        private String subject;
        private String content;
        private String imageUrl; // Keep this for future use, but set to null

        public Note(String subject, String content, String imageUrl) {
            this.subject = subject;
            this.content = content;
            this.imageUrl = imageUrl; // Can be null
        }

        public String getSubject() {
            return subject;
        }

        public String getContent() {
            return content;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}