package andoop.android.amstory.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import andoop.android.amstory.module.Story;

/**
 * Created by Administrator on 2017/5/11/011.
 */

public class PlayRecordDao {

    private static final String TABLE_NAME = DBHelper.TABLE_PLLAYRECORD;
    private static final String TITLE = "title";
    private static final String VID = "id";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String IMG = "img";
    private static final String VOICE = "voice";
    private static final String[] FROM = { VID, TITLE, AUTHOR, CONTENT, IMG, VOICE};
    private DBHelper dbHelper;

    public PlayRecordDao(Context context){

        dbHelper = new DBHelper(context);
    }

    //添加播放记录数据
    public boolean addPlayRecord(Story playrecord) {

        boolean result = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VID, playrecord.id);
        values.put(TITLE, playrecord.title);
        values.put(AUTHOR, playrecord.author);
        values.put(CONTENT, playrecord.content);
        values.put(IMG, playrecord.img);
        values.put(VOICE, playrecord.voice);
        if (hasAlert(db, playrecord.id)) {
            delAlert(db, playrecord.id);
        }
        try {
            // 调用insert方法，就可以将数据插入到数据库当中
            // 第一个参数:表名称
            // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
            // 第三个参数：ContentValues对象
            db.insertOrThrow(TABLE_NAME, null, values);
            hasTenRecords(db);
        } catch (Exception e) {
            // TODO: handle exception
        }

        result = true;
        db.close();//关闭所有打开的数据库对象
        return result;
    }
    private boolean hasAlert(SQLiteDatabase db, String vid) {
        String selection = VID + "='" + vid + "'";
        Cursor cursor = db.query(TABLE_NAME, FROM, selection, null, null, null,null);
        boolean hasCht = false;
        if (cursor != null && cursor.getCount() > 0) {
            hasCht = true;
        }
        cursor.close();
        return hasCht;
    }
    public void delAlert(SQLiteDatabase db, String vid) {
        String whereClause = VID + "='" + vid + "'";
        db.delete(TABLE_NAME, whereClause, null);
    }


    private void hasTenRecords(SQLiteDatabase db) {

        Cursor cursor = db
                .query(TABLE_NAME, FROM, null, null, null, null, null);
        if (cursor.getCount() > 100) {

            cursor.moveToFirst();
            if (cursor.isFirst()) {
                String vid = cursor.getString(cursor.getColumnIndex(VID));
                delAlert(db, vid);
            }

        }
        cursor.close();

    }

    public ArrayList<Story> getPlayRecord() {


        Story playrecord = null;
        ArrayList<Story> arrayList = new ArrayList<Story>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {

            do {

                playrecord = new Story();

                playrecord.id = cursor.getString(cursor.getColumnIndex(VID));
                playrecord.title = cursor.getString(cursor.getColumnIndex(TITLE));
                playrecord.author = cursor.getString(cursor.getColumnIndex(AUTHOR));
                playrecord.content = cursor.getString(cursor.getColumnIndex(CONTENT));
                playrecord.img = cursor.getString(cursor.getColumnIndex(IMG));
                playrecord.voice = cursor.getString(cursor.getColumnIndex(VOICE));

                arrayList.add(playrecord);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return arrayList;

    }

    public void delAlerts() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }
}
