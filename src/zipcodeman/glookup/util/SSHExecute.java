package zipcodeman.glookup.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHExecute {
	private String uname;
	private String server;
	private String pass;

	public SSHExecute(String uname, String server, String pass) {
		this.uname = uname;
		this.server = server;
		this.pass = pass;
	}

	public String RunCommand(String command) {
		JSch jsch = new JSch();
		Session ses;
		String read = new String();
		try {
			ses = jsch.getSession(uname, server);
			ses.setPassword(pass);
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
			return "";
		} catch (IOException ioe) {
			return "";
		}
		
		return read;
	}
}
