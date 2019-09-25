package in.wangziq.fitnessrecorder.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import in.wangziq.fitnessrecorder.Models.MyModel;

public class DBhelper extends SQLiteOpenHelper implements DBInterface {

    private static final String TABLE_NAME = "MYTABLE";
    private static final String _ID = "_ID";
    private static final String TIME = "TIME";
    private static final String DISTANSE = "DISTANSE";
    public DBhelper(Context context) {
        super(context, "myBas", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY,"
                + TIME + " TEXT,"
                + DISTANSE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "MYTABLE");
        onCreate(db);
    }

    @Override
    public void addNewItem(MyModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME, model.getTime());
        contentValues.put(DISTANSE, model.getDistanse());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    @Override
    public MyModel getNewsItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_NAME, new String[] {
                _ID,
                TIME,
                DISTANSE
        }, _ID + "=?",
                new String[] {
                        String.valueOf(id)
                }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        assert cursor != null;
        return new MyModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2));
    }

    @Override
    public ArrayList<MyModel> getAllNewsItems() {
        ArrayList<MyModel> ar = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("MYTABLE", null, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            int timeColIndex = cursor.getColumnIndex("TIME");
            int distColIndex = cursor.getColumnIndex("DISTANSE");

            do {
                MyModel model = new MyModel();
                model.setTime(cursor.getString(timeColIndex));
                model.setDistanse(cursor.getString(distColIndex));
                ar.add(model);
                Log.i("SAVEDB", "TIME" + cursor.getString(timeColIndex)
                        + " DIST" + cursor.getString(distColIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("aaaa", "0 r");
        cursor.close();
        db.close();
        return ar;
    }

    @Override
    public int getNewsItemsCount() {
        String counQuary = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(counQuary, null);
        cursor.close();
        return cursor.getCount();
    }

    @Override
    public int updateNewsItem(MyModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIME, model.getTime());
        values.put(DISTANSE, model.getDistanse());

        return db.update(TABLE_NAME, values, _ID + " = ?",
                new String[] { String.valueOf(model.getId()) });
    }

}
