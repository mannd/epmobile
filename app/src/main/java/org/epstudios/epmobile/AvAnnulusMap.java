package org.epstudios.epmobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class AvAnnulusMap extends EpActivity {
	private String location1, location2;
	private FrameLayout frame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.avannulusmap);
		super.onCreate(savedInstanceState);
		ImageView background = (ImageView) findViewById(R.id.avannulus_image);
		background.setImageResource(R.drawable.modgrayavannulus);
//		background.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT));
//
		frame = (FrameLayout) findViewById(R.id.avannulus_frame);
//		frame.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT));
		frame.setBackgroundColor(Color.WHITE);
//		frame.addView(background);
//		setContentView(frame);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String message = extras.getString("message");
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			location1 = extras.getString("location1");
			location2 = extras.getString("location2");
			setApLocation(location1);
			setApLocation(location2);
		} else
			setTitle(getString(R.string.anatomy_av_annulus_title));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this,
					WpwAlgorithmList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
