package zipcodeman.glookup.maingrades;

import zipcodeman.glookup.R;
import zipcodeman.glookup.util.GlookupRow;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainListAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private Activity mAct;
	private String[] rows;
	private String uname;
	private String pass;
	private String server;
	
	MainListAdapter(String[] rows, String uname, String pass, String server, Context c, Activity act){
		this.mInflater = LayoutInflater.from(c);
		this.rows = rows;
		this.mAct = act;
		this.uname = uname;
		this.pass = pass;
		this.server = server;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = this.mInflater.inflate(R.layout.main_list_item, null);
		
		TextView one = (TextView)convertView.findViewById(R.id.main_list_text_1);
		TextView two = (TextView)convertView.findViewById(R.id.main_list_text_2);
    	TextView three = (TextView)convertView.findViewById(R.id.main_list_text_3);
		if(position == 0){
			String row = rows[position];
			String[] items = row.split(":");
			one.setText(items[0]);
			String rest = items[1].replaceFirst("[ ]*", "");
			String[] other = rest.replace(".", "").split(" ");
			two.setText(other[0].replace("_", " "));
			three.setText(other[1].replace("_", " "));
		}else if(position == 1){
			one.setText("Assignment");
			two.setText("Score");
			three.setText("Weight");
		}else{
			GlookupRow gr = new GlookupRow(rows[position]);
			
			Log.i("MainListAdapter", "I found that the grader was: " + gr.grader);
			
			if(gr.comment != ""){
				convertView = this.mInflater.inflate(R.layout.comment_main_list_item, null);
				
				one = (TextView)convertView.findViewById(R.id.main_list_text_1);
				two = (TextView)convertView.findViewById(R.id.main_list_text_2);
		    	three = (TextView)convertView.findViewById(R.id.main_list_text_3);
		    	
		    	TextView commentText = (TextView)convertView.findViewById(R.id.comment_text);
		    	
				one.setText(gr.assignName);
				two.setText(gr.score);
				three.setText(gr.weight);
				commentText.setText(gr.comment);
			}else{
				one.setText(gr.assignName);
				two.setText(gr.score);
				three.setText(gr.weight);
			}
			if(!gr.assignName.equals("Extrapolated total")){
				convertView.setClickable(true);
				convertView.setOnClickListener(new MainListClickListner(gr.comment, parent, gr.assignName, 
												uname, pass, server, this.mAct));
			}
		}
		return convertView;
	}

	public int getCount() {
		return this.rows.length;
	}

	public Object getItem(int position) {
		return this.rows[position];
	}

	public long getItemId(int position) {
		return position;
	}
}