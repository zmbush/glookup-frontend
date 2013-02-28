package zipcodeman.glookup;

public class GlookupGradeOutputParser {
	private boolean isDetails;
	GlookupGradeOutputParser(boolean isDetails) {
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
	
	public static class GlookupRow {
		public String assignName;
		public String score;
		public String grader;
		public String weight;
		public String comment;
		
		GlookupRow(String row) {
			initialize(row, false);
		}
		
		GlookupRow(String row, boolean isDetails) {
			initialize(row, isDetails);
		}
		
		public int getCurrentGrade() {
			try {
				return Integer.parseInt(score.split("/")[0]);
			} catch (NumberFormatException nfe) {
				return -1;
			}
		}
		
		public int getGradeOutOf() {
			try {
				return Integer.parseInt(score.split("/")[1]);
			} catch (NumberFormatException nfe) {
				return -1;
			}
		}
		
		private void initialize(String row, boolean isDetails) {
			if (isDetails) {
				
			} else {
				String rawRow = row.replaceFirst("[ ]*", "");
				String[] assignmentParts = rawRow.split(":", 2);
				assignName = assignmentParts[0];
				String restOfAssignmentString = assignmentParts[1].replaceFirst("[ ]*", "");
				
				int end = restOfAssignmentString.indexOf(" ");
				if(end == -1){
					score = restOfAssignmentString;
					grader = "";
				}else{
					score = restOfAssignmentString.substring(0, end);
					restOfAssignmentString = restOfAssignmentString.substring(end).replaceFirst("[ ]*", "");
					end = restOfAssignmentString.indexOf(" ");
					if(end != -1){
						weight = restOfAssignmentString.substring(0, end);
						restOfAssignmentString = restOfAssignmentString.substring(end).replaceFirst("[ ]*", "");
						end = restOfAssignmentString.indexOf(" ");
						if(end != -1){
							grader = restOfAssignmentString.substring(0, end);
							comment = restOfAssignmentString.substring(end).replaceFirst("[ ]*", "");
						}else{
							grader = restOfAssignmentString;
						}
					}
				}
			}
		}
	}
}
