package org.epstudios.epmobile;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.Button;
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
    		// more buttons here
    	}
    }
    	
    
    private void about() {
   		Intent i = new Intent(this, About.class);
		startActivity(i);
    }
    
    private void dofetilideCalculator() {
    	;
    }
}