package org.epstudios.epmobile

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.UUID

/**
Copyright (C) 2025 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 11/7/25.

This file is part of epmobile.

epmobile is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

epmobile is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Represents a node in a decision tree. It can be a question with branches,
 * or a terminal leaf node with a result.
 *
 * @property id A unique identifier for the node.
 * @property label The text of the answer from the parent node that leads to this node. Empty for the root.
 * @property question The question to be asked at this node. Null for leaf nodes.
 * @property branches A list of possible next nodes, corresponding to answers for the question. Null for leaf nodes.
 * @property result The final outcome or result if this node is a leaf. Null for non-leaf nodes.
 * @property note Additional information or context for this node.
 * @property tag An optional tag for categorization or special handling.
 */
@Serializable
data class DecisionNode(
    val id: String = UUID.randomUUID().toString(),
    var label: String = "",
    var question: String? = null,
    var branches: List<DecisionNode>? = null,
    var result: String? = null,
    var note: String? = null,
    var tag: String? = null
) {
    /**
     * A computed property that returns true if the node is a leaf (i.e., it has a result).
     */
    val isLeaf: Boolean
        get() = result != null

    companion object {
        /**
         * A constant identifier for the root node of the tree.
         */
        const val ROOT_NODE_ID = "node-root"

        private val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        /**
         * Loads a DecisionNode object from a JSON file bundled in the app's raw resources.
         *
         * @param context The application or activity context, needed to access resources.
         * @param resourceId The ID of the raw resource file (e.g., R.raw.decision_tree).
         * @return The deserialized DecisionNode object.
         * @throws Exception if the resource is not found or json parsing fails.
         */
        fun loadFromResource(context: Context, resourceId: Int): DecisionNode {
            val jsonString = context.resources.openRawResource(resourceId)
                .bufferedReader().use { it.readText() }
            return json.decodeFromString(jsonString)
        }
    }
}