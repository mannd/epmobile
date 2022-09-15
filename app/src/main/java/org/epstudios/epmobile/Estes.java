package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.CheckBox;

public class Estes extends RiskScore {

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
    protected void calculateResult() {
        final int STRAIN_WITH_DIG = 2;
        final int STRAIN_WITHOUT_DIG = 1;
        if (checkBox[STRAIN_WITHOUT_DIG].isChecked()
                && checkBox[STRAIN_WITH_DIG].isChecked()) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setMessage(getString(R.string.estes_contradiction_message));
            dialog.setTitle(getString(R.string.error_dialog_title));
            dialog.show();
            return;
        }
        int result = 0;
        clearSelectedRisks();
        for (int i = 0; i < checkBox.length; i++) {
            if (checkBox[i].isChecked()) {
                addSelectedRisk(checkBox[i].getText().toString());
                final int INTRINSICOID = 6;
                final int QRS_DURATION = 5;
                final int LAD = 4;
                final int LAE = 3;
                final int VOLTAGE = 0;
                switch (i) {
                    case VOLTAGE:
                    case LAE:
                    case STRAIN_WITHOUT_DIG:
                        result += 3;
                        break;
                    case LAD:
                        result += 2;
                        break;
                    case STRAIN_WITH_DIG:
                    case QRS_DURATION:
                    case INTRINSICOID:
                        result++;
                }
            }
        }
        displayResult(getResultMessage(result),
                getString(R.string.estes_criteria_title));
    }

    private String getResultMessage(int result) {
        String message = getRiskLabel() + " = " + result + "\n";
        if (result < 4)
            message += getString(R.string.estes_no_lvh_message);
        else if (result == 4)
            message += getString(R.string.estes_probable_lvh_message);
        else
            message += getString(R.string.estes_definite_lvh_message);
        setResultMessage(message);
        // short reference not added here, is on the layout
        return message;

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.estes);

    }

    @Override
    protected void init() {
        checkBox = new CheckBox[7];

        checkBox[0] = findViewById(R.id.voltage);
        checkBox[1] = findViewById(R.id.strain_without_dig);
        checkBox[2] = findViewById(R.id.strain_with_dig);
        checkBox[3] = findViewById(R.id.lae);
        checkBox[4] = findViewById(R.id.lad);
        checkBox[5] = findViewById(R.id.qrs_duration);
        checkBox[6] = findViewById(R.id.intrinsicoid);

    }

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.estes_full_reference,
                R.string.estes_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.estes_criteria_label);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.estes_full_reference,
                R.string.estes_link);
    }

    @Override
    protected boolean hideKeyMenuItem() {
        return false;
    }

    @Override
    protected void showActivityKey() {
        showKeyAlertDialog(R.string.estes_key);
    }

}
