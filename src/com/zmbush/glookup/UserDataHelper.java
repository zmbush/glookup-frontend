package com.zmbush.glookup;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class UserDataHelper extends SQLiteOpenHelper {
	private static final int version = 1;
	private static final String CREATE_TABLE_USERS = "CREATE TABLE Users " +
	 "( uname TEXT, pass TEXT, server TEXT);";
	private static final String CREATE_TABLE_USERS2 = "CREATE TABLE Users " +
	 "( user_id INTEGER PRIMARY KEY, uname TEXT, pass TEXT, server TEXT);";

	public UserDataHelper(Context context) {
		super(context, "USER_DATA", null, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_USERS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion == 1 && newVersion == 2){
			db.execSQL(CREATE_TABLE_USERS2);
			Cursor rows = this.getReadableDatabase().query("Users", null, null, null, null, null, null);
			if(rows != null){
				rows.moveToFirst();
	    		do{
	    			if(rows.getColumnCount() >= 3){
	    				db.execSQL("INSERT INTO Users2  (uname, pass, server) VALUES ('" + rows.getString(0) + "','" + 
	    							rows.getString(1) + "','" + rows.getString(2));
	    			}
	    		}while(rows.moveToNext());
			}
		}
	}

}
