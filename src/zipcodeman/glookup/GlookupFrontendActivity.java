package zipcodeman.glookup;

//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.util.Map;
//
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;

import zipcodeman.glookup.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


// TODO: Move to ssh keys. Perhaps more secure.
public class GlookupFrontendActivity extends ListActivity {

	public static final int ADD_USER_RESULT = 0;
	public static final int EDIT_USER_RESULT = 1;
	public static final String ADD_USER_USERNAME = "username";
	public static final String ADD_USER_PASSWORD = "password";
	public static final String ADD_USER_SERVER = "server";
	public static final String GRADES_UNAME = "username";
	public static final String GRADES_PASS = "password";
	public static final String GRADES_SERVER = "server";
	public static final String PREFS_NAME = "glookupFrontendData";
	public static final String PREFS_USER = "glookupFrontendUser";
	public static final String[] items = { "Open Menu and Click Add New User" };
	public static final String ADD_USER_ID = "uid";
	
	private String[] unames, passwords, servers;
	
	private LoginDataHelper dataDB;
	private SQLiteDatabase readOnly;
	private SQLiteDatabase writeOnly;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        dataDB = new LoginDataHelper(this);
        readOnly = dataDB.getReadableDatabase();
        writeOnly = dataDB.getWritableDatabase();
        this.registerForContextMenu(getListView());
        
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        
        if (pref.getBoolean("check-for-updates", true))
        	scheduleAlarmReceiver(this);
        
        loadList();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
    }
    
    private void loadList(){
    	SQLiteDatabase read = readOnly;
    	Cursor rows = read.query("Users", null, null, null, null, null, null);
    	if(rows != null)
    		rows.moveToFirst();
    	int count = rows.getCount();
    	if(count > 0){
    		int i = 0;
    		String[] users = new String[count];
    		this.unames = new String[count];
    		this.passwords = new String[count];
    		this.servers = new String[count];
    		do{
    			if(rows.getColumnCount() >= 3){
    				String uname = rows.getString(1);
    				String pass = rows.getString(2);
    				String server = rows.getString(3);
    				unames[i] = uname;
    				passwords[i] = pass;
    				servers[i] = server;
    				users[i] = uname + "@" + server;
    			}
    			i++;
    		}while(rows.moveToNext());
    		rows.close();
    		
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , users));
    	}else{
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , items));
		}
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	SQLiteDatabase sld = readOnly;
    	Cursor rows = sld.rawQuery("SELECT * FROM Users LIMIT " + position + ", 1", null);
    	if(rows != null)
    		rows.moveToFirst();
    	if(rows.getCount() > 0){
    		Intent mainGrades = new Intent(this, MainGradesActivity.class);

    		String uname = rows.getString(1);
    		String pass = rows.getString(2);
    		String server = rows.getString(3);
    		
    		rows.close();
    		mainGrades.putExtra(GRADES_UNAME, uname);
        	mainGrades.putExtra(GRADES_PASS, pass);
        	mainGrades.putExtra(GRADES_SERVER, server);
        	this.startActivity(mainGrades);
    	}
    	super.onListItemClick(l, v, position, id);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
    	if(view.getId() == this.getListView().getId()){
    		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    		TextView tv = (TextView)info.targetView.findViewById(android.R.id.text1);
    		menu.setHeaderTitle(tv.getText());
    		menu.add(Menu.NONE, 0, 0, R.string.delete_connection);
    		menu.add(Menu.NONE, 1, 1, R.string.edit_connection);
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item){	    	
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    	SQLiteDatabase sld = dataDB.getWritableDatabase();
    	Cursor row = sld.rawQuery("SELECT * FROM Users LIMIT " + info.position + ", 1", null);
    	switch(item.getItemId()){
    	case 0:
	    	if(row != null){
	    		row.moveToFirst();
		    	if(row.getCount() >= 1){
		    		sld.delete("Users",  "user_id=" + row.getInt(0), null);
		    	}
	    	}
	    	loadList();
	    	break;
    	case 1:
	    	if(row != null){
	    		row.moveToFirst();
	    		if(row.getCount() >= 1){
	    			this.editAccount(row.getInt(0), row.getString(1), row.getString(2), row.getString(3));
	    		}
	    	}
	    	loadList();
    		break;
    	}
    	return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.addAccount:
        	addAccount();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == ADD_USER_RESULT){
    		if(resultCode == Activity.RESULT_OK){
    			String uname = data.getExtras().getString(ADD_USER_USERNAME);
    			String pass = data.getExtras().getString(ADD_USER_PASSWORD);
    			String server = data.getExtras().getString(ADD_USER_SERVER);
    			
    			SQLiteDatabase edit = writeOnly;
    			
    			ContentValues values = new ContentValues(3);
    			values.put("uname", uname);
    			values.put("pass", pass);
    			values.put("server", server);
    			edit.insert("Users", null, values);
    			
    			Toast.makeText(this, "Added user: " + uname, Toast.LENGTH_LONG).show();
    			loadList();
    		}
    	}else if(requestCode == EDIT_USER_RESULT){
    		if(resultCode == Activity.RESULT_OK){
    			String uname = data.getExtras().getString(ADD_USER_USERNAME);
    			String pass = data.getExtras().getString(ADD_USER_PASSWORD);
    			String server = data.getExtras().getString(ADD_USER_SERVER);
    			int uid = data.getIntExtra(ADD_USER_ID, -1);
    			if(uid > 0){
    				SQLiteDatabase edit = writeOnly;
    				
    				ContentValues vals = new ContentValues(3);
    				vals.put("uname", uname);
    				vals.put("pass", pass);
    				vals.put("server", server);
    				edit.update("Users", vals, "user_id = " + uid, null);
    				loadList();
    			}
    		}
    	}
    }

    private void addAccount(){
    	Intent addUser = new Intent(this, AddUserActivity.class);
    	GlookupFrontendActivity.this.startActivityForResult(addUser, ADD_USER_RESULT);
    }

    private void editAccount(int id, String uname, String pass, String server){
    	Intent editUser = new Intent(this, AddUserActivity.class);
    	editUser.putExtra(ADD_USER_ID, id);
    	editUser.putExtra(ADD_USER_USERNAME, uname);
    	editUser.putExtra(ADD_USER_PASSWORD, pass);
    	editUser.putExtra(ADD_USER_SERVER, server);
    	GlookupFrontendActivity.this.startActivityForResult(editUser, EDIT_USER_RESULT);
    }
    
    public static void scheduleAlarmReceiver(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                 PendingIntent.getBroadcast(context, 0, new Intent(context, GlookupAlarmReceiver.class),
                          PendingIntent.FLAG_CANCEL_CURRENT);

        // Use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, Constants.UPDATE_STARTUP_TIME,
                 				     Constants.UPDATE_FREQUENCY, pendingIntent);
     }
}