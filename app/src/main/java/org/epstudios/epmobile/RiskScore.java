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

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

public abstract class RiskScore extends DiagnosticScore {
	private String resultMessage;
	private final List<String> selectedRisks = new ArrayList<String>();

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, RiskScoreList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void clearEntries() {
		for (int i = 0; i < checkBox.length; i++)
			checkBox[i].setChecked(false);
	}

	protected CheckBox[] checkBox;

	@Override
	@SuppressWarnings("deprecation")
	protected void displayResult(String message, String title) {
		// put message in class field so inner class can use
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setMessage(message);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.reset_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearEntries();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
				getString(R.string.dont_reset_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(R.string.copy_report_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// clipboard handled differently depending on Android
						// version
						String textToCopy = getFullRiskReport();
						showToast();
						int sdk = android.os.Build.VERSION.SDK_INT;
						if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
							android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
							clipboard.setText(textToCopy);
						} else {
							android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
							android.content.ClipData clip = android.content.ClipData
									.newPlainText("Copied Text", textToCopy);
							clipboard.setPrimaryClip(clip);
						}
					}
				});
		dialog.setTitle(title);
		dialog.show();
	}

	private String getFullRiskReport() {
		String report = "Risk score: ";
		report += getRiskLabel() + "\nRisks: ";
		report += getSelectedRisks() + "\nResult: ";
		report += getResultMessage() + "\n"
				+ getString(R.string.reference_label) + ": ";
		report += getFullReference() + "\n";
		return report;
	}

	private void showToast() {
		Toast.makeText(this, "Result copied to clipboard", Toast.LENGTH_SHORT)
				.show();
	}

	protected void setResultMessage(String message) {
		resultMessage = message;
	}

	protected String getResultMessage() {
		return resultMessage;
	}

	protected String resultWithShortReference() {
		return getResultMessage() + "\n" + getString(R.string.reference_label)
				+ ": " + getShortReference() + ".";
	}

	protected void clearSelectedRisks() {
		selectedRisks.clear();
	}

	protected void addSelectedRisk(String risk) {
		selectedRisks.add(risk);
	}

	protected String getSelectedRisks() {
		if (selectedRisks.isEmpty()) {
			return getString(R.string.none_label);
		} else {
			return selectedRisks.toString();
		}
	}

	// each risk score needs these
	abstract protected String getFullReference();

	// this is the R.string.risk_label, not risk_title. No "score" attached
	abstract protected String getRiskLabel();

	abstract protected String getShortReference();
}
