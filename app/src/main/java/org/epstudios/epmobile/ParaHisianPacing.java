package org.epstudios.epmobile;

import android.os.Bundle;
import android.webkit.WebView;

public class ParaHisianPacing extends EpReferenceActivity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.weblayout);
		super.onCreate(savedInstanceState);
		String url = "file:///android_asset/parahisianpacinginstructions.html";
		webView = (WebView) findViewById(R.id.web_view);
		webView.loadUrl(url);
	}

}
