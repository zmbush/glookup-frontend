package zipcodeman.glookup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class GlookupBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(Constants.UPDATE_SERVICE_TAG, "BootReceiver Triggered");
		GlookupFrontendActivity.scheduleAlarmReceiver(context);
	}

}
