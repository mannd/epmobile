package org.epstudios.epmobile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class BrugadaMorphologyCriteria extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wctmorphologycriteria);
		
		View calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);
		
        bbbSpinner = (Spinner) findViewById(R.id.bbb_spinner);
        
        setAdapters();
        
        lbbbCheckBox = new CheckBox[4];
        lbbbCheckBox[0] = (CheckBox) findViewById(R.id.broad_r);
        lbbbCheckBox[1] = (CheckBox) findViewById(R.id.broad_rs);
        lbbbCheckBox[2] = (CheckBox) findViewById(R.id.notched_s);
        lbbbCheckBox[3] = (CheckBox) findViewById(R.id.lbbb_q_v6);
        
        rbbbCheckBox = new CheckBox[6];
        rbbbCheckBox[0] = (CheckBox) findViewById(R.id.monophasic_r_v1);
        rbbbCheckBox[1] = (CheckBox) findViewById(R.id.qr_v1);
        rbbbCheckBox[2] = (CheckBox) findViewById(R.id.rs_v1);
        rbbbCheckBox[3] = (CheckBox) findViewById(R.id.deep_s_v6);
        rbbbCheckBox[4] = (CheckBox) findViewById(R.id.rbbb_q_v6);
        rbbbCheckBox[5] = (CheckBox) findViewById(R.id.monophasic_r_v6);
        
        clearEntries();
	}
	
	private enum Bbb {LBBB, RBBB}
	
	private Spinner bbbSpinner;
	
	private OnItemSelectedListener itemListener;
	private CheckBox[] lbbbCheckBox;
	private CheckBox[] rbbbCheckBox;
	
//	private HashSet<int> lbbbV1Entries;
//	
//	private int[] lbbbV6Entries;
//	private int[] rbbbV1Entries;
//	private int[] rbbbV6Entries;

	
	private void setAdapters() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.bbb_labels, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bbbSpinner.setAdapter(adapter);
		itemListener = new OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View v,
					int position, long id) {
				updateBbbSelection();
			}
			public void onNothingSelected(AdapterView parent) {
				// do nothing
			}
		
		};
		
		bbbSpinner.setOnItemSelectedListener(itemListener);

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
	
	private void calculateResult() {
		// TODO
	}
	
	private void updateBbbSelection() {
		Bbb bbbSelection = getBbbSelection();
		if (bbbSelection.equals(Bbb.LBBB)) {
			hideRbbbEntries();
			showLbbbEntries();
		}
		else {
			hideLbbbEntries();
			showRbbbEntries();
		}
	}
	
	private Bbb getBbbSelection() {
		String result = bbbSpinner.getSelectedItem().toString();
		if (result.startsWith("R"))
			return Bbb.RBBB;
		else
			return Bbb.LBBB;
	}
	
	private void hideLbbbEntries() {
		hideEntries(lbbbCheckBox);
	}
	
	private void showLbbbEntries() {
		showEntries(lbbbCheckBox);
	}
	
	private void hideRbbbEntries() {
		hideEntries(rbbbCheckBox);
	}
	
	private void showRbbbEntries() {
		showEntries(rbbbCheckBox);
	}
	
	private void hideEntries(CheckBox[] cb) {
		clearEntries();
		for (int i = 0; i < cb.length; i++)
			cb[i].setVisibility(View.GONE);
	}
	
	private void showEntries(CheckBox[] cb) {
		clearEntries();
		for (int i = 0; i < cb.length; i++)
			cb[i].setVisibility(View.VISIBLE);
	}
	
	private void clearEntries() {
		for (int i = 0; i < lbbbCheckBox.length; i++)
			lbbbCheckBox[i].setChecked(false);
		for (int i = 0; i < rbbbCheckBox.length; i++)
			rbbbCheckBox[i].setChecked(false);
	}


}
