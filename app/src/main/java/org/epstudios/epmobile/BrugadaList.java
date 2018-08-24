package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 9/27/17.
 * <p>
 * This file is part of epmobile.
 * <p>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */

public class BrugadaList extends EpActivity {

    	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionlist);
        initToolbar();
	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.brugada_list, android.R.layout.simple_list_item_1);
        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = ((TextView) view).getText().toString();
				if (selection
						.equals(getString(R.string.brugada_ecg_title)))
					brugadaEcg();
				else if (selection
						.equals(getString(R.string.brugada_diagnosis_title)))
					brugadaDiagnosis();
				else if (selection
						.equals(getString(R.string.brugada_score_title)))
					brugadaScore();

			}
		});
	}

	private void brugadaEcg() {
		Intent i = new Intent(this, BrugadaEcg.class);
		startActivity(i);
	}

   private void brugadaDiagnosis() {
		Intent i = new Intent(this, LinkView.class);
        i.putExtra("EXTRA_URL", "file:///android_asset/brugadadiagnosis.html");
        i.putExtra("EXTRA_TITLE", getString((R.string.brugada_diagnosis_title)));
        startActivity(i);
	}

	private void brugadaScore() {
		Intent i = new Intent(this, BrugadaScore.class);
		startActivity(i);
	}

}
