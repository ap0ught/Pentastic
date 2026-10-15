package app.pentastic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import app.pentastic.data.DataStoreRepository
import app.pentastic.data.ThemeMode
import app.pentastic.nav.SetupNavGraph
import app.pentastic.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.mp.KoinPlatform

@Composable
@Preview
fun App() {
    // Check if Koin has been initialized before attempting to inject DataStoreRepository.
    // In Android Studio Compose Preview mode, Koin's Application context is not started,
    // which previously caused an IllegalStateException when calling koinInject().
    val dataStoreRepository = if (KoinPlatform.getKoinOrNull() != null) {
        koinInject<DataStoreRepository>()
    } else {
        null
    }
    
    // Default to DAY_NIGHT theme if dataStoreRepository is null (e.g., during Preview rendering)
    val themeOrdinal by dataStoreRepository?.themeMode?.collectAsState(initial = ThemeMode.DAY_NIGHT.ordinal)
        ?: remember { mutableStateOf(ThemeMode.DAY_NIGHT.ordinal) }
    val themeMode = ThemeMode.fromOrdinal(themeOrdinal)

    AppTheme(themeMode = themeMode) {
        val navController = rememberNavController()
        SetupNavGraph(navController = navController)
    }
}
