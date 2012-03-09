package com.zmbush.glookup;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.Toast;

public class MainListClickListner implements OnClickListener {

	private String mComment;
	// private View mParent;
	private String mAssignment;
	private String mUname;
	private String mPass;
	private String mServer;
	private Activity mActivity;
	MainListClickListner(String comment, View parent, String assignment, 
						String uname, String pass, String server, Activity superActivity){
	    mComment = comment;
		// mParent = parent;
		mAssignment = assignment;
		mUname = uname;
		mPass = pass;
		mServer = server;
		mActivity = superActivity;
	}
	public void onClick(View v) {
		Intent subView = new Intent(mActivity, SubGradeActivity.class);
		subView.putExtra(SubGradeActivity.SUB_GRADE_UNAME, mUname);
		subView.putExtra(SubGradeActivity.SUB_GRADE_PASS, mPass);
		subView.putExtra(SubGradeActivity.SUB_GRADE_SERVER, mServer);
		subView.putExtra(SubGradeActivity.SUB_GRADE_ASSIGNMENT, mAssignment);
		subView.putExtra(SubGradeActivity.SUB_GRADE_COMMENTS, mComment);
		mActivity.startActivity(subView);
	}

}
