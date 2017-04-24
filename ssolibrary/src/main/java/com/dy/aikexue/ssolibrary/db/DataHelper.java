package com.dy.aikexue.ssolibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dy.sdk.utils.GsonUtil;
import com.dy.sso.bean.Attrs;
import com.dy.sso.bean.Certification;
import com.dy.sso.bean.NewUserData;
import com.dy.sso.config.Config;
import com.dy.sso.util.Dysso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengdl
 * @create 2015-01-29
 */
public class DataHelper {
    // database name
    private static String DB_NAME = "dayang.db";
    // db version
    private static int DB_VERSION = 8;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;
    private static DataHelper dataHelper;

    private DataHelper(Context context) {
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized DataHelper getInstance(Context context) {
        if (dataHelper == null) {
            dataHelper = new DataHelper(context);
        }
        return dataHelper;
    }

    public void Close() {
        db.close();
        dbHelper.close();
        db = null;
        dbHelper = null;
    }

    public Long SaveUserInfo(UserInfo user) {
        long uid = -1;
        try {
            db.execSQL("DELETE FROM " + SqliteHelper.TB_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            dbHelper.onUpgrade(db, DB_VERSION, DB_VERSION);
        }
        try {
            ContentValues values = new ContentValues();
            values.put(UserInfo.USERID, user.getUserId());
            values.put(UserInfo.USERNAME, user.getUserName());
            values.put(UserInfo.TOKEN, user.getToken());
            values.put(UserInfo.ATTRS, user.getAttrs());
            values.put(UserInfo.PHONE, user.getPhone());
            values.put(UserInfo.PASS, user.getPass());
            values.put(UserInfo.HEADURL, user.getHeadurl());
            values.put(UserInfo.SIGN, user.getSign());
            values.put(UserInfo.GENDER, user.getGender());
            values.put(UserInfo.LOCATION, user.getLocation());
            values.put(UserInfo.NUMBER,user.getPrivated()!=null?user.getPrivated().getNo():"");
            //mete-data渠道字段
            values.put(UserInfo.CHANNAL, Config.getMetaDataString("sr-rel"));
            try {
                values.put(UserInfo.ATTRSINFO, GsonUtil.toJson(user.getAttrsInfo()));
                values.put(UserInfo.Certification, GsonUtil.toJson(user.getCertification()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            uid = db.insert(SqliteHelper.TB_NAME, UserInfo.ID, values);

            if(uid != -1){
                Dysso.resetUserInfo();
            }
            Log.i("DataHelper", "SaveUserInfo result--" + uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uid;
    }

    public boolean updateColumn(String columnName, String name, String userId) {
        ContentValues values = new ContentValues();
        values.put(columnName, name);
        if (db == null) return false;
        long id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "=?", new String[]{userId});
        Log.i("DataHelper", "updateColumn--" + id);
        if (id == 0) return false;
        return true;
    }

    public boolean updateIntColumn(String columnName, int number, String userId) {
        ContentValues values = new ContentValues();
        values.put(columnName, number);
        if (db == null) return false;
        long id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "=?", new String[]{userId});
        Log.i("DataHelper", "updateIntColumn--" + id);
        if (id == 0) return false;
        return true;
    }

    public void deleteToken() {
        try {
            db.execSQL("DELETE FROM " + SqliteHelper.TB_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserInfo GetUsrInfo() {
        try {
            List<UserInfo> userList = new ArrayList<UserInfo>();
            Cursor cursor = db.query(SqliteHelper.TB_NAME, null, "channel=?", new String[]{Config.getMetaDataString("sr-rel")}, null, null, UserInfo.ID + " DESC");
//          Cursor cursor = db.query(SqliteHelper.TB_NAME, null, null, null, null, null, UserInfo.ID + " DESC");
//		    Cursor  cursor=db.rawQuery("select * from "+SqliteHelper.TB_NAME,null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
                UserInfo user = new UserInfo();
                user.setId(cursor.getInt(cursor.getColumnIndex(UserInfo.ID)));
                user.setUserId(cursor.getString(cursor.getColumnIndex(UserInfo.USERID)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(UserInfo.USERNAME)));
                user.setToken(cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN)));
                user.setAttrs(cursor.getString(cursor.getColumnIndex(UserInfo.ATTRS)));
                user.setPass(cursor.getInt(cursor.getColumnIndex(UserInfo.PASS)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(UserInfo.PHONE)));
                user.setHeadurl(cursor.getString(cursor.getColumnIndex(UserInfo.HEADURL)));
                user.setGender(cursor.getInt(cursor.getColumnIndex(UserInfo.GENDER)));
                user.setSign(cursor.getString(cursor.getColumnIndex(UserInfo.SIGN)));
                user.setLocation(cursor.getString(cursor.getColumnIndex(UserInfo.LOCATION)));
                try {
                    user.setAttrsInfo((Attrs) GsonUtil.fromJson(cursor.getString(
                            cursor.getColumnIndex(UserInfo.ATTRSINFO)),Attrs.class));
                    user.setCertification((Certification) GsonUtil.fromJson(cursor.getString(
                            cursor.getColumnIndex(UserInfo.Certification)), Certification.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setCertification(null);
                }

                NewUserData data = GsonUtil.fromJson(user.getAttrs(), NewUserData.class);
                if(data != null && data.getData() != null && data.getData().getUsr() != null && data.getData().getUsr().getAttrs() != null) {
                    Attrs attrs = data.getData().getUsr().getAttrs();
                    user.setAttrsInfo(attrs);
                    user.setBasicUserInfo(attrs.getBasic());
                    user.setPrivated(attrs.getPrivated());
                    user.setExtraInfo(attrs.getExtra());
                }
//                Log.i("DataHelper", "GetUsrInfo from db--" + user.toString());
                userList.add(user);
                cursor.moveToNext();
            }
            cursor.close();
            if (userList.size() > 0) {
                return userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.i("DataHelper", "GetUsrInfo from db  null");
        return null;
    }

}
