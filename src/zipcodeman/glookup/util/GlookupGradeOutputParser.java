package zipcodeman.glookup.util;

import java.util.Hashtable;

public class GlookupGradeOutputParser {
	private boolean isDetails;
	
	public GlookupGradeOutputParser() {
		this.isDetails = false;
	}
	
	public GlookupGradeOutputParser(boolean isDetails) {
		this.isDetails = isDetails;
	}
	
	public GlookupRow[] parseOutput(String contents) {
		String[] rows = contents.split("\n");
		GlookupRow[] response = new GlookupRow[rows.length];
		
		for (int i = 0; i < rows.length; i++) {
			response[i] = new GlookupRow(rows[i], this.isDetails);
		}
		return response;
	}
	
	public Hashtable<String, GlookupRow> parseOutputHash(String contents) {
		GlookupRow[] rows = parseOutput(contents);
		Hashtable<String, GlookupRow> response = new Hashtable<String, GlookupRow>(rows.length);
		
		for (int i = 0; i < rows.length; i++) {
			response.put(rows[i].assignName, rows[i]);
		}
		
		return response;
	}
}
