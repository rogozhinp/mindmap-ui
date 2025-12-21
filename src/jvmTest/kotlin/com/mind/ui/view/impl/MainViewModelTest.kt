package com.mind.ui.view.impl

import com.mind.ui.model.BlankMindMapResponse
import com.mind.ui.model.MindMap
import com.mind.ui.model.Node
import com.mind.ui.repository.MindMapRepository
import com.mind.ui.theme.MindMapTheme
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MainViewModelImplTest {

    private lateinit var repository: MindMapRepository
    private lateinit var viewModel: MainViewModel

    private val mindMapId = UUID.randomUUID()
    private val centralNodeId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        repository = mock()
        viewModel = MainViewModel(repository)
    }

    @Test
    fun `loadBlankMindMap populates nodes and mindMapResponse`() = runBlocking {
        // Arrange
        val response = BlankMindMapResponse(
            mindMap = MindMap(mindMapId, "Test Map"),
            nodes = listOf(Node(centralNodeId, "Central", 0.0, 0.0, mindMapId))
        )
        whenever(repository.getBlankMindMap()).thenReturn(response)

        // Act
        viewModel.loadBlankMindMap()

        // Assert
        assertEquals("Test Map", viewModel.mindMapResponse?.mindMap?.title)
        assertEquals(1, viewModel.nodes.size)
        assertEquals("Central", viewModel.nodes.first().text)
    }

    @Test
    fun `addChildNode adds new node`() = runBlocking {
        // Arrange
        val parent = Node(centralNodeId, "Central", 0.0, 0.0, mindMapId)
        // Use the new theme-based expected coordinates
        val expectedX = MindMapTheme.nodeHorizontalSpacing
        val expectedY = 0.0

        val childNode = Node(UUID.randomUUID(), "Child", expectedX, expectedY, mindMapId, parentNodeId = parent.id)

        // Match the signature: addChildNode(parentNode, text, currentNodes)
        // We use any() for the list as the snapshot created in the VM is a new list instance
        whenever(repository.addChildNode(eq(parent), eq("New Child"), any())).thenReturn(childNode)

        // Act
        viewModel.addChildNode(parent)

        // Assert
        assertEquals(1, viewModel.nodes.size)
        assertEquals("Child", viewModel.nodes.first().text)
        assertEquals(parent.id, viewModel.nodes.first().parentNodeId)
        assertEquals(expectedX, viewModel.nodes.first().x)
    }

    @Test
    fun `moveNode updates coordinates`() = runBlocking {
        // Arrange
        val node = Node(UUID.randomUUID(), "Node", 0.0, 0.0, mindMapId)
        viewModel.nodes.add(node)

        // Act
        viewModel.moveNode(node.id, 10f, 20f)

        // Assert
        val updatedNode = viewModel.nodes.first()
        assertEquals(10.0, updatedNode.x)
        assertEquals(20.0, updatedNode.y)
    }

    @Test
    fun `renameNode updates text`() = runBlocking {
        // Arrange
        val nodeId = UUID.randomUUID()
        val node = Node(nodeId, "Old", 0.0, 0.0, mindMapId)
        viewModel.nodes.add(node)
        val updatedNode = node.copy(text = "New")
        whenever(repository.updateNodeText(nodeId, "New")).thenReturn(updatedNode)

        // Act
        viewModel.renameNode(nodeId, "New")

        // Assert
        assertEquals("New", viewModel.nodes.first().text)
    }

    @Test
    fun `renameNode preserves node coordinates`() = runBlocking {
        // Arrange
        val nodeId = UUID.randomUUID()
        val originalX = 50.0
        val originalY = 75.0
        val node = Node(nodeId, "Old Text", originalX, originalY, mindMapId)
        viewModel.nodes.add(node)

        // Mock returns a node (as the API would), but VM logic ensures original coords are kept
        val apiReturnNode = node.copy(text = "New Text", x = 0.0, y = 0.0)
        whenever(repository.updateNodeText(nodeId, "New Text")).thenReturn(apiReturnNode)

        // Act
        viewModel.renameNode(nodeId, "New Text")

        // Assert
        val renamedNode = viewModel.nodes.first()
        assertEquals("New Text", renamedNode.text)
        assertEquals(originalX, renamedNode.x, "X coordinate should be preserved by ViewModel")
        assertEquals(originalY, renamedNode.y, "Y coordinate should be preserved by ViewModel")
    }

    @Test
    fun `mindMapResponse is null initially`() {
        assertNull(viewModel.mindMapResponse)
        assertEquals(0, viewModel.nodes.size)
    }
}
