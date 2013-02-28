package zipcodeman.glookup.subgrades;

//import java.util.Random;

import org.achartengine.ChartFactory;
//import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
//import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
//import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.Toast;

public class SubListClickListner implements OnClickListener {

	private Activity mActivity;
	String[] rows;
	int start;

    XYMultipleSeriesDataset dataset;
    XYMultipleSeriesRenderer renderer;
	SubListClickListner(Activity superActivity, String[] rows, int start){
		mActivity = superActivity;
		this.rows = rows;
		this.start = start;
	}
	
	private void prepareGraph(){
		dataset = new XYMultipleSeriesDataset();
		renderer = new XYMultipleSeriesRenderer();
		CategorySeries series = new CategorySeries("Grades");
		//XYSeries dist = new XYSeries("grades");
		int xmin = 1000;
		int ymin = 1000;
		int xmax = 0;
		int ymax = 0;
		for(int i = start; i < rows.length; i++){
			String row = rows[i];
			String removed = row.replaceFirst(
					"[ ]*[0-9]+\\.[0-9]+[ ]*-[ ]*[0-9]+.[0-9]+:[ ]*", "");
			String[] elements = removed.split("[ ]+");
			String first = elements[0];
			String number = elements[0];
			
			int x = (int)Float.parseFloat(first);
			int y = Integer.parseInt(number);
			if(x < xmin) xmin = x;
			if(x > xmax) xmax = x;
			if(y < ymin) ymin = y;
			if(y > ymax) ymax = y;
			series.add(y);
		}
		xmin = 1;
		xmax = rows.length - start + 1;
		dataset.addSeries(series.toXYSeries());
		
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setMargins(new int[] {30, 25, 0, 10});
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(Color.BLUE);
	    renderer.addSeriesRenderer(r);
	    renderer.setChartTitle("Score Distribution");
	    renderer.setXTitle("Score Range");
	    renderer.setYTitle("Number of Students");
	    renderer.setXAxisMin(xmin);
	    renderer.setXAxisMax(xmax);
	    renderer.setYAxisMin(ymin);
	    renderer.setYAxisMax(ymax);
	    renderer.setPanEnabled(false);
	}
	public void onClick(View v) {
		prepareGraph();
		Intent distGraph = ChartFactory.getBarChartIntent(mActivity, dataset, renderer, Type.DEFAULT);
		mActivity.startActivity(distGraph);
	}

}
