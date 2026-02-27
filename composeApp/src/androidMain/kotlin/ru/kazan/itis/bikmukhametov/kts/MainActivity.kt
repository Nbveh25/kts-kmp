package ru.kazan.itis.bikmukhametov.kts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import ru.kazan.itis.bikmukhametov.kts.presentation.App
import ru.kazan.itis.bikmukhametov.kts.presentation.theme.KtsMetaclassTheme
import ru.kazan.itis.bikmukhametov.onboarding.presentation.screens.OnboardingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            KtsMetaclassTheme {
                Surface {
                    var showOnboarding by rememberSaveable { mutableStateOf(true) }

                    if (showOnboarding) {
                        OnboardingScreen(
                            onOnboardingComplete = { showOnboarding = false }
                        )
                    } else {
                        App()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
