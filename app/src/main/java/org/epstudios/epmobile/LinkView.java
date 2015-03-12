package org.epstudios.epmobile;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

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
        if (extras != null) {
            url  = extras.getString("EXTRA_URL");
            linkTitle = extras.getString("EXTRA_TITLE");
        }
        setContentView(R.layout.weblayout);
        super.onCreate(savedInstanceState);
        //String url = "file:///android_asset/rvapexvsbasepacing.html";
        webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(url);
        setTitle(linkTitle);

    }
}
