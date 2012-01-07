package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public abstract class RiskScore extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();

		View calculateButton = findViewById(R.id.calculate_button);
		calculateButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

		init();

		clearEntries();
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

	abstract protected void calculateResult();

	abstract protected void setContentView();

	abstract protected void init();

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

	protected CheckBox[] checkBox;

}
