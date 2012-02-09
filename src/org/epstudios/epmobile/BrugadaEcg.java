package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BrugadaEcg extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brugadaecg);
		textButton = (Button) findViewById(R.id.text_button);
		textButton.setOnClickListener(this);
	}

	private Button textButton;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_button:
			displayHelp();
			break;
		}
	}

	private void displayHelp() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message = getString(R.string.brugada_ecg_description);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.brugada_ecg_title));
		dialog.show();
	}
}
