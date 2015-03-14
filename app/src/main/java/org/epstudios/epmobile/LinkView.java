package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;


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
public class LinkView extends EpActivity implements View.OnClickListener {
    private WebView webView;
    private Button calcCrClButton;
    private static final String BUTTON_TITLE = "Calculate CrCl";


    static public int CREATININE_CLEARANCE_CALCULATOR_ACTIVITY = 123;

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
            calcCrClButton = (Button) findViewById(R.id.text_button);
            calcCrClButton.setOnClickListener(this);
            calcCrClButton.setText(BUTTON_TITLE);
        }

        super.onCreate(savedInstanceState);


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_button:
                Intent i = new Intent(this, CreatinineClearanceCalculator.class);
                startActivityForResult(i, CREATININE_CLEARANCE_CALCULATOR_ACTIVITY);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATININE_CLEARANCE_CALCULATOR_ACTIVITY &&
                resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra("EXTRA_RESULT_STRING");
            if (result == null) {
                result = BUTTON_TITLE;
            }

            calcCrClButton.setText(result);
        }

    }

}
