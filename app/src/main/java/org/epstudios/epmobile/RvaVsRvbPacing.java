package org.epstudios.epmobile;

import android.os.Bundle;
import android.webkit.WebView;

public class RvaVsRvbPacing extends EpActivity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String titleView = extras.getString("EXTRA_TITLE");
            setTitle(titleView);

        }
        setContentView(R.layout.weblayout);
		super.onCreate(savedInstanceState);
		String url = "file:///android_asset/rvapexvsbasepacing.html";
		webView = (WebView) findViewById(R.id.web_view);
		webView.loadUrl(url);

	}

}
