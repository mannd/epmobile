package org.epstudios.epmobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DoseTable extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dosetable);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		lowEnd = prefs.getInt("lowEnd", 0);
		highEnd = prefs.getInt("highEnd", 0);
		increase = prefs.getBoolean("increase", true);
		
		TextView percent1TextView = (TextView) findViewById(R.id.percent1);
		percent1TextView.setText(String.valueOf(lowEnd) + "% " 
				+ (increase ? "Increase" : "Decrease"));
		TextView percent2TextView = (TextView) findViewById(R.id.percent2);
		percent2TextView.setText(String.valueOf(highEnd) + "% " 
				+ (increase ? "Increase" : "Decrease"));
	}

	@Override
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
	
	private int lowEnd;
	private int highEnd;
	private Boolean increase;

}
