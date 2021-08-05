package com.nicanoritorma.mynotes.fragmentAdapter;

import android.app.Activity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NVH> {

    private Activity activity;
    private ArrayList<DataModel> notesList;
    private final onNoteClickListener clickListener;
    private boolean isEnabled = false;
    private ViewModel viewModel;
    private List<DataModel> selectedList = new ArrayList<>();

    public NotesAdapter(Activity activity, ArrayList<DataModel> notesList, onNoteClickListener noteClickListener) {
        this.activity = activity;
        this.notesList = notesList;
        this.clickListener = noteClickListener;
    }

    //interface for click listener
    public interface onNoteClickListener {
        void onNoteClick(int position);
    }

    public static class NVH extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_note;
        private ImageView iv_pinned;

        public NVH(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_note = itemView.findViewById(R.id.tv_note);
            iv_pinned = itemView.findViewById(R.id.iv_pinnedNote);
        }
    }

    @NonNull
    @Override
    public NVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ViewModel.class);
        return new NVH(v);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.NVH holder, int position) {
        DataModel dataModel = notesList.get(position);
        holder.tv_title.setText(dataModel.getTitle());

        if (!dataModel.getNote().isEmpty()) {
            holder.tv_note.setVisibility(View.VISIBLE);
            holder.tv_note.setText(dataModel.getNote());
        }
        if (dataModel.getTag().equals("pinned")) {
            holder.iv_pinned.setVisibility(View.VISIBLE);
        }

        //long click listener
        holder.itemView.setOnLongClickListener(v -> {
            //check if item is already selected
            if (!isEnabled) {
                ActionMode.Callback callback = new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater menuInflater = mode.getMenuInflater();
                        menuInflater.inflate(R.menu.menu_long_click, menu);
                        menu.findItem(R.id.menu_actionPin).setVisible(true);
                        menu.findItem(R.id.menu_actionDelete).setVisible(true);

                        if (dataModel.getTag().equals("pinned")) {
                            menu.findItem(R.id.menu_actionPin).setIcon(R.drawable.ic_pin_enabled);
                        }
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

                        switch (id) {
                            case R.id.menu_actionPin:
                                for (DataModel data : selectedList) {
                                    if (data.getTag().equals("pinned")) {
                                        data.setTag("unpinned");
                                    } else {
                                        data.setTag("pinned");
                                    }
                                    dbHelper.editNote(data);
                                }
                                mode.finish();
                                break;
                            case R.id.menu_actionDelete:
                                for (DataModel data : selectedList) {
                                    if (data.getTag().equals("pinned")) {
                                        Toast.makeText(v.getContext(), "Pinned notes cannot be deleted", Toast.LENGTH_SHORT).show();
                                    } else {
                                        notesList.remove(data);
                                        data.setTag("trashed");
                                        dbHelper.trashNote(data);
                                    }
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
                ((AppCompatActivity) v.getContext()).startActionMode(callback);
            } else {
                longClickedItem(holder);
            }

            return true;
        });

        //click listener
        holder.itemView.setOnClickListener(v -> {
            if (isEnabled) {
                longClickedItem(holder);
            } else {
                clickListener.onNoteClick(holder.getBindingAdapterPosition());
            }
        });
    }

    private void longClickedItem(RecyclerView.ViewHolder holder) {
        //get selected item value
        DataModel data = notesList.get(holder.getBindingAdapterPosition());

        if (!selectedList.contains(data)) {
            holder.itemView.setBackgroundResource(R.drawable.item_long_clicked);
            selectedList.add(data);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.transparent_border);
            selectedList.remove(data);
        }

        //set text on view model
        if (selectedList.size() == 0) {
            viewModel.setText("");
        } else {
            viewModel.setText(String.valueOf(selectedList.size()));
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
