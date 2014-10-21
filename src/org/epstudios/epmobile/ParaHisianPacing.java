package org.epstudios.epmobile;

import android.os.Bundle;
import android.webkit.WebView;

public class ParaHisianPacing extends EpReferenceActivity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = "file:///android_asset/parahisianpacinginstructions.html";
		webView = new WebView(this);
		setContentView(webView);
		webView.loadUrl(url);
	}

}
