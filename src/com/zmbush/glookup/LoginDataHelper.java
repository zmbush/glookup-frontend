package com.zmbush.glookup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class LoginDataHelper extends SQLiteOpenHelper {
	private static final int version = 1;
	private static final String CREATE_TABLE_USERS = "CREATE TABLE Users " +
	 "( user_id INTEGER PRIMARY KEY, uname TEXT, pass TEXT, server TEXT);";
	
	Context c;

	public LoginDataHelper(Context context) {
		super(context, "LOGIN_DATA", null, version);
		c = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_USERS);
		UserDataHelper h = new UserDataHelper(c);
		SQLiteDatabase sld = h.getReadableDatabase();
		Cursor rows = sld.query("Users", null, null, null, null, null, null);
		if(rows != null){
			if(rows.moveToFirst()){
				do{
					ContentValues values = new ContentValues(3);
	    			values.put("uname", rows.getString(0));
	    			values.put("pass", rows.getString(1));
	    			values.put("server", rows.getString(2));
					db.insert("Users", null, values);
				}while(rows.moveToNext());
			}
		}
		c.deleteDatabase("USER_DATA");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
