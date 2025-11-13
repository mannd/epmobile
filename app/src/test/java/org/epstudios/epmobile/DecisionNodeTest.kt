package org.epstudios.epmobile

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

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
 * Unit tests for the DecisionNode class, specifically for loading from resources.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P]) // Configure Robolectric to use a specific SDK version
class DecisionNodeTest {

    @Test
    fun loadFromResource_parsesJsonCorrectly() {
        // 1. Get the application context from the test runner
        val context = ApplicationProvider.getApplicationContext<Context>()

        // 2. Load the DecisionNode from the test JSON resource
        val rootNode = DecisionNode.loadFromResource(context, R.raw.test_decision_tree)

        // 3. Assert that the loaded data is correct
        assertThat(rootNode).isNotNull()
        assertThat(rootNode.id).isEqualTo("test-root")
        assertThat(rootNode.question).isEqualTo("Is this a test?")
        assertThat(rootNode.isLeaf).isFalse()
        assertThat(rootNode.note).isEqualTo("This is a tree for unit testing.")
        assertThat(rootNode.tag).isEqualTo("test-tag")

        // 4. Assert that branches are parsed correctly
        assertThat(rootNode.branches).hasSize(2)

        // 5. Check the first branch ("Yes")
        val yesBranch = rootNode.branches?.get(0)
        assertThat(yesBranch).isNotNull()
        assertThat(yesBranch?.label).isEqualTo("Yes")
        assertThat(yesBranch?.isLeaf).isTrue()
        assertThat(yesBranch?.result).isEqualTo("Test successful.")

        // 6. Check the second branch ("No") and its nested branch
        val noBranch = rootNode.branches?.get(1)
        assertThat(noBranch).isNotNull()
        assertThat(noBranch?.label).isEqualTo("No")
        assertThat(noBranch?.isLeaf).isFalse()
        assertThat(noBranch?.question).isEqualTo("Is this a nested test?")
        assertThat(noBranch?.branches).hasSize(1)

        val nestedBranch = noBranch?.branches?.get(0)
        assertThat(nestedBranch).isNotNull()
        assertThat(nestedBranch?.result).isEqualTo("Nested test successful.")
    }
}