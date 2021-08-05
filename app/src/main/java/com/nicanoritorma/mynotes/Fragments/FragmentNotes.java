package com.nicanoritorma.mynotes.Fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicanoritorma.mynotes.DataModel;
import com.nicanoritorma.mynotes.EditNote;
import com.nicanoritorma.mynotes.R;
import com.nicanoritorma.mynotes.db.DBHelper;
import com.nicanoritorma.mynotes.fragmentAdapter.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentNotes extends Fragment implements NotesAdapter.onNoteClickListener {

    private RecyclerView rv_notes;
    private DBHelper dbHelper;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataModel> notesList;
    private NotesAdapter adapter;
    private TextView tv_empty;

    public FragmentNotes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        rv_notes = view.findViewById(R.id.notes_list);
        tv_empty = view.findViewById(R.id.tv_empty);
        initUI();
        return view;
    }

    private void initUI()
    {
        dbHelper = new DBHelper(getContext());
        if (dbHelper.checkContents("pinned", "unpinned"))
        {
            notesList = dbHelper.getAllNotes();
            adapter = new NotesAdapter(getActivity(), notesList, this);
            rv_notes.setHasFixedSize(true);
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rv_notes.setLayoutManager(layoutManager);

            rv_notes.setAdapter(adapter);
        }
        else
        {
            tv_empty.setVisibility(View.VISIBLE);
        }
    }

    //method for click listener
    @Override
    public void onNoteClick(int position) {
        DataModel dataModel = notesList.get(position);
        Intent intent = new Intent(getContext(), EditNote.class);
        intent.putExtra("ID", dataModel.getId());
        intent.putExtra("TAG", dataModel.getTag());
        startActivity(intent);
    }


}