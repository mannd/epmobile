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

import java.util.HashSet;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class BrugadaMorphologyCriteria extends EpActivity implements
		OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wctmorphologycriteria);

		View calculateButton = findViewById(R.id.calculate_button);
		calculateButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

		bbbSpinner = (Spinner) findViewById(R.id.bbb_spinner);

		setAdapters();

		lbbbCheckBox = new CheckBox[4];
		lbbbCheckBox[0] = (CheckBox) findViewById(R.id.broad_r);
		lbbbCheckBox[1] = (CheckBox) findViewById(R.id.broad_rs);
		lbbbCheckBox[2] = (CheckBox) findViewById(R.id.notched_s);
		lbbbCheckBox[3] = (CheckBox) findViewById(R.id.lbbb_q_v6);

		rbbbCheckBox = new CheckBox[6];
		rbbbCheckBox[0] = (CheckBox) findViewById(R.id.monophasic_r_v1);
		rbbbCheckBox[1] = (CheckBox) findViewById(R.id.qr_v1);
		rbbbCheckBox[2] = (CheckBox) findViewById(R.id.rs_v1);
		rbbbCheckBox[3] = (CheckBox) findViewById(R.id.deep_s_v6);
		rbbbCheckBox[4] = (CheckBox) findViewById(R.id.rbbb_q_v6);
		rbbbCheckBox[5] = (CheckBox) findViewById(R.id.monophasic_r_v6);

		clearEntries();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this,
					WctAlgorithmList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private enum Bbb {
		LBBB, RBBB
	}

	private Spinner bbbSpinner;

	private OnItemSelectedListener itemListener;
	private CheckBox[] lbbbCheckBox;
	private CheckBox[] rbbbCheckBox;

	private void setAdapters() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.bbb_labels, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bbbSpinner.setAdapter(adapter);
		itemListener = new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				updateBbbSelection();
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}

		};

		bbbSpinner.setOnItemSelectedListener(itemListener);

	}

	@Override
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
		if (bothLeadsHaveEntries())
			displayVtResult();
		else
			displaySvtResult();
	}

	private Boolean bothLeadsHaveEntries() {
		Set<Integer> lbbbV1 = new HashSet<Integer>();
		Set<Integer> lbbbV6 = new HashSet<Integer>();
		Set<Integer> rbbbV1 = new HashSet<Integer>();
		Set<Integer> rbbbV6 = new HashSet<Integer>();
		lbbbV1.add(0);
		lbbbV1.add(1);
		lbbbV1.add(2);
		lbbbV6.add(3);
		rbbbV1.add(0);
		rbbbV1.add(1);
		rbbbV1.add(2);
		rbbbV6.add(3);
		rbbbV6.add(4);
		rbbbV6.add(5);
		Boolean inV1 = false;
		Boolean inV6 = false;
		for (int i = 0; i < lbbbCheckBox.length; i++) {
			if (lbbbCheckBox[i].isChecked() && lbbbV1.contains(i))
				inV1 = true;
			if (lbbbCheckBox[i].isChecked() && lbbbV6.contains(i))
				inV6 = true;
		}
		for (int i = 0; i < rbbbCheckBox.length; i++) {
			if (rbbbCheckBox[i].isChecked() && rbbbV1.contains(i))
				inV1 = true;
			if (rbbbCheckBox[i].isChecked() && rbbbV6.contains(i))
				inV6 = true;
		}
		return inV1 && inV6;
	}

	private void updateBbbSelection() {
		Bbb bbbSelection = getBbbSelection();
		if (bbbSelection.equals(Bbb.LBBB)) {
			hideRbbbEntries();
			showLbbbEntries();
		} else {
			hideLbbbEntries();
			showRbbbEntries();
		}
	}

	private void displayVtResult() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String sens = ".987";
		String spec = ".965";
		String message;
		message = getString(R.string.vt_result);
		message = message + " (Sens=" + sens + ", Spec=" + spec + ") ";
		message = message + getString(R.string.brugada_reference);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.wct_result_label));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Done",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Back",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						;
					}
				});
		dialog.show();
	}

	private void displaySvtResult() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message;
		message = getString(R.string.svt_result);
		message = message + " (Sens=.965, Spec=.967) ";
		message = message + getString(R.string.brugada_reference);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.wct_result_label));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Done",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Back",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						;
					}
				});
		dialog.show();
	}

	private Bbb getBbbSelection() {
		String result = bbbSpinner.getSelectedItem().toString();
		if (result.startsWith("R"))
			return Bbb.RBBB;
		else
			return Bbb.LBBB;
	}

	private void hideLbbbEntries() {
		hideEntries(lbbbCheckBox);
	}

	private void showLbbbEntries() {
		showEntries(lbbbCheckBox);
	}

	private void hideRbbbEntries() {
		hideEntries(rbbbCheckBox);
	}

	private void showRbbbEntries() {
		showEntries(rbbbCheckBox);
	}

	private void hideEntries(CheckBox[] cb) {
		clearEntries();
		for (int i = 0; i < cb.length; i++)
			cb[i].setVisibility(View.GONE);
	}

	private void showEntries(CheckBox[] cb) {
		clearEntries();
		for (int i = 0; i < cb.length; i++)
			cb[i].setVisibility(View.VISIBLE);
	}

	private void clearEntries() {
		for (int i = 0; i < lbbbCheckBox.length; i++)
			lbbbCheckBox[i].setChecked(false);
		for (int i = 0; i < rbbbCheckBox.length; i++)
			rbbbCheckBox[i].setChecked(false);
	}

}
