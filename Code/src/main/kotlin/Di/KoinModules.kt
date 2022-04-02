package Di

import Viewmodel.EventViewmodel
import Viewmodel.PositionViewModel
import org.koin.dsl.module

class KoinModules {
    companion object {
        val viewModel = module {
            single { PositionViewModel() }
            single { EventViewmodel() }
        }
    }
}