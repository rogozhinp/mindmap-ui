package com.mind.ui.view

import androidx.compose.ui.test.*
import com.mind.ui.model.BlankMindMapResponse
import com.mind.ui.model.MindMap
import com.mind.ui.model.Node
import com.mind.ui.repository.MindMapRepository
import com.mind.ui.view.impl.MainViewModel
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import java.util.*
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class WelcomeViewTest {

    @Test
    fun `clicking create button loads the map view`() = runDesktopComposeUiTest {
        // 1. Setup Mock Data
        val mindMapId = UUID.randomUUID()
        val mockResponse = BlankMindMapResponse(
            mindMap = MindMap(mindMapId, "Test Map"),
            nodes = listOf(Node(UUID.randomUUID(), "Central Node", 0.0, 0.0, mindMapId))
        )

        // 2. Mock Repository
        val repository = mock<MindMapRepository>()
        repository.stub {
            onBlocking { getBlankMindMap() } doReturn mockResponse
        }

        val viewModel = MainViewModel(repository)

        // 3. Preload MindMapResponse to avoid indefinite wait
        kotlinx.coroutines.runBlocking {
            viewModel.loadBlankMindMap()
        }

        // 4. Set Content
        setContent {
            WelcomeView(viewModel)
        }

        // 5. Assert MapView is displayed immediately
        onNodeWithText("Central Node").assertExists().assertIsDisplayed()
        onNodeWithText("Create New Mind Map").assertDoesNotExist()
    }
}
