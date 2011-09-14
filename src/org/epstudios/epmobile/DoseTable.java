/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2011 EP Studios, Inc.
    www.epstudiossoftware.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */   

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
		tabletDose = prefs.getString("tabletDose", "");
		
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
	private String tabletDose;

}
