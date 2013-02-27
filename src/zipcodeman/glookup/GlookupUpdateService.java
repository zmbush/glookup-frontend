package zipcodeman.glookup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class GlookupUpdateService extends IntentService {

	public GlookupUpdateService() {
		super("Glookup Update Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(Constants.UPDATE_SERVICE_TAG, "Handling Intent");
		LoginDataHelper dataDB = new LoginDataHelper(this);
        SQLiteDatabase readOnly = dataDB.getReadableDatabase();
        
        Cursor rows = readOnly.query("Users", null, null, null, null, null, null);
        if (rows != null)
        	rows.moveToFirst();
        if (rows.getCount() > 0) {
        	do {
        		if (rows.getColumnCount() >= 3) {
        			Integer uid = rows.getInt(0);
        			String uname = rows.getString(1);
        			String pass = rows.getString(2);
        			String server = rows.getString(3);
        			
        			JSch jsch = new JSch();
        			Session ses;
        			String read = new String();
        			try {
        				ses = jsch.getSession(uname, server);
        				ses.setPassword(pass);
        				java.util.Properties config = new java.util.Properties();
        				config.put("StrictHostKeyChecking", "no");
        				ses.setConfig(config);
        				ses.connect();
        				ChannelExec c = (ChannelExec)ses.openChannel("exec");
        				c.setCommand("source ~/.bash_profile;glookup");
        				c.connect();
        				BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
        				StringBuilder total = new StringBuilder();
        				String line = "";
        				while ((line = r.readLine()) != null) {
        					total.append(line + "\n");
        				}
        				read = total.toString();
        				c.disconnect();
        				ses.disconnect();
        				try {
        					FileInputStream fis = openFileInput(uid + "-data");
        					InputStreamReader isr = new InputStreamReader(fis);
        					BufferedReader br = new BufferedReader(isr);
            				StringBuffer fc = new StringBuffer();
        					while ((line = br.readLine()) != null) {
        						fc.append(line);
        						fc.append("\n");
        					}
        					String old = fc.toString();
        					Log.v(Constants.UPDATE_SERVICE_TAG, old);
        					Log.v(Constants.UPDATE_SERVICE_TAG, read);
            				if (! old.equals(read)) {
                    			Log.i(Constants.UPDATE_SERVICE_TAG, "Updated " + uname);
                    			createNotification(uid, uname, pass, server);
            				}
            			} catch (FileNotFoundException fnfe) {
            				Log.d(Constants.UPDATE_SERVICE_TAG, "No Data File Found");
            			} catch (IOException ioe) {
            				Log.d(Constants.UPDATE_SERVICE_TAG, "IO Exception");
            			} finally {
            				try {
            					Log.d(Constants.UPDATE_SERVICE_TAG, "Writing Data");
            					FileOutputStream fos = openFileOutput(uid + "-data", Context.MODE_PRIVATE);
            					fos.write(read.getBytes());
            					fos.close();
            					Log.d(Constants.UPDATE_SERVICE_TAG, "Data Write Complete");
            				} catch (FileNotFoundException fnfe) {
                				Log.d(Constants.UPDATE_SERVICE_TAG, "Could not write to data file");
            				} catch (IOException ioe) {
                				Log.d(Constants.UPDATE_SERVICE_TAG, "IO Exception");
            				}
            			}
            			Log.i(Constants.UPDATE_SERVICE_TAG, "Loaded for " + uname);
        			} catch(JSchException jse) {
        				
        			} catch(IOException ioe) {
        				
        			}
        		}
        	} while(rows.moveToNext());
        }
        readOnly.close();
        dataDB.close();
	}
	
	private void createNotification(int notificationID, String uname, String pass, String server){
		NotificationManager notificationMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Intent notifyIntent = new Intent(this, MainGradesActivity.class);
		notifyIntent.putExtra(GlookupFrontendActivity.GRADES_UNAME, uname);
		notifyIntent.putExtra(GlookupFrontendActivity.GRADES_PASS, pass);
		notifyIntent.putExtra(GlookupFrontendActivity.GRADES_SERVER, server);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(GlookupFrontendActivity.class);
		stackBuilder.addNextIntent(notifyIntent);
		
		PendingIntent pi = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
		
		Notification noti = new NotificationCompat.Builder(this)
								.setSmallIcon(R.drawable.icon)
								.setContentTitle("Grades Updated")
								.setContentText("Grades for " + uname + " have been updated")
								.setContentIntent(pi)
								.build();
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notificationMgr.notify(notificationID, noti);
								
		/*
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);
		
		NotificationManager notificationMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(android.R.drawable.star_on, "Grades for " + uname + " have been updated!", System.currentTimeMillis());
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationMgr.notify(notificationID, notification);*/
	}
}
