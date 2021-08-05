package com.nicanoritorma.mynotes.fragmentAdapter;

import android.app.Activity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.nicanoritorma.mynotes.DataModel;
import com.nicanoritorma.mynotes.R;
import com.nicanoritorma.mynotes.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class TrashedAdapter extends RecyclerView.Adapter<TrashedAdapter.TVH> {

    private Activity activity;
    private ArrayList<DataModel> deletedNotes;
    private boolean isEnabled = false;
    private ViewModel viewModel;
    private List<DataModel> selectedList = new ArrayList<>();

    public TrashedAdapter(Activity activity, ArrayList<DataModel> deletedNotes) {
        this.activity = activity;
        this.deletedNotes = deletedNotes;
    }

    public static class TVH extends RecyclerView.ViewHolder
    {
        private TextView title, note;

        public TVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            note = itemView.findViewById(R.id.tv_note);
        }
    }

    @NonNull
    @Override
    public TVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ViewModel.class);
        return new TrashedAdapter.TVH(v);
    }

    @Override
    public void onBindViewHolder(TrashedAdapter.TVH holder, int position) {
        DataModel data = deletedNotes.get(position);
        holder.title.setText(data.getTitle());
        holder.note.setText(data.getNote());

        if (!data.getNote().isEmpty()) {
            holder.note.setVisibility(View.VISIBLE);
            holder.note.setText(data.getNote());
        }

        holder.itemView.setOnClickListener(v -> {
            if (isEnabled) {
                longClickedItem(holder);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {

            if (!isEnabled)
            {
                ActionMode.Callback callback = new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater menuInflater = mode.getMenuInflater();
                        menuInflater.inflate(R.menu.menu_long_click, menu);
                        menu.findItem(R.id.menu_trashRestore).setVisible(true);
                        menu.findItem(R.id.menu_actionDelete).setVisible(true);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        isEnabled = true;
                        longClickedItem(holder);

                        viewModel.getText().observe((LifecycleOwner) activity, mode::setTitle);
                        return true;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        int id = item.getItemId();
                        DBHelper dbHelper = new DBHelper(v.getContext());

                        switch (id)
                        {
                            case R.id.menu_trashRestore:
                                for (DataModel data1 : selectedList)
                                {
                                    data1.setTag("unpinned");
                                    dbHelper.editNote(data1);
                                }
                                mode.finish();
                                break;
                            case R.id.menu_actionDelete:
                                for (DataModel data1 : selectedList)
                                {
                                    deletedNotes.remove(data1);
                                    dbHelper.deleteNote(data1);
                                }
                                mode.finish();
                                break;
                        }
                        activity.onBackPressed();
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        isEnabled = false;
                        selectedList.clear();
                        activity.onBackPressed();
                    }
                };
                ((AppCompatActivity)v.getContext()).startActionMode(callback);
            }
            else
            {
                longClickedItem(holder);
            }

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return deletedNotes.size();
    }

    private void longClickedItem(RecyclerView.ViewHolder holder) {
        //get selected item value
        DataModel data = deletedNotes.get(holder.getBindingAdapterPosition());

        if (!selectedList.contains(data)) {
            holder.itemView.setBackgroundResource(R.drawable.item_long_clicked);
            selectedList.add(data);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.transparent_border);
            selectedList.remove(data);
        }

        //set text on view model
        if (selectedList.size() == 0)
        {
            viewModel.setText("");
        }
        else
        {
            viewModel.setText(String.valueOf(selectedList.size()));
        }
    }
}
