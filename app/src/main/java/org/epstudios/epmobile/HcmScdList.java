package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.epstudios.epmobile.core.ui.base.EpActivity;

/**
 * Copyright (C) 2024 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 11/4/24.
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
public class HcmScdList extends EpActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionlist);
        setupInsets(R.id.selection_list_root_view);
        initToolbar();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.hcm_scd_risk_scores, android.R.layout.simple_list_item_1);
        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            String selection = ((TextView) view).getText().toString();
            if (selection
                    .equals(getString(R.string.hcm_title))) {
               hcmScd2002();
            }

            else if (selection
                    .equals(getString(R.string.hcm_scd_esc_score_title))) {
               hcmScd2014();
            }
            else if (selection.equals(getString(R.string.hcm_scd_2022_title))) {
                hcmScd2022();
            }
            else if (selection.equals(getString(R.string.hcm_scd_2024_title))) {
                hcmScd2024();
            }
        });
    }

    private void hcmScd2024() {
        Intent i = new Intent(this, HcmScd2024.class);
        startActivity(i);
    }

    private void hcmScd2002() {
        Intent i = new Intent(this, HcmScd2002.class);
        startActivity(i);
    }

    private void hcmScd2014() {
        Intent i = new Intent(this, HcmRiskScd.class);
        startActivity(i);
    }

    private void hcmScd2022() {
        Intent i = new Intent(this, HcmScd2022.class);
        startActivity(i);
    }
}
