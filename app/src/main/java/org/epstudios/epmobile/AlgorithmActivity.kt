package org.epstudios.epmobile

import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.platform.ComposeView

class AlgorithmActivity : EpActivity() {

    private lateinit var algorithm: Algorithm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_algorithm)

        val algorithmName = intent.getStringExtra(ALGORITHM_NAME_KEY)

        algorithm = when (algorithmName) {
            "EasyWpw" -> EasyWpw()
            // Future algorithms can be added here
            else -> throw IllegalArgumentException("Unknown algorithm: $algorithmName")
        }

        title = algorithm.name
        initToolbar()

        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            AlgorithmView(model = algorithm)
        }
    }

    override fun hideInstructionsMenuItem(): Boolean {
        return algorithm.getInstructions() == null
    }

    override fun hideReferenceMenuItem(): Boolean {
        return algorithm.getReferences().isEmpty()
    }

    override fun hideKeyMenuItem(): Boolean {
        return algorithm.getKey() == null
    }

    override fun showActivityInstructions() {
        val intent = Intent(this, InformationActivity::class.java).apply {
            putExtra(InformationActivity.INFO_TYPE_KEY, InformationActivity.InfoType.INSTRUCTIONS.name)
            putExtra(InformationActivity.ALGORITHM_NAME_KEY, algorithm.name)
        }
        startActivity(intent)
    }

    override fun showActivityReference() {
        val intent = Intent(this, InformationActivity::class.java).apply {
            putExtra(InformationActivity.INFO_TYPE_KEY, InformationActivity.InfoType.REFERENCE.name)
            putExtra(InformationActivity.ALGORITHM_NAME_KEY, algorithm.name)
        }
        startActivity(intent)
    }

    override fun showActivityKey() {
        val intent = Intent(this, InformationActivity::class.java).apply {
            putExtra(InformationActivity.INFO_TYPE_KEY, InformationActivity.InfoType.KEY.name)
            putExtra(InformationActivity.ALGORITHM_NAME_KEY, algorithm.name)
        }
        startActivity(intent)
    }

    companion object {
        const val ALGORITHM_NAME_KEY = "ALGORITHM_NAME"
    }
}
