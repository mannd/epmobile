package org.epstudios.epmobile;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AvAnnulusMap extends EpActivity {
	private String location1, location2;
	private FrameLayout frameLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avannulusmap);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String message = extras.getString("message");
			Toast.makeText(this, message, 5000).show();
			location1 = extras.getString("location1");
			location2 = extras.getString("location2");
			setForeground(location1);
			setForeground(location2);
		}
	}

	private void setForeground(String location) {
		if (location.equals(""))
			return;
		if (location.equals(WpwArruda.AS))
			this.setForeground("asap");

	}
}
