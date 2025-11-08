package org.epstudios.epmobile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgorithmView(model: Algorithm) {
    var currentNode by remember { mutableStateOf(model.rootNode) }
    val nodeStack = remember { mutableStateListOf<DecisionNode>() }
    var showResult by remember { mutableStateOf(false) }
    var algorithmResult by remember { mutableStateOf<String?>(null) }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "algorithm") {
        composable("algorithm") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(model.name) },
                        actions = {
                            IconButton(onClick = { navController.navigate("info") }) {
                                Icon(Icons.Filled.Info, contentDescription = "Information")
                            }
                        }
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(it)
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
                                columns = 2
                            ) {
                                branches.forEach { branch ->
                                    Button(
                                        onClick = {
                                            algorithmResult = branch.result
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
                                        currentNode = model.rootNode
                                        nodeStack.clear()
                                    }
                                ) {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                if (model.hasMap) {
                                    TextButton(onClick = { /* showMap() */ }) {
                                        Text("Show Map")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
        composable("info") {
            InformationView(
                instructions = model.getInstructions(),
                key = model.getKey(),
                references = model.getReferences(),
                name = model.name
            ) { navController.popBackStack() }
        }
    }
}

// A placeholder for the InformationView
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationView(
    instructions: String?,
    key: String?,
    references: List<Reference>,
    name: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        // Display your information here
        Column(modifier = Modifier.padding(it)) {
            Text("Information about the algorithm goes here.")
        }

    }
}
