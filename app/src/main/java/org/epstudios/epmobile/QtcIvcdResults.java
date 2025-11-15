package org.epstudios.epmobile;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.epstudios.epmobile.core.ui.base.EpActivity;

/**
 * Copyright (C) 2016 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 4/3/16.
 * <p/>
 * This file is part of epmobile.
 * <p/>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */
@SuppressWarnings("SpellCheckingInspection")
public class QtcIvcdResults extends EpActivity {
    static final int QT = 0;
    static final int QTC = 1;
    static final int JT = 2;
    static final int JTC = 3;
    static final int QTM = 4;
    static final int QTMC = 5;
    static final int QTRRQRS = 6;
    static final int PRELBBBQTC = 7;

    private int qt;
    private int qtc;
    private int jt;
    private int jtc;
    private int qtm;
    private int qtmc;
    private int qtrrqrs;
    private int prelbbbqtc;
    private boolean isLBBB;
    private String formulaName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionlist);
        setupInsets(R.id.selection_list_root_view);
        initToolbar();

        qt = getIntent().getIntExtra("QT", 0);
        qtc = getIntent().getIntExtra("QTc", 0);
        jt = getIntent().getIntExtra("JT", 0);
        jtc = getIntent().getIntExtra("JTc", 0);
        qtm = getIntent().getIntExtra("QTm", 0);
        qtmc = getIntent().getIntExtra("QTmc", 0);
        qtrrqrs = getIntent().getIntExtra("QTrrqrs", 0);
        prelbbbqtc = getIntent().getIntExtra("preLbbbQtc", 0);
        isLBBB = getIntent().getBooleanExtra("isLBBB", false);
        formulaName = getIntent().getStringExtra("QTcFormula");

        String qtMessage = makeResult(qt, "QT");
        String qtcMessage = makeResult(qtc, "QTc");
        String jtMessage = makeResult(jt, "JT");
        String jtcMessage = makeResult(jtc, "JTc");
        String qtmMessage = makeResult(qtm, "QTm");
        String qtmcMessage = makeResult(qtmc, "QTmc");
        String qtrrqrsMessage = makeResult(qtrrqrs, "QTrr,qrs");
        String prelbbbqtcMessage = isLBBB ? makeResult(prelbbbqtc, "preLBBBQTc") : getString(R.string.prelbbbqtc_lbbb_error_message);
        String infoMessage = getString(R.string.qt_ivcd_info_message);


        String[] items = new String[]{qtMessage, qtcMessage, jtMessage, jtcMessage,
                qtmMessage, qtmcMessage, qtrrqrsMessage, prelbbbqtcMessage, infoMessage};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case QT:
                    showQT();
                    break;
                case QTC:
                    showQTc();
                    break;
                case JT:
                    showJT();
                    break;
                case JTC:
                    showJTc();
                    break;
                case QTM:
                    showQTM();
                    break;
                case QTMC:
                    showQTMC();
                    break;
                case QTRRQRS:
                    showQTrrqrs();
                    break;
                case PRELBBBQTC:
                    showPreLBBBQTc();
                    break;
                default:
                    showInstructions();
                    break;
            }
        });
    }

    private String makeResult(int value, String label) {
        return (label + " = " + value + " msec");
    }

    private void showQT() {
        String message = getString(R.string.qt_details, qt);
        showDetails(getString(R.string.qt_details_title), message);
    }

    private void showQTc() {
        String message = getString(R.string.qtc_details, qtc, formulaName,
                getString(R.string.qtc_limits_short_reference));
        showDetails(getString(R.string.qtc_details_title), message);
    }

    private void showJT() {
        String message = getString(R.string.jt_details, jt);
        showDetails(getString(R.string.jt_details_title), message);
    }

    private void showJTc() {
        String message = getString(R.string.jtc_details, jtc);
        showDetails(getString(R.string.jtc_details_title), message);
    }

    private void showQTM() {
        String message = getString(R.string.qtm_details, qtm, getString(R.string.qtm_new_reference));
        showDetails(getString(R.string.qtm_new_details_title), message);
    }

    private void showQTMC() {
        String message = getString(R.string.qtmc_details, qtmc, formulaName,
                getString(R.string.qtm_new_reference));
        showDetails(getString(R.string.qtmc_new_details_title), message);
    }

    private void showQTrrqrs() {
        String message = getString(R.string.qtrrqrs_details,
                qtrrqrs, getString(R.string.qtrrqrs_formula),
                getString(R.string.qtrrqrs_reference));
        showDetails(getString(R.string.qtrrqrs_details_title), message);
    }

    private void showPreLBBBQTc() {
        String message = isLBBB ? getString(R.string.prelbbbqtc_details, prelbbbqtc,
                getString(R.string.prelbbbqtc_reference)) : getString(R.string.prelbbbqtc_lbbb_error_message);
        showDetails(getString(R.string.prelbbbqtc_details_title), message);
    }

    private void showInstructions() {
        showDetails(getString(R.string.qt_instructions_title),
                getString(R.string.qt_ivcd_instructions));
    }

    private void showDetails(String title, String message) {
        showAlertDialog(title, message);
    }


    // Note this is duplicate code used in QTcIVCD also.
    // No easy way to DRY this that I can think of.
    // WARNING: Any changes need to be duplicated in QTcIVCD.
    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        Reference referenceBogossian = new Reference(this,
                R.string.qtc_ivcd_reference_bogossian,
                R.string.qtc_ivcd_link_bogossian);
        Reference referenceRautaharju = new Reference(this,
                R.string.qtc_ivcd_reference_rautaharju,
                R.string.qtc_ivcd_link_rautaharju);
        Reference referenceYankelson = new Reference(this,
                R.string.qtc_ivcd_reference_yankelson,
                R.string.qtc_ivcd_link_yankelson);
        Reference referenceQtcLimits = new Reference(this,
                R.string.qtc_limits_reference,
                R.string.qtc_limits_link);
        Reference[] references = new Reference[4];
        references[0] = referenceBogossian;
        references[1] = referenceRautaharju;
        references[2] = referenceYankelson;
        references[3] = referenceQtcLimits;
        showReferenceAlertDialog(references);
    }
}
