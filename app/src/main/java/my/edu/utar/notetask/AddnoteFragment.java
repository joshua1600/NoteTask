package my.edu.utar.notetask;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import java.util.stream.Collectors;


public class AddnoteFragment extends Fragment {

    private List<Note> notes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_addnote, container, false);

        // Sample notes
        notes.add(new Note("Shopping List", "Buy milk, eggs, and bread.", "To-Do List"));
        notes.add(new Note("Meeting Notes", "Discuss project milestones.", "Memo"));

        // Implement other functionalities as needed...

        return view;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public List<Note> searchByName(String name) {
        return notes.stream()
                .filter(note -> note.getTitle().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<Note> sortByFileSize() {
        return notes.stream()
                .sorted(Comparator.comparingLong(Note::getFileSize))
                .collect(Collectors.toList());
    }

    public List<Note> sortByTimeline() {
        return notes.stream()
                .sorted(Comparator.comparing(Note::getCreatedDate))
                .collect(Collectors.toList());
    }

    public List<Note> sortAlphabetically() {
        return notes.stream()
                .sorted(Comparator.comparing(Note::getTitle))
                .collect(Collectors.toList());
    }

    public List<Note> searchByKeywordAndDate(String keyword, Date targetDate) {
        // Set start and end of the day to match Date objects
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(targetDate);
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);
        Date startOfDay = calStart.getTime();

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(targetDate);
        calEnd.set(Calendar.HOUR_OF_DAY, 23);
        calEnd.set(Calendar.MINUTE, 59);
        calEnd.set(Calendar.SECOND, 59);
        calEnd.set(Calendar.MILLISECOND, 999);
        Date endOfDay = calEnd.getTime();

        return notes.stream()
                .filter(note -> note.getContent().contains(keyword) &&
                        !note.getCreatedDate().before(startOfDay) &&
                        !note.getCreatedDate().after(endOfDay))
                .collect(Collectors.toList());
    }


    public Map<String, List<Note>> createSectionGroup() {
        return notes.stream()
                .collect(Collectors.groupingBy(Note::getCategory));
    }

    public List<Note> searchByContent(String content) {
        return notes.stream()
                .filter(note -> note.getContent().contains(content))
                .collect(Collectors.toList());
    }
}