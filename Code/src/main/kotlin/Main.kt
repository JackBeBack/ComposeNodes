// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:OptIn(ExperimentalComposeUiApi::class)

import Composables.BaseScreen
import Di.KoinModules
import Viewmodel.EventViewmodel
import Viewmodel.PositionViewModel
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEvent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.*
import jdk.jfr.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.get
import org.koin.java.KoinJavaComponent.inject
import java.awt.event.MouseEvent
import kotlin.random.Random

@Composable
@Preview
fun App(modifier: Modifier, screenSize: DpSize) {
    val positionViewModel by inject<PositionViewModel>(PositionViewModel::class.java)
    val eventViewModel by inject<EventViewmodel>(EventViewmodel::class.java)
    val zoomLevel = eventViewModel.zoomLevel.collectAsState()

    val panningEnabled = eventViewModel.panningEnabled.collectAsState()

    var lastEvent by remember { mutableStateOf<MouseEvent?>(null) }

    MaterialTheme {
        BaseScreen(modifier = Modifier.fillMaxSize().scale(zoomLevel.value).pointerInput(Unit) {
            forEachGesture {
                detectDragGestures { change, dragAmount ->
                    if (panningEnabled.value) {
                        positionViewModel.update(0, dragAmount)
                    }
                }
            }
        }, screenSize)
    }
}

fun main() {

    startKoin {
        modules(
            KoinModules.viewModel
        )
    }

    application {
        val positionViewModel by inject<PositionViewModel>(PositionViewModel::class.java)
        val eventViewModel by inject<EventViewmodel>(EventViewmodel::class.java)

        val state = rememberWindowState(placement = WindowPlacement.Maximized)
        val panningEnabled = remember{ mutableStateOf(true) }


        val stepSize = 10

        Window(
            onCloseRequest = ::exitApplication,
            state = state,
            title = "ComposeNodes",
            resizable = true,
            alwaysOnTop = true,
            onKeyEvent = {
                when (it.key) {
                    Key.S -> {
                        positionViewModel.update(0, IntOffset(0, -stepSize))
                    }
                    Key.D -> {
                        positionViewModel.update(0, IntOffset(-stepSize, 0))
                    }
                    Key.W -> {
                        positionViewModel.update(0, IntOffset(0, stepSize))
                    }
                    Key.A -> {
                        positionViewModel.update(0, IntOffset(stepSize, 0))
                    }
                    Key.Spacebar -> {
                        eventViewModel.togglePanning()
                    }
                    Key.Plus -> eventViewModel.updateZoom(0.02F)
                    Key.Minus -> eventViewModel.updateZoom(-0.02F)
                    else -> {
                        false
                    }
                }
                true

            }
        ) {
            App(modifier = Modifier, state.size)
        }

        println("Screen size: ${state.size}")
    }
}
