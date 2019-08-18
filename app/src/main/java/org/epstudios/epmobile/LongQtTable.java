package org.epstudios.epmobile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Copyright (C) 2019 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 2019-08-15.
 * <p>
 * This file is part of epmobile.
 * <p>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */
public class LongQtTable extends EpActivity {
    private TableLayout tableLayout;
    private LayoutInflater layoutInflater;

    private RowData[] data = { new RowData("LQT1", "KCNQ1", "Encodes the α-subunit of the slow delayed rectifier potassium channel KV7.1 carrying the potassium current IKs."),
            new RowData("LQT2", "KCNH2", "Also known as hERG. Encodes the α-subunit of the rapid delayed rectifier potassium channel KV11.1 carrying the potassium current IKr."),
            new RowData("LQT3", "SCN5A", "Encodes the α-subunit of the cardiac sodium channel NaV1.5 carrying the sodium current INa."),
            new RowData("LQT4", "ANK2", "Encodes Ankyrin B which anchors the ion channels in the cell. Disputed whether truly disease causing versus minor QT susceptibility gene."),
            new RowData("LQT5", "KCNE1", "Encodes MinK, a potassium channel β-subunit. Heterozygous inheritance causes Romano-Ward, homozygous inheritance causes Jervell and Lange-Nielsen syndrome."),
            new RowData("LQT6", "KCNE2", "Encodes MiRP1, a potassium channel β-subunit. Disputed whether truly disease causing versus minor QT susceptibility gene."),
            new RowData("LQT7", "KCNJ2", "Encodes inward rectifying potassium current Kir2.1 carrying the potassium current IK1. Causes Andersen-Tawil syndrome."),
            new RowData("LQT8", "CACNA1c", "Encodes the α-subunit CaV1.2 of the calcium channel Cav1.2 carrying the calcium current ICa(L). Causes Timothy syndrome."),
            new RowData("LQT9", "CAV3", "Encodes Caveolin-3, responsible for forming membrane pouches known as caveolae. Mutations in this gene may increase the late sodium current INa."),
            new RowData("LQT10", "SCN4B", "Encodes the β4-subunit of the cardiac sodium channel."),
            new RowData("LQT11", "AKAP9", "Encodes A-kinase associated protein which interacts with KV7.1."),
            new RowData("LQT12", "SNTA1", "Encodes syntrophin-α1. Mutations in this gene may increase the late sodium current INa."),
            new RowData("LQT13", "KCNJ5", "Also known as GIRK4, encodes G protein-sensitive inwardly rectifying potassium channels (Kir3.4) which carry the potassium current IK(ACh)."),
            new RowData("LQT14", "CALM1", "Encodes calmodulin-1, a calcium-binding messenger protein that interacts with the calcium current ICa(L)."),
            new RowData("LQT15", "CALM2", "Encodes calmodulin-2, a calcium-binding messenger protein that interacts with the calcium current ICa(L)."),
            new RowData("LQT16", "CALM3", "Encodes calmodulin-3, a calcium-binding messenger protein that interacts with the calcium current ICa(L)."),
            new RowData("LQT17",  "TRDN",  "Encodes triadin, associated with the release of calcium from the sarcoplastic reticulum.  Causes exercise-induced cardiac arrest in young children.")
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.longqttable);
        initToolbar();
        tableLayout = (TableLayout)findViewById(R.id.lqt_table);
        layoutInflater = LayoutInflater.from(this);
        init();

    }

    private void init() {
        RowData rowData[] = initData();
        int count = rowData.length;
        for (int i = 0; i < count; i++) {
            final View item = layoutInflater.inflate(R.layout.lqtrowlayout, tableLayout, false);
            final TextView subtype_view = (TextView) item.findViewById(R.id.lqt_subtype);
            final TextView channel_view = (TextView) item.findViewById(R.id.lqt_channel);
            final TextView details_view = (TextView) item.findViewById(R.id.lqt_details);

            subtype_view.setText(rowData[i].subtype);
            channel_view.setText(rowData[i].channel);
            details_view.setText(rowData[i].details);


            TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            tableLayout.addView(item, trParamsSep );

            // add separator row
            final TableRow trSep = new TableRow(this);

            trSep.setLayoutParams(trParamsSep);
            TextView tvSep = new TextView(this);
            TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tvSepLay.span = 4;
            tvSep.setLayoutParams(tvSepLay);
            tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
            tvSep.setHeight(1);

            trSep.addView(tvSep);
            tableLayout.addView(trSep, trParamsSep);

        }
    }

    private class RowData {
        String subtype;
        String channel;
        String details;

        RowData(String subtype, String channel, String details) {
            this.subtype = subtype;
            this.channel = channel;
            this.details = details;
        }

        RowData() {
            subtype = "";
            channel = "";
            details = "";
        }
    }

    private RowData[] initData() {
        RowData rowData[] = data;
//        RowData rowData[] = new RowData[10];
//        for (int i = 0; i < 10; i++) {
//            RowData row = new RowData();
//            row.subtype = "subtype" + i;
//            row.channel = "channel" + i;
//            row.details = "details detail detail detail detail detail detail detail detail detail detail detail " + i;
//            rowData[i] = row;
//        }
        return rowData;
    }
}
