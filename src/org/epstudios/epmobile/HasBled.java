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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;


public class HasBled extends EpActivity implements OnClickListener {
		@Override
		protected void onCreate(Bundle savedInstanceState)  {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.hasbled);
			
			View calculateButton = findViewById(R.id.calculate_button);
	        calculateButton.setOnClickListener(this);
	        View clearButton = findViewById(R.id.clear_button);
	        clearButton.setOnClickListener(this);
			
			checkBox = new CheckBox[9];
			
			checkBox[0] = (CheckBox) findViewById(R.id.hypertension);
			checkBox[1] = (CheckBox) findViewById(R.id.abnormal_renal_function);
			checkBox[2] = (CheckBox) findViewById(R.id.abnormal_liver_function);
			checkBox[3] = (CheckBox) findViewById(R.id.stroke);
			checkBox[4] = (CheckBox) findViewById(R.id.bleeding);
			checkBox[5] = (CheckBox) findViewById(R.id.labile_inr);
			checkBox[6] = (CheckBox) findViewById(R.id.age65);
			checkBox[7] = (CheckBox) findViewById(R.id.drug);
			checkBox[8] = (CheckBox) findViewById(R.id.etoh);
		}
		
		private CheckBox[] checkBox;
		
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.calculate_button:
				calculateResult();
				break;
			case R.id.clear_button:
				clearEntries();
				break;
			}
		}
		
		private void calculateResult() {
			int result = 0;
			for (int i = 0; i < checkBox.length; i++) {
				if (checkBox[i].isChecked())
					result++;
			}
			displayResult(result);
		}

		private void displayResult(int result) {
			Log.d("Result", "Result is " + result);
			AlertDialog dialog = new AlertDialog.Builder(this).create();
			String message;
			if (result < 3)
				message = getString(R.string.normal_hasbled);
			else
				message = getString(R.string.abnormal_hasbled);
			String risk = "";
			switch (result) {
			case 0:
			case 1:
				risk = "1.02-1.13";
				break;
			case 2:
				risk = "1.88";
				break;
			case 3:
				risk = "3.74";
				break;
			case 4:
				risk = "8.70";
				break;
			case 5:
				risk = "12.50";
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				risk = "> 12.50";
				break;
			}
			risk = "Bleeding risk is " + risk + " bleeds per 100 patient-years";
			
			dialog.setMessage("HAS-BLED score = " + result
					+ "\n" + message + "\n" + risk);
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Reset",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clearEntries();
						}
					});
			dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Don't Reset",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					});
			dialog.show();
		}
		
		private void clearEntries() {
			for (int i = 0; i < checkBox.length; i++)
				checkBox[i].setChecked(false);
		}

}
