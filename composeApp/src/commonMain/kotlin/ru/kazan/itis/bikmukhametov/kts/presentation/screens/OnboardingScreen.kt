package ru.kazan.itis.bikmukhametov.kts.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kts.composeapp.generated.resources.Res
import kts.composeapp.generated.resources.button_login
import kts.composeapp.generated.resources.ic_error
import kts.composeapp.generated.resources.ic_loading
import kts.composeapp.generated.resources.onboarding_welcome
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val IMAGE_URL =
    "https://res.cloudinary.com/dsrqq4er2/image/upload/v1771504876/Onboarding_a9p6c9.png"

/* Приветственный экран */
@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Картинка из облака через Coil
        AsyncImage(
            model = IMAGE_URL,
            contentDescription = "Greeting Image",
            modifier = Modifier
                .size(400.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(Res.drawable.ic_error),
            placeholder = painterResource(Res.drawable.ic_loading)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Текст приветствия
        Text(
            text = stringResource(Res.string.onboarding_welcome),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка перехода к экрану логина
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.button_login),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
