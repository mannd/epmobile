package org.epstudios.epmobile

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class InformationActivity : EpActivity() {

    private lateinit var algorithm: Algorithm
    private lateinit var infoType: InfoType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val algorithmName = intent.getStringExtra(ALGORITHM_NAME_KEY)
        infoType = InfoType.valueOf(intent.getStringExtra(INFO_TYPE_KEY) ?: InfoType.INSTRUCTIONS.name)

        algorithm = when (algorithmName) {
            "EASY-WPW" -> EasyWpw()
            //"SMART-WPW" -> SmartWpw()
            // Future algorithms can be added here
            else -> throw IllegalArgumentException("Unknown algorithm: $algorithmName")
        }

        title = when (infoType) {
            InfoType.INSTRUCTIONS -> "Instructions"
            InfoType.REFERENCE -> "References"
            InfoType.KEY -> "Key"
        }
        initToolbar()

        setContent {
            InformationScreen(algorithm = algorithm, infoType = infoType) { finish() }
        }
    }

    enum class InfoType {
        INSTRUCTIONS, REFERENCE, KEY
    }

    companion object {
        const val ALGORITHM_NAME_KEY = "ALGORITHM_NAME"
        const val INFO_TYPE_KEY = "INFO_TYPE"
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(algorithm: Algorithm, infoType: InformationActivity.InfoType, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(algorithm.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            when (infoType) {
                InformationActivity.InfoType.INSTRUCTIONS -> {
                    val instructions = algorithm.getInstructions()
                    if (instructions != null) {
                        Text(text = instructions)
                    }
                }
                InformationActivity.InfoType.REFERENCE -> {
                    val references = algorithm.getReferences()
                    // You can format and display the references here
                    references.forEach { reference ->
                        Text(text = reference.text)
                    }
                }
                InformationActivity.InfoType.KEY -> {
                    val key = algorithm.getKey()
                    if (key != null) {
                        Text(text = key)
                    }
                }
            }
        }
    }
}
