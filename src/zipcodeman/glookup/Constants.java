package zipcodeman.glookup;

import android.os.SystemClock;
import android.app.AlarmManager;

public class Constants {
	public static final String UPDATE_SERVICE_TAG = "GlookupUpdateService";
	public static final long UPDATE_STARTUP_TIME = SystemClock.elapsedRealtime() + 3000;
	public static final long UPDATE_FREQUENCY = AlarmManager.INTERVAL_HOUR;
}
