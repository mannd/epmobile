package org.epstudios.epmobile;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 3/11/15.
 * <p/>
 * This file is part of EP Mobile.
 * <p/>
 * EP Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * EP Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with EP Mobile.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 */
public class LinkView extends EpActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        String url = "";
        String linkTitle = "";
        Boolean showButton = false;
        if (extras != null) {
            url  = extras.getString("EXTRA_URL");
            linkTitle = extras.getString("EXTRA_TITLE");
            showButton = extras.getBoolean("EXTRA_SHOW_BUTTON");

        }
        if (showButton)
            setContentView(R.layout.weblayout);
        else
            setContentView(R.layout.weblayout_no_button);
        webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(url);
        setTitle(linkTitle);
        if (showButton) {
            Button button = (Button) findViewById(R.id.text_button);
            button.setText("CrCl = 17mL/min");
        }

        super.onCreate(savedInstanceState);


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_button:
                ;
                break;
        }
    }}
