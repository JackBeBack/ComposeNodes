package Viewmodel

import Di.KoinModules
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import kotlin.random.Random

class PositionViewModel : KoinComponent {
    val offsets = mutableMapOf<Int, MutableStateFlow<IntOffset>>()


    fun trackNewOffset(index: Int, offset: IntOffset = IntOffset(0, 0)): MutableStateFlow<IntOffset> {
        if (!offsets.containsKey(index)) offsets[index] = MutableStateFlow(offset)

        return offsets[index]!!
    }

    fun update(index: Int, offset: IntOffset) {
        GlobalScope.launch(Dispatchers.IO) {
            if (index == 0) {
                offsets.forEach { index, offsetFlow ->
                    val current = offsetFlow.value
                    val newOffset = IntOffset(current.x + offset.x, current.y + offset.y)
                    GlobalScope.launch(Dispatchers.IO) {
                        offsets[index]?.emit(newOffset)
                    }
                }
            }
        }
    }

    fun update(index: Int, offset: Offset) {
        update(index, IntOffset(offset.x.toInt(), offset.y.toInt()))
    }
}


