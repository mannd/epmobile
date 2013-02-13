package org.epstudios.epmobile;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AtrialTachLocalization extends EpActivity implements
		OnClickListener {
	private Button negButton;
	private Button posNegButton;
	private Button negPosButton;
	private Button isoButton;
	private Button posButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atlocalization);

		negButton = (Button) findViewById(R.id.neg_button);
		posNegButton = (Button) findViewById(R.id.pos_neg_button);
		negPosButton = (Button) findViewById(R.id.neg_pos_button);
		isoButton = (Button) findViewById(R.id.iso_button);
		posButton = (Button) findViewById(R.id.pos_button);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
