package zipcodeman.glookup.util;

import android.os.SystemClock;
import android.app.AlarmManager;

public class Constants {
	public static final String UPDATE_SERVICE_TAG = "GlookupUpdateService";
	public static final long UPDATE_STARTUP_TIME = SystemClock.elapsedRealtime() + /*/ 10 /*/ 3000 /**/;
	public static final long UPDATE_FREQUENCY = /*/ 10000 /*/ AlarmManager.INTERVAL_HOUR /**/; // AlarmManager.INTERVAL_HOUR;
	public static final String DB_SECRET = "E\\/3R77!mm[O__uaoe}{#@!$soneu";
	public static final String SSH_KEY_HOST_NAME = "AndroidGlookupApp";
}
