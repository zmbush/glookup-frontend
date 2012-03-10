package zipcodeman.glookup;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;

//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;

// import android.app.Activity;
import zipcodeman.glookup.R;
import android.app.ListActivity;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
//import android.widget.Toast;

public class MainGradesActivity extends ListActivity {
	
	public static final String[] loading = { "Loading..." };
	private String[] rows = null;
	private String uname, pass, server;
	
	public void setRows(String[] r){
		rows = r;
	}
	
	public void render(){
		if(rows != null){
			setListAdapter(new MainListAdapter(rows, uname, pass, server, this, this));
		}else{
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , 
							new String[]{"There was a problem fetching data", 
										"Please check your username and password"}));
		}
	}
	
	private void loadData(){
		loadData(false);
	}
	
	private void loadData(boolean reload){
        if(rows == null || reload){
        	LoadMainGradesAsyncTask lmgat = new LoadMainGradesAsyncTask(this);
        	if(!reload)
        		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , loading));
        	lmgat.execute(uname, pass, server);
        }else{
        	render();
        }
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.grades_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.reload:
        	loadData(true);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        Intent current = this.getIntent();
        uname = current.getStringExtra(GlookupFrontendActivity.GRADES_UNAME);
        pass = current.getStringExtra(GlookupFrontendActivity.GRADES_PASS);
        server = current.getStringExtra(GlookupFrontendActivity.GRADES_SERVER);
        loadData();
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		loadData();
	}
}
