package my.edu.utar.notetask;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {

    private ListView notesListView;
    private ArrayAdapter<Note> notesAdapter;
    private List<Note> notes; // List to hold Note objects

    private Set<String> existingSubjects = new HashSet<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        notesListView = view.findViewById(R.id.notesListView);
        notes = new ArrayList<>();

        // Set up the adapter to display note subjects using the custom layout
        notesAdapter = new ArrayAdapter<Note>(getContext(), R.layout.list_item, notes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                }

                Note note = notes.get(position);
                TextView subjectTextView = convertView.findViewById(R.id.noteSubjectTextView);
                subjectTextView.setText(note.getSubject());

                return convertView;
            }
        };
        notesListView.setAdapter(notesAdapter);

        // Set up the click listener for the ListView
        notesListView.setOnItemClickListener((parent, view1, position, id) -> {
            Note selectedNote = notes.get(position); // Get the selected note
            NoteFragment noteFragment = NoteFragment.newInstance(selectedNote); // Create the NoteFragment instance

            // Replace the current fragment with NoteFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, noteFragment) // Use the correct ID for the fragment container
                    .addToBackStack(null) // Optional: to add to back stack
                    .commit();
        });

        retrieveNotes(); // Call method to retrieve notes from Firebase
        return view;
    }

    private void retrieveNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("notes");

            notesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    notes.clear(); // Clear existing notes

                    for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                        Note note = noteSnapshot.getValue(Note.class);
                        if (note != null) {
                            // Check if the note subject already exists in the set
                            if (!existingSubjects.contains(note.getSubject())) {
                                existingSubjects.add(note.getSubject()); // Add to the set
                                notes.add(note); // Add the Note object to the notes list
                            }
                        }
                    }
                    notesAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load notes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No user is logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    // Note class to match the structure in Firebase
    public static class Note implements Serializable {
        private String subject;
        private String content;
        private String imageUrl;

        public Note() {
        } // Default constructor required for calls to DataSnapshot.getValue(Note.class)

        public Note(String subject, String content) {
            this.subject = subject;
            this.content = content;
            this.imageUrl = imageUrl;
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