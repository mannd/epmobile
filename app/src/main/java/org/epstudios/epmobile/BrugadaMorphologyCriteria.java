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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashSet;
import java.util.Set;

public class BrugadaMorphologyCriteria extends EpActivity implements
        OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wctmorphologycriteria);
        setupInsets(R.id.wct_morphology_criteria_root_view);
        initToolbar();

        View calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        bbbSpinner = findViewById(R.id.bbb_spinner);

        setAdapters();

        lbbbCheckBox = new CheckBox[4];
        lbbbCheckBox[0] = findViewById(R.id.broad_r);
        lbbbCheckBox[1] = findViewById(R.id.broad_rs);
        lbbbCheckBox[2] = findViewById(R.id.notched_s);
        lbbbCheckBox[3] = findViewById(R.id.lbbb_q_v6);

        rbbbCheckBox = new CheckBox[6];
        rbbbCheckBox[0] = findViewById(R.id.monophasic_r_v1);
        rbbbCheckBox[1] = findViewById(R.id.qr_v1);
        rbbbCheckBox[2] = findViewById(R.id.rs_v1);
        rbbbCheckBox[3] = findViewById(R.id.deep_s_v6);
        rbbbCheckBox[4] = findViewById(R.id.rbbb_q_v6);
        rbbbCheckBox[5] = findViewById(R.id.monophasic_r_v6);

        clearEntries();
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

    private enum Bbb {
        LBBB, RBBB
    }

    private Spinner bbbSpinner;

    private CheckBox[] lbbbCheckBox;
    private CheckBox[] rbbbCheckBox;

    private void setAdapters() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.bbb_labels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bbbSpinner.setAdapter(adapter);
        OnItemSelectedListener itemListener = new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                updateBbbSelection();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }

        };

        bbbSpinner.setOnItemSelectedListener(itemListener);

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculateResult();
        } else if (id == R.id.clear_button) {
            clearEntries();
        }
    }

    private void calculateResult() {
        if (bothLeadsHaveEntries())
            displayVtResult();
        else
            displaySvtResult();
    }

    private Boolean bothLeadsHaveEntries() {
        Set<Integer> lbbbV1 = new HashSet<>();
        Set<Integer> lbbbV6 = new HashSet<>();
        Set<Integer> rbbbV1 = new HashSet<>();
        Set<Integer> rbbbV6 = new HashSet<>();
        lbbbV1.add(0);
        lbbbV1.add(1);
        lbbbV1.add(2);
        lbbbV6.add(3);
        rbbbV1.add(0);
        rbbbV1.add(1);
        rbbbV1.add(2);
        rbbbV6.add(3);
        rbbbV6.add(4);
        rbbbV6.add(5);
        boolean inV1 = false;
        boolean inV6 = false;
        for (int i = 0; i < lbbbCheckBox.length; i++) {
            if (lbbbCheckBox[i].isChecked() && lbbbV1.contains(i))
                inV1 = true;
            if (lbbbCheckBox[i].isChecked() && lbbbV6.contains(i))
                inV6 = true;
        }
        for (int i = 0; i < rbbbCheckBox.length; i++) {
            if (rbbbCheckBox[i].isChecked() && rbbbV1.contains(i))
                inV1 = true;
            if (rbbbCheckBox[i].isChecked() && rbbbV6.contains(i))
                inV6 = true;
        }
        return inV1 && inV6;
    }

    private void updateBbbSelection() {
        Bbb bbbSelection = getBbbSelection();
        if (bbbSelection.equals(Bbb.LBBB)) {
            hideRbbbEntries();
            showLbbbEntries();
        } else {
            hideLbbbEntries();
            showRbbbEntries();
        }
    }

    private void displayVtResult() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        String sens = ".987";
        String spec = ".965";
        String message;
        message = getString(R.string.vt_result);
        message = message + " (Sens=" + sens + ", Spec=" + spec + ") ";
        dialog.setMessage(message);
        dialog.setTitle(getString(R.string.wct_result_label));
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

    private Bbb getBbbSelection() {
        String result = bbbSpinner.getSelectedItem().toString();
        if (result.startsWith("R"))
            return Bbb.RBBB;
        else
            return Bbb.LBBB;
    }

    private void hideLbbbEntries() {
        hideEntries(lbbbCheckBox);
    }

    private void showLbbbEntries() {
        showEntries(lbbbCheckBox);
    }

    private void hideRbbbEntries() {
        hideEntries(rbbbCheckBox);
    }

    private void showRbbbEntries() {
        showEntries(rbbbCheckBox);
    }

    private void hideEntries(CheckBox[] cb) {
        clearEntries();
        for (CheckBox aCb : cb) aCb.setVisibility(View.GONE);
    }

    private void showEntries(CheckBox[] cb) {
        clearEntries();
        for (CheckBox aCb : cb) aCb.setVisibility(View.VISIBLE);
    }

    private void clearEntries() {
        for (CheckBox aLbbbCheckBox : lbbbCheckBox) aLbbbCheckBox.setChecked(false);
        for (CheckBox aRbbbCheckBox : rbbbCheckBox) aRbbbCheckBox.setChecked(false);
    }


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
