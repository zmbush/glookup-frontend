package zipcodeman.glookup.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.AndroidCharacter;
import android.widget.Toast;

public class GlookupAccount extends Service {
	private static final String TAG = "GlookupAccount";
	private static GlookupAccountAuth authenticator = null; 
	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		if(intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT))
			ret = getAuth().getIBinder();
		return ret;
	}
	
	private GlookupAccountAuth getAuth(){
		if(authenticator == null)
			authenticator = new GlookupAccountAuth(this);
		return authenticator; 
	}
	
	private class GlookupAccountAuth extends AbstractAccountAuthenticator {
	
		private Context c;
		public GlookupAccountAuth(Context context) {
			super(context);
			c = context;
		}
	
		@Override
		public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
				String authTokenType, String[] requiredFeatures, Bundle arg4)
				throws NetworkErrorException {
			Toast.makeText(this.c, "boo", Toast.LENGTH_SHORT).show();
			return null;
		}
	
		@Override
		public Bundle confirmCredentials(AccountAuthenticatorResponse arg0,
				Account arg1, Bundle arg2) throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Bundle editProperties(AccountAuthenticatorResponse arg0, String arg1) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Bundle getAuthToken(AccountAuthenticatorResponse arg0, Account arg1,
				String arg2, Bundle arg3) throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public String getAuthTokenLabel(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Bundle hasFeatures(AccountAuthenticatorResponse arg0, Account arg1,
				String[] arg2) throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Bundle updateCredentials(AccountAuthenticatorResponse arg0,
				Account arg1, String arg2, Bundle arg3)
				throws NetworkErrorException {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
