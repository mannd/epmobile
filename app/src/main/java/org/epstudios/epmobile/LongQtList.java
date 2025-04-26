/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2012 EP Studios, Inc.
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

public class LongQtList extends EpActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionlist);
        setupInsets(R.id.selection_list_root_view);
        initToolbar();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.lqts_list, android.R.layout.simple_list_item_1);
        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            String selection = ((TextView) view).getText().toString();
            if (selection
                    .equals(getString(R.string.long_qt_syndrome_diagnosis_title)))
                lqtsDiagnosis();

            else if (selection
                    .equals(getString(R.string.lqt_subtypes_title)))
                lqtSubtypes();
            else if (selection.equals(getString(R.string.lqt_ecg_title)))
                lqtEcg();
            else if (selection.equals(getString(R.string.long_qt_table_title)))
                lqtTable();
            // lqtDrugs here
        });
    }

    private void lqtsDiagnosis() {
        Intent i = new Intent(this, LongQt.class);
        startActivity(i);
    }

    private void lqtSubtypes() {
        Intent i = new Intent(this, LongQtSubtypes.class);
        startActivity(i);
    }

    private void lqtEcg() {
        Intent i = new Intent(this, LongQtEcg.class);
        startActivity(i);
    }

    private void lqtTable() {
        Intent i = new Intent(this, LongQtTable.class);
        startActivity(i);
    }

}
