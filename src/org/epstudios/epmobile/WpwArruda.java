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

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WpwArruda extends EpActivity implements OnClickListener {
	private Button yesButton;
	private Button noButton;
	private Button backButton;
	private Button morphologyButton;
	private TextView stepTextView;
	private static int step = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simplealgorithm);

		yesButton = (Button) findViewById(R.id.yes_button);
		yesButton.setOnClickListener(this);
		noButton = (Button) findViewById(R.id.no_button);
		noButton.setOnClickListener(this);
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(this);
		morphologyButton = (Button) findViewById(R.id.morphology_button);
		morphologyButton.setVisibility(View.GONE); // maybe need to change this
													// to an instructions button
		stepTextView = (TextView) findViewById(R.id.stepTextView);

		step = 1; // needed to reset this when activity starts
		step1();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yes_button:
			// getYesResult();
			break;
		case R.id.no_button:
			// getNoResult();
			break;
		case R.id.back_button:
			step--;
			// gotoStep();
			break;
		}
	}

	private void step1() {
		backButton.setEnabled(false);
	}
}
