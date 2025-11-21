package org.epstudios.epmobile.features.riskscores.ui

import android.os.Bundle
import androidx.compose.ui.platform.ComposeView
import org.epstudios.epmobile.R
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.ui.theme.AppTheme

class HcmAfActivity : EpActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hcm_af)
        setupInsets(R.id.hcm_af_activity_root_view)

        title = getString(R.string.hcm_af_risk_title)
        initToolbar()

        val composeView = findViewById<ComposeView>(R.id.compose_view)

        composeView.setContent {
            AppTheme() {
                HcmAfScreen()
            }
        }
    }
}