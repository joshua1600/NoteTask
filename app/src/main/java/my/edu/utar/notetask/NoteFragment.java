package my.edu.utar.notetask;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

public class NoteFragment extends Fragment {

    private static final String ARG_NOTE = "note";
    private HomeFragment.Note note; // Use the inner class Note

    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance(HomeFragment.Note note) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE, note); // Pass the note object
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = (HomeFragment.Note) getArguments().getSerializable(ARG_NOTE); // Retrieve the note object
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        TextView titleTextView = view.findViewById(R.id.noteTitleTextView);
        TextView contentTextView = view.findViewById(R.id.noteContentTextView);

        if (note != null) {
            titleTextView.setText(note.getSubject());
            contentTextView.setText(note.getContent());
        }

        return view;
    }
}