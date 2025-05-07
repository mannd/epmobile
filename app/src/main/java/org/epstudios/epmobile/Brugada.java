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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Brugada extends EpActivity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplealgorithm);
        setupInsets(R.id.simplealgorithm_root_view);
        initToolbar();

        Button yesButton = findViewById(R.id.yes_button);
        yesButton.setOnClickListener(this);
        Button noButton = findViewById(R.id.no_button);
        noButton.setOnClickListener(this);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        morphologyButton = findViewById(R.id.morphology_button);
        morphologyButton.setOnClickListener(this);
        morphologyButton.setVisibility(View.GONE);
        stepTextView = findViewById(R.id.stepTextView);

        step = 1; // needed to reset this when activity starts
        step1();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this,
                    WctAlgorithmList.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.yes_button) {
            displayVtResult(step);
        } else if (id == R.id.no_button) {
            getNoResult();
        } else if (id == R.id.back_button) {
            step--;
            gotoStep();
        } else if (id == R.id.morphology_button) {
            displayMorphologyCriteria();
        }
    }

    private void step1() {
        stepTextView.setText(getString(R.string.brugada_step_1));
        backButton.setEnabled(false);
    }

    private void step2() {
        stepTextView.setText(getString(R.string.brugada_step_2));
        backButton.setEnabled(true);
    }

    private void step3() {
        stepTextView.setText(getString(R.string.brugada_step_3));
        backButton.setEnabled(true);
    }

    private void step4() {
        stepTextView.setText(getString(R.string.brugada_step_4));
        backButton.setEnabled(true);
    }

    private void getNoResult() {
        switch (step) {
            case 1:
            case 2:
            case 3:
                step++;
                gotoStep();
                break;
            case 4:
                displaySvtResult();
                break;
        }

    }

    private void gotoStep() {
        if (step < 4)
            morphologyButton.setVisibility(View.GONE);
        else
            morphologyButton.setVisibility(View.VISIBLE);
        switch (step) {
            case 1:
                step1();
                break;
            case 2:
                step2();
                break;
            case 3:
                step3();
                break;
            case 4:
                step4();
                break;
        }
    }

    private void displayVtResult(int step) {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        String sens = "";
        String spec = "";
        switch (step) {
            case 1:
            case 2:
                sens = ".21";
                spec = "1.0";
                break;
            case 3:
                sens = ".82";
                spec = ".98";
                break;
            case 4:
                sens = ".987";
                spec = ".965";
                break;
        }
        String message;
        message = getString(R.string.vt_result);
        message = message + " (Sens=" + sens + ", Spec=" + spec + ") ";
        dialog.setMessage(message);
        dialog.setTitle(getString(R.string.wct_result_label));
        dialog.setCancelable(false);
        dialog.setPositiveButton("Done",
                (dialog12, which) -> finish());
        dialog.setNegativeButton("Back",
                (dialog1, which) -> {
                });
        dialog.show();
    }

    private void displaySvtResult() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        String message;
        message = getString(R.string.svt_result);
        message = message + " (Sens=.965, Spec=.967) ";
        dialog.setMessage(message);
        dialog.setTitle(getString(R.string.wct_result_label));
        dialog.setPositiveButton("Done",
                (dialog12, which) -> finish());
        dialog.setNegativeButton("Back",
                (dialog1, which) -> {
                });
        dialog.show();
    }

    private void displayMorphologyCriteria() {
        Intent i = new Intent(this, BrugadaMorphologyCriteria.class);
        startActivity(i);
    }

    private Button backButton;
    private Button morphologyButton;
    private TextView stepTextView;
    private static int step = 1;

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.brugada_wct_reference,
                R.string.brugada_wct_link);
    }

}
