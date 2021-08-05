package com.nicanoritorma.mynotes.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nicanoritorma.mynotes.DataModel;
import com.nicanoritorma.mynotes.R;
import com.nicanoritorma.mynotes.db.DBHelper;
import com.nicanoritorma.mynotes.fragmentAdapter.TrashedAdapter;

import java.util.ArrayList;

public class TrashedNotes extends Fragment {

    private RecyclerView rv_trashed;
    private DBHelper dbHelper;
    private ArrayList<DataModel> deletedNotes;
    private RecyclerView.LayoutManager layoutManager;
    private TrashedAdapter adapter;
    private TextView tv_trashed;

    public TrashedNotes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trashed_notes, container, false);
        rv_trashed = view.findViewById(R.id.trashed_notes_list);
        tv_trashed = view.findViewById(R.id.tv_trashed_empty);
        initUI();
        return view;
    }

    private void initUI()
    {
        dbHelper = new DBHelper(getContext());
        if (dbHelper.checkContents("trashed"))
        {
            deletedNotes = dbHelper.getTrashedNotes();
            adapter = new TrashedAdapter(getActivity(), deletedNotes);
            rv_trashed.setHasFixedSize(true);
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rv_trashed.setLayoutManager(layoutManager);
            rv_trashed.setAdapter(adapter);
        }
        else {
            tv_trashed.setVisibility(View.VISIBLE);
        }
    }
}