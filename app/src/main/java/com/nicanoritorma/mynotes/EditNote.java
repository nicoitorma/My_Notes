package com.nicanoritorma.mynotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.nicanoritorma.mynotes.db.DBHelper;

public class EditNote extends AppCompatActivity {

    private EditText et_editTitle, et_editNote;
    private DBHelper dbHelper;
    private DataModel dataModel;
    private Toolbar toolbar;
    private ActionBar ab;
    private int id = 0;
    private String note_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        et_editTitle = findViewById(R.id.et_editTitle);
        et_editNote = findViewById(R.id.et_editNote);
        toolbar = findViewById(R.id.editNote_tb);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        dbHelper = new DBHelper(getApplicationContext());
        id = getIntent().getIntExtra("ID", 0);
        note_tag = getIntent().getStringExtra("TAG");
        initUI(id);
    }

    private void initUI(int note_id)
    {
        ab.setTitle("Edit Note");
        dataModel = dbHelper.getNote(note_id);

        //check if there is data
        if (!dataModel.getTitle().isEmpty() || !dataModel.getNote().isEmpty())
        {
            et_editTitle.setText(dataModel.getTitle());
            et_editNote.setText(dataModel.getNote());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_tb, menu);
        if (note_tag.equals("pinned"))
        {
            menu.findItem(R.id.menu_pinNote).setIcon(R.drawable.ic_pin_enabled);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_pinNote:
                if (note_tag.equals("pinned"))
                {
                    getNoteData("unpinned");
                }
                else
                {
                    pinNote();
                }
                return true;
            case R.id.menu_saveNote:
                getNoteData("unpinned");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pinNote()
    {
        String tag = "pinned";
        getNoteData(tag);
    }

    private void getNoteData(String tag)
    {
        String title = et_editTitle.getText().toString().trim();
        String note = et_editNote.getText().toString().trim();
        verifyChanges(title, note, tag);
    }

    private void getNoteData ()
    {
        String title = et_editTitle.getText().toString().trim();
        String note = et_editNote.getText().toString().trim();
        verifyChanges(title, note, note_tag);
    }

    private void verifyChanges(String title, String note, String tag)
    {
        //check if there is changes
        if (title.isEmpty() || note.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Empty notes cannot be saved", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (!title.equals(dataModel.getTitle()) || !note.equals(dataModel.getNote()))
        {
            alertDialog(title, note, tag);
        }
        else if (!tag.equals(note_tag))
        {
            alertDialog(title, note, tag);
        }
        else
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void alertDialog(String title, String note, String tag)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(EditNote.this);
        alert.setMessage("Save edited notes?").
                setPositiveButton("Yes", (dialogInterface, i) -> {
                    saveChanges(title, note, tag);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }).setNegativeButton("No", (dialogInterface, i) -> finish());
        AlertDialog builder = alert.create();
        builder.show();
    }

    private void saveChanges(String title, String note, String tag)
    {
        dataModel.setTitle(title);
        dataModel.setNote(note);
        dataModel.setTag(tag);
        dbHelper.editNote(dataModel);
    }

    @Override
    public void onBackPressed() {
        getNoteData();
    }
}