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
	private FrameLayout frame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.avannulusmap);
	initToolbar();
	ImageView background = findViewById(R.id.avannulus_image);
		background.setImageResource(R.drawable.modgrayavannulus);
		frame = findViewById(R.id.avannulus_frame);
		frame.setBackgroundColor(Color.WHITE);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String message = extras.getString("message");
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			String location1 = extras.getString("location1");
			String location2 = extras.getString("location2");
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
		switch (location) {
			case WpwArruda.AS:
				resource = R.drawable.asap;
				break;
			case WpwArruda.SUBEPI:
				resource = R.drawable.epicardialap;
				break;
			case WpwArruda.LAL:
				resource = R.drawable.lalap;
				break;
			case WpwArruda.LL:
				resource = R.drawable.llap;
				break;
			case WpwArruda.LP:
				resource = R.drawable.lpap;
				break;
			case WpwArruda.LPL:
				resource = R.drawable.lplap;
				break;
			case WpwArruda.MSTA:
				resource = R.drawable.msap;
				break;
			case WpwArruda.PSMA:
				resource = R.drawable.psmaap;
				break;
			case WpwArruda.PSTA:
				resource = R.drawable.pstaap;
				break;
			case WpwArruda.RA:
				resource = R.drawable.raap;
				break;
			case WpwArruda.RAL:
				resource = R.drawable.ralap;
				break;
			case WpwArruda.RL:
				resource = R.drawable.rlap;
				break;
			case WpwArruda.RP:
				resource = R.drawable.rpap;
				break;
			case WpwArruda.RPL:
				resource = R.drawable.rplap;
				break;
		}

		if (resource == 0)
			return;
		ImageView foreground = new ImageView(this);
		foreground.setImageResource(resource);
		foreground.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		frame.addView(foreground);
	}
}
