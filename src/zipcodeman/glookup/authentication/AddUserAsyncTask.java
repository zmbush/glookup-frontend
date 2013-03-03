package zipcodeman.glookup.authentication;

import zipcodeman.glookup.GlookupFrontendActivity;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class AddUserAsyncTask extends AsyncTask<String, Integer, Boolean> {

	@Override
	protected void onPostExecute(Boolean result) {
		ListActivity la = (ListActivity)this.context;
		GlookupFrontendActivity gfa = (GlookupFrontendActivity)la;
		if (!result) {
			if (isAdd) {
				gfa.editAccount(uname, pass, server);
			} else {
				gfa.editAccount(uid, uname, pass, server);
			}
		} else {
			gfa.loadList();
		}
		try {
			p.dismiss();
		} catch (IllegalArgumentException iae) {
			Log.e("AddUserAsyncTask", "Could not dismiss progress");
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		p.setProgress(values[0]);
		p.setMessage(this.getProgressString(values[0]));
		super.onProgressUpdate(values);
	}

	private static final int PROG_INIT = 0;
	private static final int PROG_CREATING = 1;
	private static final int PROG_INSTALLING = 2;
	private static final int PROG_DONE = 3;
	private Activity context;
	private ProgressDialog p;
	private String uname;
	private String pass;
	private String server;
	private boolean isAdd;
	private SQLiteDatabase edit;
	private int uid;

	public AddUserAsyncTask(Activity c, boolean isAdd, SQLiteDatabase edit) {
		this.context = c;
		this.isAdd = isAdd;
		this.edit = edit;
		this.p = new ProgressDialog(context);
		p.setCancelable(false);
		p.setMax(PROG_DONE);
		p.setProgress(PROG_INIT);
		p.show();
		p.setMessage(getProgressString(PROG_INIT));
	}
	
	private String getProgressString(int progInt) {
		switch (progInt) {
		case PROG_INIT:
			return "Starting...";
		case PROG_CREATING:
			return "Creating DSA Keys";
		case PROG_INSTALLING:
			return "Adding key to server";
		case PROG_DONE:
			return "User " + uname + " successfully created";
		}
		return "";
	}

	@Override
	protected Boolean doInBackground(String... params) {
		uname = params[0];
		pass = params[1];
		server = params[2];
		try {
			uid = Integer.parseInt(params[3]);
		} catch (NumberFormatException nfe) {
			uid = -1;
		}
		
		publishProgress(PROG_INIT);
		
		DSAKeys dsak = new DSAKeys(context, uname, server, pass);
		
		publishProgress(PROG_CREATING);
		if (!dsak.createKeys()) return false;
		
		publishProgress(PROG_INSTALLING);
		if (!dsak.installKeys()) return false;
		
		if (this.isAdd) {
			ContentValues values = new ContentValues(3);
			values.put("uname", this.uname);
			values.put("server", this.server);
			values.put("pass", "dsa");
			edit.insert("Users", null, values);
		} else {
			ContentValues vals = new ContentValues(3);
			vals.put("uname", uname);
			vals.put("pass", "dsa");
			vals.put("server", server);
			edit.update("Users", vals, "user_id = " + uid, null);
		}
		
		publishProgress(PROG_DONE);
		return true;
	}

}
