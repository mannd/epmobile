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

public class DoseTable extends EpDrugCalculatorActivity implements
		OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dosetable);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		lowEnd = prefs.getInt("lowEnd", 0);
		highEnd = prefs.getInt("highEnd", 0);
		increase = prefs.getBoolean("increase", true);
		tabletDose = prefs.getFloat("tabletDose", 0);
		double weeklyDose = prefs.getFloat("weeklyDose", 0);

		this.setTitle("Warfarin Dose (" + String.valueOf(tabletDose)
				+ " mg tabs)");
		TextView percent1TextView = (TextView) findViewById(R.id.percent1);
		percent1TextView.setText(String.valueOf(lowEnd) + "% "
				+ (increase ? "Increase" : "Decrease"));
		TextView percent2TextView = (TextView) findViewById(R.id.percent2);
		percent2TextView.setText(String.valueOf(highEnd) + "% "
				+ (increase ? "Increase" : "Decrease"));

		double newLowEndWeeklyDose = Warfarin.getNewDoseFromPercentage(
				lowEnd / 100.0, weeklyDose, increase);
		double newHighEndWeeklyDose = Warfarin.getNewDoseFromPercentage(
				highEnd / 100.0, weeklyDose, increase);
		DoseCalculator doseCalculator = new DoseCalculator(tabletDose,
				newLowEndWeeklyDose);
		double[] result = doseCalculator.weeklyDoses();
		((TextView) findViewById(R.id.sunDose1))
				.setText(formatDose(result[SUN]));
		((TextView) findViewById(R.id.monDose1))
				.setText(formatDose(result[MON]));
		((TextView) findViewById(R.id.tueDose1))
				.setText(formatDose(result[TUE]));
		((TextView) findViewById(R.id.wedDose1))
				.setText(formatDose(result[WED]));
		((TextView) findViewById(R.id.thuDose1))
				.setText(formatDose(result[THU]));
		((TextView) findViewById(R.id.friDose1))
				.setText(formatDose(result[FRI]));
		((TextView) findViewById(R.id.satDose1))
				.setText(formatDose(result[SAT]));
		String totalWeeklyLowDose = String
				.valueOf(totalDose(result, tabletDose)) + " mg/wk";
		((TextView) findViewById(R.id.weeklyDose1)).setText(totalWeeklyLowDose);
		doseCalculator.setWeeklyDose(newHighEndWeeklyDose);
		result = doseCalculator.weeklyDoses();
		((TextView) findViewById(R.id.sunDose2))
				.setText(formatDose(result[SUN]));
		((TextView) findViewById(R.id.monDose2))
				.setText(formatDose(result[MON]));
		((TextView) findViewById(R.id.tueDose2))
				.setText(formatDose(result[TUE]));
		((TextView) findViewById(R.id.wedDose2))
				.setText(formatDose(result[WED]));
		((TextView) findViewById(R.id.thuDose2))
				.setText(formatDose(result[THU]));
		((TextView) findViewById(R.id.friDose2))
				.setText(formatDose(result[FRI]));
		((TextView) findViewById(R.id.satDose2))
				.setText(formatDose(result[SAT]));
		String totalWeeklyHighDose = String.valueOf(totalDose(result,
				tabletDose)) + " mg/wk";
		((TextView) findViewById(R.id.weeklyDose2))
				.setText(totalWeeklyHighDose);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	private final int SUN = 0;
	private final int MON = 1;
	private final int TUE = 2;
	private final int WED = 3;
	private final int THU = 4;
	private final int FRI = 5;
	private final int SAT = 6;

	private int lowEnd;
	private int highEnd;
	private Boolean increase;
	private double tabletDose;

	private double totalDose(double[] doses, double tabletDose) {
		double total = 0.0;
		for (int i = 0; i < doses.length; ++i)
			total = total + doses[i] * tabletDose;
		return total;
	}

	private String formatDose(double dose) {
		return String.valueOf(dose) + " tab";
	}

}
