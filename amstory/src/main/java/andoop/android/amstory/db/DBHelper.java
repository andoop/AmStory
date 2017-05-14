package andoop.android.amstory.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/5/11/011.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "amStory.db";
    private static final int DATABASE_VERSION = 2;
    //创建表,历史记录表
    public static final String TABLE_PLLAYRECORD = "playrecord";

    private static final String CREATE_PLLAYRECORD_SQL = "CREATE TABLE " + TABLE_PLLAYRECORD + " ("
            + "id TEXT PRIMARY KEY, "
            + "title TEXT, "
            + "author TEXT, "
            + "content TEXT, "
            + "img TEXT, "
            + "voice TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate() called with: db = [" + db + "]");
        db.execSQL(CREATE_PLLAYRECORD_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLLAYRECORD);
            onCreate(db);
        }
    }
}
