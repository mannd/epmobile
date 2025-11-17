package org.epstudios.epmobile.features.diagnosis.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.ui.base.EpActivity;

public class LongQtEcg extends EpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.longqtecg);
        setupInsets(R.id.longqtecg_root_view);
        initToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this, LongQtList.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
