package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class VtList extends EpActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionlist);
        setupInsets(R.id.my_root_view);
        initToolbar();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.vt_list, android.R.layout.simple_list_item_1);
        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            String selection = ((TextView) view).getText().toString();
            if (selection.equals(getString(R.string.epicardial_vt_title)))
                epicardialVt();
            else if (selection
                    .equals(getString(R.string.outflow_tract_vt_title)))
                outflowTractVt();
            else if (selection
                    .equals(getString(R.string.mitral_annular_vt_title)))
                mitralAnnularVt();
            else if (selection
                    .equals(getString(R.string.v2_transition_ratio_vt_title)))
                v2TransitionRatioVt();
        });
    }

    protected void outflowTractVt() {
        Intent i = new Intent(this, OutflowVt.class);
        startActivity(i);
    }

    protected void epicardialVt() {
        Intent i = new Intent(this, EpiVt.class);
        startActivity(i);
    }

    protected void mitralAnnularVt() {
        Intent i = new Intent(this, MitralAnnularVt.class);
        startActivity(i);
    }

    protected void v2TransitionRatioVt() {
        Intent i = new Intent(this, V2TransitionRatioVt.class);
        startActivity(i);
    }
}
