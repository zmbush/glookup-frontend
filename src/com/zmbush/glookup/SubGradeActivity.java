package com.zmbush.glookup;

import zipcodeman.glookup.R;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class SubGradeActivity extends ListActivity {
	public static final String SUB_GRADE_UNAME = "username";
	public static final String SUB_GRADE_PASS = "pass";
	public static final String SUB_GRADE_SERVER = "server";
	public static final String SUB_GRADE_ASSIGNMENT = "assign";
	public static final String SUB_GRADE_COMMENTS = "comments";
	
	private String uname, pass, server, assign, comments;
	
	private String[] rows;
	public void setRows(String[] r){
		rows = r;
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
	
	public void render(){
		this.setListAdapter(new SubListAdapter(rows, comments, assign, this, this));		
	}
	
	private void loadData(){
		loadData(false);
	}
	private void loadData(boolean reload){
		if(rows == null || reload){
			LoadSubGradesAsyncTask lsgat = new LoadSubGradesAsyncTask(this);
			if(!reload){
				String[] loading = new String[]{ 
						"Loading...",
						"Assignment: " + assign,
						"Comments: " + ((comments.equals("")) ? "None" : comments) };
				setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , loading));
			}
			lsgat.execute(uname, pass, server, assign, comments);
		}else{
			render();
		}
	}
	
	public void onCreate(Bundle savedState){
		super.onCreate(savedState);
		
		Intent in = this.getIntent();
		uname = in.getStringExtra(SUB_GRADE_UNAME);
		pass = in.getStringExtra(SUB_GRADE_PASS);
		server = in.getStringExtra(SUB_GRADE_SERVER);
		assign = in.getStringExtra(SUB_GRADE_ASSIGNMENT);
		comments = in.getStringExtra(SUB_GRADE_COMMENTS);
		
		loadData();
	}
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		loadData();
	}
}
