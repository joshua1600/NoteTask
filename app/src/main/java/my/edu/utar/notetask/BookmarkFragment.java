package my.edu.utar.notetask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


        return view;
    }

}
