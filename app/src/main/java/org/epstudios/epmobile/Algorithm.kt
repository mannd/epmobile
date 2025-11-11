package org.epstudios.epmobile

import android.content.Context
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
    fun getReferences(context: Context): List<Reference>
    fun getInstructions(context: Context): String?
    fun getKey(context: Context): String?
}
