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
 * EP Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * EP Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with EP Mobile.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 */


public class DrugReferenceList extends EpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selectionlist);
        super.onCreate(savedInstanceState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.drug_reference_list,
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
                        .equals(getString(R.string.dabigatran)))
                    dabigatranReference();
                else if (selection
                        .equals(getString(R.string.dofetilide)))
                    dofetilideReference();
                else if (selection
                        .equals(getString(R.string.rivaroxaban)))
                    rivaroxabanReference();
                else if (selection
                        .equals(getString(R.string.sotalol)))
                    sotalolReference();
                else if (selection
                        .equals(getString(R.string.apixaban)))
                    apixabanReference();
                else if (selection
                        .equals(getString(R.string.edoxaban)))
                    edoxabanReference();

            }
        });
    }

    private void dabigatranReference() {
        Intent i = new Intent(this, LinkView.class);
        i.putExtra("EXTRA_URL", "file:///android_asset/dabigatran.html");
        i.putExtra("EXTRA_TITLE", getString(R.string.dabigatran));
        i.putExtra("EXTRA_SHOW_BUTTON", true);
        startActivity(i);
    }

    private void dofetilideReference() {
        Intent i = new Intent(this, LinkView.class);
        i.putExtra("EXTRA_URL", "file:///android_asset/dofetilide.html");
        i.putExtra("EXTRA_TITLE", getString(R.string.dofetilide));
        i.putExtra("EXTRA_SHOW_BUTTON", true);
        startActivity(i);
    }

    private void rivaroxabanReference() {
        Intent i = new Intent(this, LinkView.class);
        i.putExtra("EXTRA_URL", "file:///android_asset/rivaroxaban.html");
        i.putExtra("EXTRA_TITLE", getString(R.string.rivaroxaban));
        i.putExtra("EXTRA_SHOW_BUTTON", true);
        startActivity(i);
    }

    private void sotalolReference() {
        Intent i = new Intent(this, LinkView.class);
        i.putExtra("EXTRA_URL", "file:///android_asset/sotalol.html");
        i.putExtra("EXTRA_TITLE", getString(R.string.sotalol));
        i.putExtra("EXTRA_SHOW_BUTTON", true);
        startActivity(i);
    }

    private void apixabanReference() {
        Intent i = new Intent(this, LinkView.class);
        i.putExtra("EXTRA_URL", "file:///android_asset/apixaban.html");
        i.putExtra("EXTRA_TITLE", getString(R.string.apixaban));
        i.putExtra("EXTRA_SHOW_BUTTON", true);
        startActivity(i);
    }

    private void edoxabanReference() {
        Intent i = new Intent(this, LinkView.class);
        i.putExtra("EXTRA_URL", "file:///android_asset/edoxaban.html");
        i.putExtra("EXTRA_TITLE", getString(R.string.edoxaban));
        i.putExtra("EXTRA_SHOW_BUTTON", true);
        startActivity(i);
    }

}
