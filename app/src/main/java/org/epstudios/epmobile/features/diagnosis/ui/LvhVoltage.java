package org.epstudios.epmobile.features.diagnosis.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.data.Reference;
import org.epstudios.epmobile.core.ui.base.EpActivity;

public class LvhVoltage extends EpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherlvh);
        setupInsets(R.id.otherlvh_root_view);
        initToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this, LvhList.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {

        Reference[] references = new Reference[3];
        references[0] = new Reference(this, R.string.other_lvh_reference, R.string.other_lvh_link);
        references[1] = new Reference(this, R.string.peguero_reference_1, R.string.peguero_link_1);
        references[2] = new Reference(this, R.string.peguero_reference_2, R.string.peguero_link_2);

        showReferenceAlertDialog(references);
    }
}
