package org.epstudios.epmobile;

import android.os.Bundle;
import android.webkit.WebView;

public class RvaVsRvbPacing extends EpReferenceActivity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.weblayout);
		super.onCreate(savedInstanceState);
		String url = "file:///android_asset/rvapexvsbasepacing.html";
		webView = (WebView) findViewById(R.id.web_view);
		webView.loadUrl(url);
	}

}
