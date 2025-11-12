package org.epstudios.epmobile

import android.os.Bundle
import androidx.compose.ui.platform.ComposeView

class AlgorithmActivity : EpActivity() {

    private lateinit var algorithm: Algorithm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_algorithm)
        setupInsets(R.id.algorithm_activity_root_view)

        val algorithmName = intent.getStringExtra(ALGORITHM_NAME_KEY)

        algorithm = when (algorithmName) {
            "EasyWpw" -> EasyWpw()
            "SmartWpw" -> SmartWpw()
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
        return algorithm.getInstructions(this) == null
    }

    override fun hideReferenceMenuItem(): Boolean {
        return algorithm.getReferences(this).isEmpty()
    }

    override fun hideKeyMenuItem(): Boolean {
        return algorithm.getKey(this) == null
    }

    override fun showActivityInstructions() {
        showAlertDialog(algorithm.name, algorithm.getInstructions(this))
    }

    override fun showActivityReference() {
        showReferenceAlertDialog(algorithm.getReferences(this).toTypedArray())
    }

    override fun showActivityKey() {
        showAlertDialog(algorithm.name, algorithm.getKey(this))
    }

    companion object {
        const val ALGORITHM_NAME_KEY = "ALGORITHM_NAME"
    }
}
