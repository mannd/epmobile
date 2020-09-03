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
import androidx.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DoseTable extends EpActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosetable);
	initToolbar();
	
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		int lowEnd = prefs.getInt("lowEnd", 0);
		int highEnd = prefs.getInt("highEnd", 0);
		Boolean increase = prefs.getBoolean("increase", true);
		double tabletDose = prefs.getFloat("tabletDose", 0);
		double weeklyDose = prefs.getFloat("weeklyDose", 0);

		this.setTitle(getString(R.string.warfarin_dose_title, String.valueOf(tabletDose)));
		TextView percent1TextView = findViewById(R.id.percent1);
		String changeDirection = increase ? getString(R.string.increase) : getString(R.string.decrease);
		percent1TextView.setText(getString(R.string.dose_table_dose_change, String.valueOf(lowEnd), changeDirection));
		TextView percent2TextView = findViewById(R.id.percent2);
		percent2TextView.setText(getString(R.string.dose_table_dose_change, String.valueOf(highEnd), changeDirection));

		double newLowEndWeeklyDose = Warfarin.getNewDoseFromPercentage(
				lowEnd / 100.0, weeklyDose, increase);
		double newHighEndWeeklyDose = Warfarin.getNewDoseFromPercentage(
				highEnd / 100.0, weeklyDose, increase);
		DoseCalculator doseCalculator = new DoseCalculator(tabletDose,
				newLowEndWeeklyDose);
		double[] result = doseCalculator.weeklyDoses();
		int SUN = 0;
		((TextView) findViewById(R.id.sunDose1))
				.setText(formatDose(result[SUN]));
		int MON = 1;
		((TextView) findViewById(R.id.monDose1))
				.setText(formatDose(result[MON]));
		int TUE = 2;
		((TextView) findViewById(R.id.tueDose1))
				.setText(formatDose(result[TUE]));
		int WED = 3;
		((TextView) findViewById(R.id.wedDose1))
				.setText(formatDose(result[WED]));
		int THU = 4;
		((TextView) findViewById(R.id.thuDose1))
				.setText(formatDose(result[THU]));
		int FRI = 5;
		((TextView) findViewById(R.id.friDose1))
				.setText(formatDose(result[FRI]));
		int SAT = 6;
		((TextView) findViewById(R.id.satDose1))
				.setText(formatDose(result[SAT]));
		String totalWeeklyLowDose = totalDose(result, tabletDose) + " mg/wk";
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
		String totalWeeklyHighDose = totalDose(result,
				tabletDose) + " mg/wk";
		((TextView) findViewById(R.id.weeklyDose2))
				.setText(totalWeeklyHighDose);

	}

	private double totalDose(double[] doses, double tabletDose) {
		double total = 0.0;
		for (double dose : doses) total = total + dose * tabletDose;
		return total;
	}

	private String formatDose(double dose) {
		return dose + " tab";
	}

}
