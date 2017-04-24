package com.dy.aikexue.ssolibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author zengdl
 * @create 2015-01-29
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public static final String TB_NAME = "users";

    public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + "(" +
                UserInfo.ID + "  INTEGER primary key AUTOINCREMENT," +
                UserInfo.USERID + "  varchar," +
                UserInfo.USERNAME + "  varchar," +
                UserInfo.TOKEN + "  varchar," +
                UserInfo.HEADURL + "  varchar," +
                UserInfo.SIGN + "  varchar," +
                UserInfo.GENDER + "  INTEGER," +
                UserInfo.LOCATION + "  varchar," +
                UserInfo.PHONE + "   varchar," +
                UserInfo.PASS + "   INTEGER," +
                UserInfo.Certification + "   varchar," +
                UserInfo.ATTRS + "  varchar," +
                UserInfo.CHANNAL + "  varchar," +
                UserInfo.NUMBER + " varchar," +
                UserInfo.ATTRSINFO + " varchar" +
                ")"
        );
        Log.e("Database", "Database  onCreate");
    }

    //update table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
        Log.e("Database", "onUpgrade");
    }
}
