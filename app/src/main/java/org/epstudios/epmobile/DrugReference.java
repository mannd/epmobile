
/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 3/11/15.
 * <p/>
 * This file is part of EP Mobile.
 * <p/>
 * EP Coding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * EP Coding is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with EP Coding.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Note also:
 * <p/>
 * CPT copyright 2012 American Medical Association. All rights
 * reserved. CPT is a registered trademark of the American Medical
 * Association.
 * <p/>
 * A limited number of CPT codes are used in this program under the Fair Use
 * doctrine of US Copyright Law.  See README.md for more information.
 */

package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class DrugReference extends EpActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.weblayout);
        super.onCreate(savedInstanceState);
        String url = "file:///android_asset/apixaban.html";
        webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent parentActivityIntent = new Intent(this, DrugReferenceList.class);
//                parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                        | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(parentActivityIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
