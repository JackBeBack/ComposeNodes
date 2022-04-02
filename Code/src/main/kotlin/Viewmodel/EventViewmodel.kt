package Viewmodel

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.awt.event.KeyEvent

class EventViewmodel(): KoinComponent {
   val panningEnabled = MutableStateFlow(true)

    val zoomLevel = MutableStateFlow(1.0F)

    private var block = false
    fun togglePanning(){
        if (!block){
            block = true
        GlobalScope.launch(Dispatchers.IO){
            panningEnabled.emit(!panningEnabled.value)
            delay(1000)
            block = false
        }}
    }

    fun updateZoom(value: Float){
        if (!block) {
            block = true
            GlobalScope.launch {
                zoomLevel.emit(zoomLevel.value + value)
                delay(100)
                block = false
            }
        }
    }

}