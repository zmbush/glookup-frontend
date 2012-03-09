package com.zmbush.glookup;

import zipcodeman.glookup.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
//import android.widget.Toast;


public class AddUserActivity extends Activity {
    private int uid;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);
        
        Intent incoming = this.getIntent();
        uid = incoming.getIntExtra(GlookupFrontendActivity.ADD_USER_ID, -1);
        if(uid != -1){
        	TextView uname = (TextView) findViewById(R.id.addUserUnameText);  
        	TextView pass = (TextView) findViewById(R.id.addUserPasswordText);
        	Spinner server = (Spinner) findViewById(R.id.addUserServerSelect);

        	uname.setText(incoming.getStringExtra(GlookupFrontendActivity.ADD_USER_USERNAME));
        	pass.setText(incoming.getStringExtra(GlookupFrontendActivity.ADD_USER_PASSWORD));
        	
        	String[] servers = this.getResources().getStringArray(R.array.server_list);
        	String serv = incoming.getStringExtra(GlookupFrontendActivity.ADD_USER_SERVER);
        	int pos = 0;
        	for(int i = 0; i < servers.length; i++){
        		if(servers[i] == serv){
        			pos = i;
        			break;
        		}
        	}
        	server.setSelection(pos);
        }
    }
    
    public void submit(View button){
    	Intent res = new Intent(this, GlookupFrontendActivity.class);
    	TextView uname = (TextView) findViewById(R.id.addUserUnameText);  
    	TextView pass = (TextView) findViewById(R.id.addUserPasswordText);
    	Spinner server = (Spinner) findViewById(R.id.addUserServerSelect);
    	TextView server_selected = (TextView) server.getSelectedView().findViewById(android.R.id.text1);
    	res.putExtra(GlookupFrontendActivity.ADD_USER_USERNAME, uname.getText().toString());
    	res.putExtra(GlookupFrontendActivity.ADD_USER_PASSWORD, pass.getText().toString());
    	res.putExtra(GlookupFrontendActivity.ADD_USER_SERVER, server_selected.getText().toString());
    	if(uid >= 0){
    		res.putExtra(GlookupFrontendActivity.ADD_USER_ID, uid);
    	}
    	setResult(Activity.RESULT_OK, res);
    	finish();
    }
}
