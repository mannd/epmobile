/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2011 EP Studios, Inc.
    www.epstudiossoftware.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WctAlgorithmList extends EpActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionlist);
        setupInsets(R.id.my_root_view);
        initToolbar();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.wct_algorithm_list,
                android.R.layout.simple_list_item_1);
        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            String selection = ((TextView) view).getText().toString();
            if (selection.equals(getString(R.string.brugada_wct_title)))
                brugadaAlgorithm();
            else if (selection.equals(getString(R.string.morphology_title)))
                morphologyCriteria();
            else if (selection.equals(getString(R.string.rwpt_title)))
                ultraSimpleBrugadaAlgorithm();
            else if (selection.equals(getString(R.string.vereckei_title)))
                vereckeiAlgorithm();
        });
    }

    private void brugadaAlgorithm() {
        Intent i = new Intent(this, Brugada.class);
        startActivity(i);
    }

    private void morphologyCriteria() {
        Intent i = new Intent(this, WctMorphologyCriteria.class);
        startActivity(i);
    }

    private void ultraSimpleBrugadaAlgorithm() {
        Intent i = new Intent(this, Rwpt.class);
        startActivity(i);
    }

    private void vereckeiAlgorithm() {
        Intent i = new Intent(this, Vereckei.class);
        startActivity(i);
    }

}
