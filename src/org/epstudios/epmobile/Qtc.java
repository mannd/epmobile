package org.epstudios.epmobile;

import org.epstudios.epmobile.QtcCalculator.QtcFormula;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Qtc extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qtc);
		
		View calculateQtcButton = findViewById(R.id.calculate_qtc_button);
        calculateQtcButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);
        
        qtcTextView = (TextView) findViewById(R.id.calculated_qtc);
        rrEditText = (EditText) findViewById(R.id.rrEditText);
        qtEditText = (EditText) findViewById(R.id.qtEditText);
	}
	
	private TextView qtcTextView;
	private EditText rrEditText;
	private EditText qtEditText;

	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calculate_qtc_button:
			calculateQtc();
			break;
		case R.id.clear_button:
			clearEntries();
			break;
		}
	}
	
	private final static int QTC_UPPER_LIMIT = 440;
	
	private void calculateQtc() {
		CharSequence rrText = rrEditText.getText();
		CharSequence qtText = qtEditText.getText();
		try {
			int rr = Integer.parseInt(rrText.toString());
			int qt = Integer.parseInt(qtText.toString());
			int qtc = QtcCalculator.calculate(rr, qt, QtcFormula.BAZETT);
			qtcTextView.setText("QTc = " + String.valueOf(qtc) + " msec");
			if (qtc >= QTC_UPPER_LIMIT)
				qtcTextView.setTextColor(Color.RED);
			else
				qtcTextView.setTextColor(Color.GREEN);
		}
		catch (NumberFormatException e) {	
			qtcTextView.setText("Invalid!");
			qtcTextView.setTextColor(Color.RED);
		}		
	}		
	
	
	private void clearEntries() {
		rrEditText.setText(null);
		qtEditText.setText(null);
		qtcTextView.setText(getString(R.string.qtc_result_label));
		qtcTextView.setTextColor(Color.LTGRAY);
		rrEditText.requestFocus();
	}
	
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	super.onCreateOptionsMenu(menu);
	    	MenuInflater inflater = getMenuInflater();
	    	inflater.inflate(R.menu.menu, menu);
	    	return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
	    	case R.id.settings:
	    		startActivity(new Intent(this, Prefs.class));
	    		return true;
	    	}
	    	return false;
	    }
		

}
