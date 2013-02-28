package zipcodeman.glookup.subgrades;
import zipcodeman.glookup.R;
import zipcodeman.glookup.R.id;
import zipcodeman.glookup.R.layout;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
import android.widget.TextView;

//import java.util.Random;
//import java.util.regex.*;
//
//import org.achartengine.ChartFactory;
//import org.achartengine.chart.BarChart.Type;
//import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
//import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

public class SubListAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	// private Context mContext;
	private Activity mParent;
	private String mComment;
	private String[] rows;
	private String mAssignment;
	XYMultipleSeriesDataset dataset;
	XYMultipleSeriesRenderer renderer;
	
	SubListAdapter(String[] rows, String comment, String assign, Context c, Activity par){
		this.mInflater = LayoutInflater.from(c);
		// this.mContext = c;
		this.rows = rows;
		this.mParent = par;
		this.mComment = comment;
		this.mAssignment = assign;
	}
	
//	private void prepareGraph(int start){
//		dataset = new XYMultipleSeriesDataset();
//		renderer = new XYMultipleSeriesRenderer();
//		CategorySeries series = new CategorySeries("Grades");
//		//XYSeries dist = new XYSeries("grades");
//		for(int i = start; i < rows.length; i++){
//			String row = rows[i];
//			String[] elements = row.replaceFirst("[ ]+", "").split("[ ]+");
////			String first = elements[0];
//			String number = elements[3];
////			int x = (int)Float.parseFloat(first);
//			int y = Integer.parseInt(number);
//			series.add(y);
//		}
//		dataset.addSeries(series.toXYSeries());
//		
//	    renderer.setAxisTitleTextSize(16);
//	    renderer.setChartTitleTextSize(20);
//	    renderer.setLabelsTextSize(15);
//	    renderer.setLegendTextSize(15);
//	    renderer.setMargins(new int[] {20, 30, 15, 0});
//	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
//	    r.setColor(Color.BLUE);
//	    renderer.addSeriesRenderer(r);
//		
//	    
//	    dataset = new XYMultipleSeriesDataset();
//	    final int nr = 10;
//	    Random ran = new Random();
//	    for (int i = 0; i < 2; i++) {
//	      CategorySeries series2 = new CategorySeries("Demo series " + (i + 1));
//	      for (int k = 0; k < nr; k++) {
//	        series2.add(100 + ran.nextInt() % 100);
//	      }
//	      dataset.addSeries(series2.toXYSeries());
//	    }
//	    
//	    renderer = new XYMultipleSeriesRenderer();
//	    renderer.setAxisTitleTextSize(16);
//	    renderer.setChartTitleTextSize(20);
//	    renderer.setLabelsTextSize(15);
//	    renderer.setLegendTextSize(15);
//	    renderer.setMargins(new int[] {20, 30, 15, 0});
//	    SimpleSeriesRenderer r1 = new SimpleSeriesRenderer();
//	    r1.setColor(Color.BLUE);
//	    renderer.addSeriesRenderer(r1);
//	    r1 = new SimpleSeriesRenderer();
//	    r1.setColor(Color.GREEN);
//	    renderer.addSeriesRenderer(r1);
//	    renderer.setChartTitle("Chart demo");
//	    renderer.setXTitle("x values");
//	    renderer.setYTitle("y values");
//	    renderer.setXAxisMin(0.5);
//	    renderer.setXAxisMax(10.5);
//	    renderer.setYAxisMin(0);
//	    renderer.setYAxisMax(210);
//	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = this.mInflater.inflate(R.layout.sub_list_item, null);
		
		TextView one = (TextView)convertView.findViewById(R.id.sub_list_text_1);
		TextView two = (TextView)convertView.findViewById(R.id.sub_list_text_2);
		
		convertView.setClickable(false);
		if(position == 0){
			String row = rows[position];
			String[] items = row.split(":");
			one.setText(items[0]);
			String rest = items[1].replaceFirst("[ ]*", "");
			two.setText(rest);
		}else if(position == 1){
			convertView = this.mInflater.inflate(android.R.layout.simple_list_item_1, null);
			one = (TextView)convertView.findViewById(android.R.id.text1);
			
			one.setText("Assignment: " + this.mAssignment);
		}else if(position == 2){
			convertView = this.mInflater.inflate(android.R.layout.simple_list_item_1, null);
			one = (TextView)convertView.findViewById(android.R.id.text1);
			
			one.setText("Comments: " + ((this.mComment.equals("")) ? "None" : this.mComment));
		}else if(position < this.getCount() - 1){
			position -= 2;
			String firstStripped = rows[position].replaceFirst("[ ]*", "");
			String[] firstSplit = firstStripped.split(":");
			String rank = "";
			
			String stat = firstSplit[0];
			firstStripped = firstSplit[1].replaceFirst("[ ]*", "");
			int end = firstStripped.indexOf(" ");
			String score = "";
			if(end == -1){
				score = firstStripped;
			}else{
				score = firstStripped.substring(0, end);
				String[] paren = rows[position].replaceFirst("[ ]*", "").split("\\(");
				if(paren.length > 1){
					rank = paren[1].replace(")", "");
				    convertView = this.mInflater.inflate(R.layout.comment_sub_list_item, null);
					one = (TextView)convertView.findViewById(R.id.sub_list_text_1);
					two = (TextView)convertView.findViewById(R.id.sub_list_text_2);
					TextView comment = (TextView)convertView.findViewById(R.id.comment_text);
					comment.setText(rank);
					one.setText(stat);
					two.setText(score);
					return convertView;
				}
			}
			
			one.setText(stat);
			two.setText(score);
		}else{
			convertView = this.mInflater.inflate(android.R.layout.simple_list_item_1, null);
			
			one = (TextView)convertView.findViewById(android.R.id.text1);
			one.setText("Show Grade Distribution");
			
			convertView.setClickable(true);
			convertView.setOnClickListener(new SubListClickListner(this.mParent, rows, this.getCount()));
		}
		return convertView;
	}

	public int getCount() {
		int i;
		for(i = 0; i < rows.length; i++)
			if(rows[i].equals("Distribution:"))
				break;
		return i + 3;
	}

	public Object getItem(int position) {
		return this.rows[position];
	}

	public long getItemId(int position) {
		return position;
	}
}