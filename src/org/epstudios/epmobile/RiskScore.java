package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public abstract class RiskScore extends EpActivity implements OnClickListener {

	protected CheckBox[] checkBox;
	
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
	
	abstract protected void calculateResult();
	
	protected void displayResult(int result) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message = getResultMessage(result);
		dialog.setMessage(message);
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
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.setTitle(getDialogTitle());
		dialog.show();
	}
	
	abstract protected String getResultMessage(int result);
	
	abstract protected String getDialogTitle();
	
	protected void clearEntries() {
		for (int i = 0; i < checkBox.length; i++)
			checkBox[i].setChecked(false);
	}


}
