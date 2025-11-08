package org.epstudios.epmobile

import androidx.annotation.RawRes

/**
 * Basis of algorithms, such as WPW or WCT algorithms in the app.
 */
interface Algorithm {
    val name: String
    @get:RawRes
    val rootNodeResId: Int
    val resultTitle: String
    val hasMap: Boolean
    fun getReferences(): List<Reference>
    fun getInstructions(): String?
    fun getKey(): String?
}
