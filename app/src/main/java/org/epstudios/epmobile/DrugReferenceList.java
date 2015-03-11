package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 3/11/15.
 * <p/>
 * This file is part of EP Mobile.
 * <p/>
 * EP Coding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * EP Coding is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with EP Coding.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Note also:
 * <p/>
 * CPT copyright 2012 American Medical Association. All rights
 * reserved. CPT is a registered trademark of the American Medical
 * Association.
 * <p/>
 * A limited number of CPT codes are used in this program under the Fair Use
 * doctrine of US Copyright Law.  See README.md for more information.
 */
public class DrugReferenceList extends EpReferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selectionlist);
        super.onCreate(savedInstanceState);
        // right now reuse same array used by drug calculators, BUT
        // if we add more drugs (like amiodarone) will need to create separate array.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.drug_calculator_list,
                android.R.layout.simple_list_item_1);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selection = ((TextView) view).getText().toString();
                if (selection
                        .equals(getString(R.string.dabigatran_calculator_title)))
                    dabigatranReference();
                else if (selection
                        .equals(getString(R.string.dofetilide_calculator_title)))
                    dofetilideReference();
                else if (selection
                        .equals(getString(R.string.rivaroxaban_calculator_title)))
                    rivaroxabanReference();
                else if (selection.equals(getString(R.string.warfarin_title)))
                    warfarinReference();
                else if (selection
                        .equals(getString(R.string.sotalol_calculator_title)))
                    sotalolReference();
                else if (selection
                        .equals(getString(R.string.apixaban_calculator_title)))
                    apixabanReference();
                else if (selection
                        .equals(getString(R.string.edoxaban_calculator_title)))
                    edoxabanReference();

            }
        });
    }

    private void dabigatranReference() {
        Intent i = new Intent(this, RvaVsRvbPacing.class);
        startActivity(i);
    }

    private void dofetilideReference() {
        Intent i = new Intent(this, DrugReference.class);
        startActivity(i);
    }

    private void rivaroxabanReference() {
        Intent i = new Intent(this, DrugReference.class);
        startActivity(i);
    }

    private void warfarinReference() {
        Intent i = new Intent(this, DrugReference.class);
        startActivity(i);
    }

    private void sotalolReference() {
        Intent i = new Intent(this, DrugReference.class);
        startActivity(i);
    }

    private void apixabanReference() {
        Intent i = new Intent(this, Apixaban.class);
        startActivity(i);
    }

    private void edoxabanReference() {
        Intent i = new Intent(this, DrugReference.class);
        startActivity(i);
    }

}
