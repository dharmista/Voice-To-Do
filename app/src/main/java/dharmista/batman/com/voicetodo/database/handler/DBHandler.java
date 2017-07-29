package dharmista.batman.com.voicetodo.database.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import dharmista.batman.com.voicetodo.database.entity.VoiceData;
import dharmista.batman.com.voicetodo.util.DateUtil;

import static dharmista.batman.com.voicetodo.util.Constants.DUMMY;

/**
 * Created by Dharmista on 7/20/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 173;

    // Database Name
    private static final String DATABASE_NAME = "privateDB";

    public static final String TABLE_NAME = "VoiceMappersWithId";

    private static final String id = "id";
    private static final String fileName = "fileName";
    private static final String date = "date";
    private static final String month = "month";
    private static final String year = "year";
    private static final String hours = "hours";
    private static final String minutes = "minutes";
    private static final String seconds = "serviceId";
    private static final String setDate = "setDate";
    private static final String status = "isTaskCompleted";
    private static final String name = "name";
    private static final String message = "message";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table "+TABLE_NAME+" (" +
                "" + id + " TEXT primary key," +
                "" + fileName + " TEXT," +
                "" + date + " TEXT," +
                "" + month + " TEXT," +
                "" + year + " TEXT," +
                "" + hours + " TEXT," +
                "" + minutes + " TEXT," +
                "" + seconds + " TEXT," +
                "" + setDate + " TEXT," +
                "" + status + " NUMBER," +
                "" + name + " TEXT," +
                "" + message + " TEXT" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addToDb(VoiceData voiceData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(id, voiceData.getId());

        values.put(fileName, voiceData.getFileName());
        values.put(date, voiceData.getDate());
        values.put(month, voiceData.getMonth());
        values.put(year, voiceData.getYear());
        values.put(hours, voiceData.getHours());
        values.put(minutes, voiceData.getMinutes());
        values.put(seconds, voiceData.getServiceId());
        values.put(setDate, new DateUtil().getFormattedDate());
        values.put(status, voiceData.getIsTaskCompleted());
        values.put(message, voiceData.getMessage());
        values.put(name, voiceData.getName());

        db.insert(TABLE_NAME, null, values);
    }

    public List<VoiceData> getAllAlarms() {
        String query = "select * from "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        return extractCursorData(cursor);
    }

    public List<VoiceData> getAllPendingTodos() {
        String query = "select * from "+TABLE_NAME+" where "+status+"=0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        return extractCursorData(cursor);
    }

    private List<VoiceData> extractCursorData(Cursor cursor) {
        List<VoiceData> voiceDatas = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                VoiceData voiceData = new VoiceData()
                        .setId(cursor.getString(0))
                        .setFileName(cursor.getString(1))
                        .setDate(cursor.getString(2))
                        .setMonth(cursor.getString(3))
                        .setYear(cursor.getString(4))
                        .setHours(cursor.getString(5))
                        .setMinutes(cursor.getString(6))
                        .setServiceId(cursor.getString(7))
                        .setIsTaskCompleted(Integer.parseInt(cursor.getString(9)))
                        .setMessage(cursor.getString(11))
                        .setName(cursor.getString(10));
                voiceDatas.add(
                        voiceData
                );
            } while (cursor.moveToNext());
        }
        return voiceDatas;
    }

    public VoiceData getVoiceRecordById(String id) {
        List<VoiceData> voiceDatas = new ArrayList<>();
        String query = String.format("select * from "+TABLE_NAME+" where id='"+id+"'");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                VoiceData voiceData = new VoiceData()
                        .setId(cursor.getString(0))
                        .setFileName(cursor.getString(1))
                        .setDate(cursor.getString(2))
                        .setMonth(cursor.getString(3))
                        .setYear(cursor.getString(4))
                        .setHours(cursor.getString(5))
                        .setMinutes(cursor.getString(6))
                        .setServiceId(cursor.getString(7))
                        .setIsTaskCompleted(Integer.parseInt(cursor.getString(9)))
                        .setMessage(cursor.getString(11))
                        .setName(cursor.getString(10));
                voiceDatas.add(
                        voiceData
                );
            } while (cursor.moveToNext());
        }
        if(voiceDatas.size() == 0){
            return null;
        }
        return voiceDatas.get(0);
    }

    public void setTaskStatus(String id_) {
        SQLiteDatabase db = this.getWritableDatabase();
        VoiceData voiceData = this.getVoiceRecordById(id_);

        ContentValues contentValues = new ContentValues();

        contentValues.put(status, (voiceData.getIsTaskCompleted()+1)%2);

        db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id_});
    }

    public String deleteItem(String id) {
        VoiceData voiceData = this.getVoiceRecordById(id);
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,
                "id = ? ",
                new String[] {id });
        return voiceData.getFileName();
    }

    public void updateAlarmInstanceId(String id_, int value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(name, Integer.toString(value));

        db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id_});
    }

    public VoiceData snoozeTodo(String id_, int[] times){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(year, times[0]+"");
        contentValues.put(month, times[1]+"");
        contentValues.put(date, times[2]+"");
        contentValues.put(hours, times[3]+"");
        contentValues.put(minutes, times[4]+"");

        db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id_});
        return getVoiceRecordById(id_);
    }

    public List<String> getListOfFiles(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{id, fileName}, null, null, null, null, null);

        List<String> fileNames = new ArrayList<>();
        cursor.moveToFirst();
        do{
            if(!cursor.getString(0).equals(DUMMY)) {
                String filename = cursor.getString(1);
                String[] splitted = filename.split("/");
                fileNames.add(splitted[splitted.length - 1]);
            }
        }while (cursor.moveToNext());

        cursor.close();
        return fileNames;
    }
}
