package zipcodeman.glookup.util;

import zipcodeman.glookup.R;
import zipcodeman.glookup.R.xml;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.glookup_preferences);
	}
	
}
