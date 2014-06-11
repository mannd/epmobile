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

import org.epstudios.epmobile.R.string;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.CheckBox;

public abstract class RiskScore extends DiagnosticScore {

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
				getString(string.reset_label, this),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearEntries();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
				getString(string.dont_reset_label, this),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(string.copy_report_label, this),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// clipboard handled differently depending on Android
						// version
						String textToCopy = getFullRiskReport();
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
		report += getRiskTitle() + "\nResult: ";
		report += getResult() + "\n" + getString(string.reference_label, this)
				+ ": ";
		report += getFullReference() + "\n";
		return report;
	}

	// each risk score needs these
	abstract protected String getFullReference();

	abstract protected String getResult();

	abstract protected String getRiskTitle();

	abstract protected String getShortReference();

	abstract protected String getSelectedRisks();
}
