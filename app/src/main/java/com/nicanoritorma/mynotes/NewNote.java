package com.nicanoritorma.mynotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.nicanoritorma.mynotes.db.DBHelper;

public class NewNote extends AppCompatActivity {

    private EditText et_title, et_note;
    private ActionBar ab;
    private DataModel new_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Toolbar toolbar = findViewById(R.id.newNote_tb);
        et_title = findViewById(R.id.note_title);
        et_note = findViewById(R.id.et_note);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        initUI();
    }

    private void initUI()
    {
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("New Note");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_tb, menu);
        menu.findItem(R.id.menu_saveNote).setVisible(true);
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
                pinNote();
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
        String title = et_title.getText().toString().trim();
        String note = et_note.getText().toString().trim();
        checkNoteData(title, note, tag);
    }

    private void checkNoteData(String title, String note, String tag)
    {
        //check if title or note is empty
        if (!title.isEmpty() || !note.isEmpty())
        {
            saveNote(title, note, tag);
        }
        else {
            Toast.makeText(getApplicationContext(), "Empty notes cannot be saved",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //function to save the note in database
    private void saveNote(String title, String note, String tag)
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        new_note = new DataModel(1, title, note, tag);
        dbHelper.addOne(new_note);
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        getNoteData("unpinned");
    }
}