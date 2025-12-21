package com.mind.ui.view

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.*
import com.mind.ui.model.Node
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MapViewTest {

    @Test
    fun `should display central node in the map view`() = runDesktopComposeUiTest {
        val mindMapId = UUID.randomUUID()
        val centralNode = Node(
            id = UUID.randomUUID(),
            text = "Central Topic",
            x = 0.0,
            y = 0.0,
            mindMapId = mindMapId
        )
        setContent {
            MapView(
                nodes = listOf(centralNode),
                onNodeMoved = { _, _, _ -> },
                onRenameNode = { _, _ -> }
            )
        }
        onNodeWithText("Central Topic").assertIsDisplayed()
    }

    @Test
    fun `should render multiple nodes from the list`() = runDesktopComposeUiTest {
        val mindMapId = UUID.randomUUID()
        val nodes = listOf(
            Node(UUID.randomUUID(), "Node A", -100.0, -100.0, mindMapId),
            Node(UUID.randomUUID(), "Node B", 100.0, 100.0, mindMapId)
        )
        setContent {
            MapView(
                nodes = nodes,
                onNodeMoved = { _, _, _ -> },
                onRenameNode = { _, _ -> }
            )
        }
        onNodeWithText("Node A").assertIsDisplayed()
        onNodeWithText("Node B").assertIsDisplayed()
    }

    @Test
    fun `should call onAddChild when node is right-clicked`() = runDesktopComposeUiTest {
        val mindMapId = UUID.randomUUID()
        val parentNode = Node(UUID.randomUUID(), "Parent Node", 0.0, 0.0, mindMapId)
        var clicked = false

        setContent {
            MapView(
                nodes = listOf(parentNode),
                onAddChild = { clicked = true },
                onNodeMoved = { _, _, _ -> },
                onRenameNode = { _, _ -> }
            )
        }

        onNodeWithText("Parent Node").performMouseInput {
            rightClick()
        }

        assert(clicked) { "onAddChild was not called when node was right-clicked" }
    }

    @Test
    fun `should trigger onRenameNode when double tap and enter is pressed`() = runDesktopComposeUiTest {
        val mindMapId = UUID.randomUUID()
        val nodeId = UUID.randomUUID()
        val node = Node(nodeId, "Initial Text", 0.0, 0.0, mindMapId)
        var capturedNewText = ""

        setContent {
            MapView(
                nodes = listOf(node),
                onNodeMoved = { _, _, _ -> },
                onRenameNode = { _, newText -> capturedNewText = newText }
            )
        }

        // 1. Double tap the text to enter edit mode
        onNodeWithText("Initial Text").performMouseInput {
            doubleClick()
        }

        // 2. Wait for composition and verify TextField is displayed
        waitForIdle()
        val textField = onNode(hasSetTextAction())
        textField.assertIsDisplayed()

        // 3. Type new text
        textField.performTextReplacement("Updated Text")

        // 4. Simulate the Enter key press on the textField
        textField.performKeyInput {
            pressKey(Key.Enter)
        }

        // 5. Wait for state update
        waitForIdle()

        assertEquals("Updated Text", capturedNewText, "The rename callback was not triggered.")
    }
}
