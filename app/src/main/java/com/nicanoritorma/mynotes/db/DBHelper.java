package com.nicanoritorma.mynotes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nicanoritorma.mynotes.DataModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    public static final String NOTES_TABLE = "NOTES_TABLE";
    public static final String COLUMN_NOTES_TITLE = "NOTES_TITLE";
    public static final String COLUMN_NOTES_BODY = "NOTES_BODY";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TAG = "NOTE_TAG";

    public DBHelper(Context context) {
        super(context, "Notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NOTES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTES_TITLE + " TEXT, " + COLUMN_NOTES_BODY + " TEXT, " + COLUMN_TAG + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public boolean checkContents(String whereCondition1, String whereCondition2) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_TAG};
        String whereClause = COLUMN_TAG + " =? " + " OR " + COLUMN_TAG + " =? ";
        String[] whereArgs = {whereCondition1, whereCondition2};

        try {
            Cursor cursor = db.query(NOTES_TABLE, columns, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean checkContents(String whereCondition) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_TAG};
        String whereClause = COLUMN_TAG + " =? ";
        String[] whereArgs = {whereCondition};

        try {
            Cursor cursor = db.query(NOTES_TABLE, columns, whereClause, whereArgs, null, null, null);

            if (cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return false;
    }

    public void addOne(DataModel notesCreated) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTES_TITLE, notesCreated.getTitle());
        cv.put(COLUMN_NOTES_BODY, notesCreated.getNote());
        cv.put(COLUMN_TAG, notesCreated.getTag());

        db.insert(NOTES_TABLE, null, cv);
        db.close();
    }

    public ArrayList<DataModel> getAllNotes() {

        ArrayList<DataModel> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID, COLUMN_NOTES_TITLE, COLUMN_NOTES_BODY, COLUMN_TAG};
        String whereClause = COLUMN_TAG + " =? " + " OR " + COLUMN_TAG + " =? ";
        String[] whereArgs = {"pinned", "unpinned"};

        Cursor cursor = db.query(NOTES_TABLE, columns, whereClause, whereArgs, null, null, COLUMN_TAG + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String note = cursor.getString(2);
                String tag = cursor.getString(3);

                DataModel newData = new DataModel(id, title, note, tag);
                returnList.add(newData);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    //note will be deleted permanently
    public void deleteNote(DataModel notesCreated) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTES_TABLE, COLUMN_ID + "=?", new String[]{String.valueOf(notesCreated.getId())});
        db.close();
    }

    //note will be put in the trash section
    public void trashNote (DataModel dataModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TAG, dataModel.getTag());

        db.update(NOTES_TABLE, cv, COLUMN_ID + "=?", new String[]{String.valueOf(dataModel.getId())});
        db.close();
    }

    //get single note
    public DataModel getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(NOTES_TABLE, new String[]{COLUMN_ID, COLUMN_NOTES_TITLE, COLUMN_NOTES_BODY, COLUMN_ID}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        assert cursor != null;
        db.close();
        return new DataModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
    }

    public void editNote(DataModel notesCreated) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTES_TITLE, notesCreated.getTitle());
        cv.put(COLUMN_NOTES_BODY, notesCreated.getNote());
        cv.put(COLUMN_TAG, notesCreated.getTag());

        db.update(NOTES_TABLE, cv, COLUMN_ID + "=?", new String[]{String.valueOf(notesCreated.getId())});
        db.close();
    }


    public ArrayList<DataModel> getTrashedNotes() {
        ArrayList<DataModel> deletedList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID, COLUMN_NOTES_TITLE, COLUMN_NOTES_BODY, COLUMN_TAG};
        String whereClause = COLUMN_TAG + " =? ";
        String[] whereArgs = {"trashed"};

        Cursor cursor = db.query(NOTES_TABLE, columns, whereClause, whereArgs, null, null, COLUMN_TAG + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String note = cursor.getString(2);
                String tag = cursor.getString(3);

                DataModel newData = new DataModel(id, title, note, tag);
                deletedList.add(newData);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return deletedList;
    }
}
