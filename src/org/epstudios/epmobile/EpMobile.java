package org.epstudios.epmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class EpMobile extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        View dofetilideButton = findViewById(R.id.dofetilide_button);
        dofetilideButton.setOnClickListener(this);
        View qtcButton = findViewById(R.id.qtc_button);
        qtcButton.setOnClickListener(this);
        View intervalRateButton = findViewById(R.id.cycle_length_button);
        intervalRateButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
    }
    
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.about_button:
    		about();
    		break;
    	case R.id.dofetilide_button:
    		dofetilideCalculator();
    		break;
    	case R.id.qtc_button:
    		qtcCalculator();
    		break;
    	case R.id.cycle_length_button:
    		intervalRateCalculator();
    		break;
    		// more buttons here
    	}
    }
    	
    
    private void about() {
   		Intent i = new Intent(this, About.class);
		startActivity(i);
    }
    
    private void dofetilideCalculator() {
    	Intent i = new Intent(this, Dofetilide.class);
    	startActivity(i);
    }
    
    private void qtcCalculator() {
    	Intent i = new Intent(this, Qtc.class);
    	startActivity(i);
    }
    
    private void intervalRateCalculator() {
    	Intent i = new Intent(this, CycleLength.class);
    	startActivity(i);
    }
}