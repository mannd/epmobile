package org.epstudios.epmobile.features.algorithms.ui

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.epstudios.epmobile.features.diagnosis.ui.AvAnnulusMap
import org.epstudios.epmobile.features.algorithms.data.Algorithm
import org.epstudios.epmobile.features.algorithms.data.DecisionNode
import org.epstudios.epmobile.ui.components.CenteringGridLayout

@Composable
fun AlgorithmView(model: Algorithm) {
    val context = LocalContext.current
    val rootNode = remember { DecisionNode.Companion.loadFromResource(context, model.rootNodeResId) }
    val scrollState = rememberScrollState()

    var currentNode by remember { mutableStateOf(rootNode) }
    val nodeStack = remember { mutableStateListOf<DecisionNode>() }
    var showResult by remember { mutableStateOf(false) }
    var algorithmResult by remember { mutableStateOf<String?>(null) }
    var locationTag by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (currentNode.question != null) {
            Text(
                text = currentNode.question!!,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        if (currentNode.note != null) {
            Text(
                text = currentNode.note!!,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        if (currentNode.result != null) {
            Text(
                text = currentNode.result!!,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            currentNode.branches?.let { branches ->
                CenteringGridLayout(
                    modifier = Modifier.padding(16.dp),
                    columns = 2,
                    spacing = 16.dp,
                    itemSpacing = 16.dp
                ) {
                    branches.forEach { branch ->
                        Button(
                            onClick = {
                                algorithmResult = branch.result
                                locationTag = branch.tag
                                if (branch.isLeaf) {
                                    showResult = true
                                } else {
                                    nodeStack.add(currentNode)
                                    currentNode = branch
                                }
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(branch.label)
                        }
                    }
                    if (nodeStack.isNotEmpty()) {
                        Button(
                            onClick = { currentNode = nodeStack.removeAt(nodeStack.lastIndex) },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Back")
                        }
                    }
                }
            }
        }

        if (showResult) {
            val activity = LocalActivity.current
            AlertDialog(
                onDismissRequest = { showResult = false },
                title = { Text(model.resultTitle) },
                text = { Text(algorithmResult ?: "") },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (model.hasMap) {
                            TextButton(
                                onClick = {
                                    val intent = Intent(context, AvAnnulusMap::class.java)
                                    intent.putExtra("message", algorithmResult ?: "Accessory pathway map")
                                    intent.putExtra("location1", locationTag ?: "")
                                    intent.putExtra("location2", "")
                                    context.startActivity(intent)
                                }
                            ) {
                                Text("Show Map")
                            }
                        }
                        TextButton(
                            onClick = {
                                showResult = false
                                currentNode = rootNode
                                nodeStack.clear()
                            }
                        ) {
                            Text("Reset")
                        }
                        TextButton(
                            onClick = {
                                activity?.finish()
                            }
                        ) {
                            Text("Done")
                        }
                    }
                }
            )
        }
    }
}
