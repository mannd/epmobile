package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    static final String EPS = "EPS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionlist);
	initToolbar();

        // TODO update labels
        qt = getIntent().getIntExtra("QT", 0);
        qtc = getIntent().getIntExtra("QTc", 0);
        jt = getIntent().getIntExtra("JT", 0);
        jtc = getIntent().getIntExtra("JTc", 0);
        qtm = getIntent().getIntExtra("QTm", 0);
        qtmc = getIntent().getIntExtra("QTmc", 0);
        qtrrqrs = getIntent().getIntExtra("QTrrqrs", 0);
        prelbbbqtc = getIntent().getIntExtra("preLbbbQtc", 0);
        isLBBB = getIntent().getBooleanExtra("isLBBB", false);

        String qtMessage = makeResult(qt, "QT");
        String qtcMessage = makeResult(qtc, "QTc");
        String jtMessage = makeResult(jt, "JT");
        String jtcMessage = makeResult(jtc, "JTc");
        String qtmMessage = isLBBB ? makeResult(qtm, "QTm") : getString(R.string.qtm_lbbb_error_message);
        String qtmcMessage = isLBBB ? makeResult(qtmc, "QTmc") : getString(R.string.qtmc_lbbb_error_message);
        String qtrrqrsMessage = makeResult(qtrrqrs, "QTrr,qrs");
        String prelbbbqtcMessage = isLBBB ? makeResult(prelbbbqtc, "preLBBBQTc") : getString(R.string.prelbbbqtc_lbbb_error_message);
        String infoMessage = getString(R.string.qt_ivcd_info_message);


        String[] items = new String[] {qtMessage, qtcMessage, jtMessage, jtcMessage,
            qtmMessage, qtmcMessage, qtrrqrsMessage, prelbbbqtcMessage, infoMessage};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        ListView lv = findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        String message = getString(R.string.qtc_details, qtc,
                getString(R.string.qtc_reference));
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
        String message = isLBBB ? getString(R.string.qtm_details, qtm, getString(R.string.qtm_reference))
                : getString(R.string.qtm_lbbb_error_message);
        showDetails(getString(R.string.qtm_details_title), message);
    }

    private void showQTMC() {
        String message = isLBBB? getString(R.string.qtmc_details, qtmc, getString(R.string.qtm_reference))
                : getString(R.string.qtmc_lbbb_error_message);
        showDetails(getString(R.string.qtmc_details_title), message);
    }

    private void showQTrrqrs() {
        String message = getString(R.string.qtrrqrs_details,
                qtrrqrs, getString(R.string.qtrrqrs_formula),
                getString(R.string.qtrrqrs_reference));
        showDetails(getString(R.string.qtrrqrs_details_title), message);
    }

    private void showPreLBBBQTc() {
        String message = isLBBB? getString(R.string.prelbbbqtc_details, prelbbbqtc,
                getString(R.string.prelbbbqtc_reference)) : getString(R.string.prelbbbqtc_lbbb_error_message);
        showDetails(getString(R.string.prelbbbqtc_details_title), message);
    }
    private void showInstructions() {
        showDetails(getString(R.string.qt_intructions_title),
                getString(R.string.qt_ivcd_instructions));
    }

    private void showDetails(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.show();
    }
}
