package org.epstudios.epmobile.core.ui.base;

import android.content.Intent;
import android.view.MenuItem;

import org.epstudios.epmobile.features.riskscores.ui.SyncopeRiskScoreList;

public abstract class SyncopeRiskScore extends RiskScore {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this,
                    SyncopeRiskScoreList.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
