package my.edu.utar.notetask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BookmarkFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotesListAdapter noteAdapter;
    private List<Note> notes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noteAdapter = new NotesListAdapter(notes, getContext());
        recyclerView.setAdapter(noteAdapter);


        // Add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_item_spacing);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

        // Listen for changes in the Firebase database
        DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference("notes");

        retrieveBookmarkedNotes();

        return view;
    }

    private void retrieveBookmarkedNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("notes");

            notesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    notes.clear();

                    for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                        Note note = noteSnapshot.getValue(Note.class);
                        if (note != null && note.isBookmarked()) {
                            note.setId(noteSnapshot.getKey());
                            notes.add(note);
                        }
                    }
                    noteAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load notes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
