package org.epstudios.epmobile.features.diagnosis.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.ui.base.EpActivity;

public class EpiVt extends EpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epivt);
        setupInsets(R.id.epivt_root_view);
        initToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this, VtList.class);
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
            showReferenceAlertDialog(R.string.epivt_reference,
                    R.string.epivt_link);
        }
}
