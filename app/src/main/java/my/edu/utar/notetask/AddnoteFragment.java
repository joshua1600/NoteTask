package my.edu.utar.notetask;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class AddnoteFragment extends Fragment {

    private Button newNoteButton, newTaskButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_addnote, container, false);

        // Initialize the button
        newNoteButton = view.findViewById(R.id.nNoteButton);

        //Set up onClickListeners for the buttons
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "New Note button clicked", Toast.LENGTH_SHORT).show();


                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new NewNoteFragment());
                fragmentTransaction.addToBackStack(null); // Add this transaction to the back stack
                fragmentTransaction.commit();
            }
        });


        return view;
    }
}