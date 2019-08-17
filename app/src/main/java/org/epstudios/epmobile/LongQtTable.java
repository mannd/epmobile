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
    }

    private RowData[] initData() {
        RowData rowData[] = new RowData[10];
        for (int i = 0; i < 10; i++) {
            RowData row = new RowData();
            row.subtype = "subtype" + i;
            row.channel = "channel" + i;
            row.details = "details detail detail detail detail detail detail detail detail detail detail detail " + i;
            rowData[i] = row;
        }
        return rowData;
    }
}
