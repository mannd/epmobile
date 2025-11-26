package org.epstudios.epmobile.features.riskscores.ui

import android.content.ClipData
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.epstudios.epmobile.R

@Composable
fun HcmAfScreen(viewModel: HcmAfViewModel = viewModel()) {
    // Observe state from the ViewModel
    val laDiameter by viewModel.laDiameterInput.collectAsState()
    val ageAtEval by viewModel.ageAtEvalInput.collectAsState()
    val ageAtDx by viewModel.ageAtDxInput.collectAsState()
    val hfSxChecked by viewModel.hfSxChecked.collectAsState()
    val result by viewModel.resultState.collectAsState()

    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clipboardLabel = stringResource(R.string.hcm_af_risk_title)
    val calculateLabel = stringResource(R.string.calculate_label)
    val clearLabel = stringResource(R.string.clear_label)
    val copyLabel = stringResource(R.string.copy_report_label)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Input Fields
            OutlinedTextField(
                value = laDiameter,
                onValueChange = viewModel::onLaDiameterChanged,
                label = { Text(stringResource(R.string.hcm_af_la_diameter)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ageAtEval,
                onValueChange = viewModel::onAgeAtEvalChanged,
                label = { Text(stringResource(R.string.hcm_af_age_at_evaluation)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ageAtDx,
                onValueChange = viewModel::onAgeAtDxChanged,
                label = { Text(stringResource(R.string.hcm_af_age_at_diagnosis)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Checkbox for Heart Failure Symptoms
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = hfSxChecked,
                    onCheckedChange = viewModel::onHfSxChanged
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("History of Heart Failure Symptoms")
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.calculate() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(calculateLabel)
                }
                Button(
                    onClick = { viewModel.clear() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(clearLabel)
                }
                Button(
                    onClick = {
                        val clip = ClipData.newPlainText(clipboardLabel, result)
                        clipboardManager.setPrimaryClip(clip)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(copyLabel)
                }
            }

            // Result Display
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = result,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HcmAfScreenPreview() {
    // You can wrap this with your app's theme for a more accurate preview
    HcmAfScreen()
}