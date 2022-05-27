package org.epstudios.epmobile;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;


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
@SuppressWarnings("deprecation")
public class LinkView extends EpActivity implements View.OnClickListener {
    private WebView webView;
    private Button calcCrClButton;
    private static final String BUTTON_TITLE = "Calculate CrCl";

    private boolean needsToRestoreState = false;
    private float offsetToRestore;

    static public final int CREATININE_CLEARANCE_CALCULATOR_ACTIVITY = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String url = "";
        String linkTitle = "";
        boolean showButton = false;
        if (extras != null) {
            url  = extras.getString("EXTRA_URL");
            linkTitle = extras.getString("EXTRA_TITLE");
            showButton = extras.getBoolean("EXTRA_SHOW_BUTTON");

        }
        if (url == null) {
            return;
        }

        if (showButton)
            setContentView(R.layout.weblayout);
        else
            setContentView(R.layout.weblayout_no_button);
        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new CustomWebViewClient());

        webView.loadUrl(url);
        // See https://stackoverflow.com/questions/57449900/letting-webview-on-android-work-with-prefers-color-scheme-dark
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
            }
        }
        setTitle(linkTitle);
        if (showButton) {
            calcCrClButton = findViewById(R.id.text_button);
            calcCrClButton.setOnClickListener(this);
            calcCrClButton.setText(BUTTON_TITLE);
        }
        initToolbar();

        OrientationChangeData data = (OrientationChangeData) getLastCustomNonConfigurationInstance();
        if (data != null) {
            needsToRestoreState = true;
            offsetToRestore = data.progress;
        }


    }

    public void onClick(View v) {
        if (v.getId() == R.id.text_button) {
            Intent i = new Intent(this, CreatinineClearanceCalculator.class);
            startActivityForResult(i, CREATININE_CLEARANCE_CALCULATOR_ACTIVITY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATININE_CLEARANCE_CALCULATOR_ACTIVITY &&
                resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra("EXTRA_RESULT_STRING");
            if (result == null) {
                result = BUTTON_TITLE;
            }

            calcCrClButton.setText(result);
        }
    }

    // TODO: Need to replace with ViewModel to save configuration.  Will leave for now.
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        OrientationChangeData data = new OrientationChangeData();
        data.progress = calculateOffset(webView);
        return data;
    }

    private final static class OrientationChangeData {
        public float progress;
    }

    private float calculateOffset(WebView webView) {
        float topPosition = webView.getTop();
        float height = webView.getHeight();
        float currentPosition = webView.getScrollY();
        return (currentPosition - topPosition) / height;
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            if (needsToRestoreState) {
                needsToRestoreState = false;
                view.postDelayed(() -> {
                    float webViewSize = webView.getContentHeight() - webView.getTop();
                    float positionInWebView = webViewSize * offsetToRestore;
                    int positionY = Math.round(webView.getTop() + positionInWebView);
                    webView.scrollTo(0, positionY);
                }, 300);
            }
            super.onPageFinished(view, url);
        }
    }


}
