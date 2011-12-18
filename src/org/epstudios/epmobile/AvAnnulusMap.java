package org.epstudios.epmobile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class AvAnnulusMap extends EpActivity {
	private String location1, location2;
	private FrameLayout frame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageView background = new ImageView(this);
		background.setImageResource(R.drawable.avannulus);
		background.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		frame = new FrameLayout(this);
		frame.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		frame.setBackgroundColor(Color.WHITE);
		frame.addView(background);
		setContentView(frame);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String message = extras.getString("message");
			Toast.makeText(this, message, 5000).show();
			location1 = extras.getString("location1");
			location2 = extras.getString("location2");
			setApLocation(location1);
			setApLocation(location2);
		}
	}

	private void setApLocation(String location) {
		if (location.equals(""))
			return;
		int resource = 0;
		if (location.equals(WpwArruda.AS))
			resource = R.drawable.asap;
		else if (location.equals(WpwArruda.SUBEPI))
			resource = R.drawable.epicardialap;
		else if (location.equals(WpwArruda.LAL))
			resource = R.drawable.lalap;
		else if (location.equals(WpwArruda.LL))
			resource = R.drawable.llap;
		else if (location.equals(WpwArruda.LP))
			resource = R.drawable.lpap;
		else if (location.equals(WpwArruda.LPL))
			resource = R.drawable.lplap;
		else if (location.equals(WpwArruda.MSTA))
			resource = R.drawable.msap;
		else if (location.equals(WpwArruda.PSMA))
			resource = R.drawable.psmaap;
		else if (location.equals(WpwArruda.PSTA))
			resource = R.drawable.pstaap;
		else if (location.equals(WpwArruda.RA))
			resource = R.drawable.raap;
		else if (location.equals(WpwArruda.RAL))
			resource = R.drawable.ralap;
		else if (location.equals(WpwArruda.RL))
			resource = R.drawable.rlap;
		else if (location.equals(WpwArruda.RP))
			resource = R.drawable.rpap;
		else if (location.equals(WpwArruda.RPL))
			resource = R.drawable.rplap;

		if (resource == 0)
			return;
		ImageView foreground = new ImageView(this);
		foreground.setImageResource(resource);
		foreground.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		frame.addView(foreground);
	}
}
