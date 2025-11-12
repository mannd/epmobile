package org.epstudios.epmobile

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AlgorithmView(model: Algorithm) {
    val context = LocalContext.current
    val rootNode = remember { DecisionNode.loadFromResource(context, model.rootNodeResId) }

    var currentNode by remember { mutableStateOf(rootNode) }
    val nodeStack = remember { mutableStateListOf<DecisionNode>() }
    var showResult by remember { mutableStateOf(false) }
    var algorithmResult by remember { mutableStateOf<String?>(null) }
    var locationTag by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        if (currentNode.question != null) {
            Text(
                text = currentNode.question!!,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        if (currentNode.note != null) {
            Text(
                text = currentNode.note!!,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                            onClick = { currentNode = nodeStack.removeLast() },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Back")
                        }
                    }
                }
            }
        }

        if (showResult) {
            AlertDialog(
                onDismissRequest = { showResult = false },
                title = { Text(model.resultTitle) },
                text = { Text(algorithmResult ?: "") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showResult = false
                            currentNode = rootNode
                            nodeStack.clear()
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    if (model.hasMap) {
                        TextButton(onClick = {
                            val intent: Intent = Intent(context, AvAnnulusMap::class.java)
                            intent.putExtra("message", algorithmResult ?: "Accessory pathway map")
                            intent.putExtra("location1", locationTag ?: "")
                            intent.putExtra("location2", "")
                            context.startActivity(intent)
                        }) {
                            Text("Show Map")
                        }
                    }
                }
            )
        }
    }
}
