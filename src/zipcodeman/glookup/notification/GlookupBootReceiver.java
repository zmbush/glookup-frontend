package zipcodeman.glookup.notification;

import zipcodeman.glookup.GlookupFrontendActivity;
import zipcodeman.glookup.util.Constants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GlookupBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(Constants.UPDATE_SERVICE_TAG, "BootReceiver Triggered");
		GlookupFrontendActivity.scheduleAlarmReceiver(context);
	}

}
