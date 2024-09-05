package my.edu.utar.notetask;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class NewNoteFragment extends Fragment {
    Button saveNoteBtn;
    TextInputEditText subjectEditText,contentEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newnote, container, false);

        // Initialize views using the 'view' object
        saveNoteBtn = view.findViewById(R.id.saveNoteBtn);
        subjectEditText = view.findViewById(R.id.subjectEditText);
        contentEditText = view.findViewById(R.id.contentEditText);

        //Set onclicklistener for save note button
        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        return view;

    }

    private void saveNote() {
        // Get text from EditTexts
        String subject = subjectEditText.getText().toString();
        String content = contentEditText.getText().toString();

        // Save data to Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference notesRef = database.getReference("notes");

        // Create a new note object
        Note note = new Note(subject, content);

        // Add note to database
        notesRef.push().setValue(note);
    }


    class Note {
        private String subject;
        private String content;

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
