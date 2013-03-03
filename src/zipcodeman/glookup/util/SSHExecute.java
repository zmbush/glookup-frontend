package zipcodeman.glookup.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import zipcodeman.glookup.authentication.DSAKeys;

import android.content.Context;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHExecute {
	private String uname;
	private String server;
	private String pass;
	private Context c;

	public SSHExecute(Context c, String uname, String server, String pass) {
		this.uname = uname;
		this.server = server;
		this.pass = pass;
		this.c = c;
	}

	public String RunCommand(String command) {
		JSch jsch = new JSch();
		Session ses;
		String read = new String();
		try {
			if (pass.equals("dsa")) {
				DSAKeys dsak = new DSAKeys(c, uname, server);
				try {
					jsch.addIdentity(dsak.getPrivate(), dsak.getPassword());
				} catch (FileNotFoundException fnfe) {
					Log.e("SSHExecute", "Unable to load SSH Key files", fnfe);
					return "";
				}
			}
			ses = jsch.getSession(uname, server);
			if (!pass.equals("dsa")) ses.setPassword(pass);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			ses.setConfig(config);
			ses.connect();
			ChannelExec c = (ChannelExec) ses.openChannel("exec");
			c.setCommand("source ~/.bash_profile;" + command);
			c.connect();
			BufferedReader r = new BufferedReader(new InputStreamReader(
					c.getInputStream()));
			StringBuilder total = new StringBuilder();
			String line = "";
			while ((line = r.readLine()) != null) {
				total.append(line + "\n");
			}
			read = total.toString();
			c.disconnect();
			ses.disconnect();
		} catch (JSchException jse) {
			Log.e("SSHExecute", "JSch Problem", jse);
			return "";
		} catch (IOException ioe) {
			Log.e("SSHExecute", "IO Exception?", ioe);
			return "";
		}
		
		return read;
	}
}
