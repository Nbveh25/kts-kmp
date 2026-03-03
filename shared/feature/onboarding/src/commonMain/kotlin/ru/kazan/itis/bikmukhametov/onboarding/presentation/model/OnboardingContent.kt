package ru.kazan.itis.bikmukhametov.onboarding.presentation.model

import ru.kazan.itis.bikmukhametov.onboarding.generated.resources.Res
import ru.kazan.itis.bikmukhametov.onboarding.generated.resources.onboarding_screen_1
import ru.kazan.itis.bikmukhametov.onboarding.generated.resources.onboarding_screen_2
import ru.kazan.itis.bikmukhametov.onboarding.generated.resources.onboarding_screen_3
import ru.kazan.itis.bikmukhametov.onboarding.generated.resources.onboarding_screen_4

internal val onboardingPagesUi = listOf(
    OnboardingPage(
        title = "Привет!",
        description = "Reddit — это мир сообществ по любым интересам. " +
                "Находите единомышленников и обсуждайте то, что вам действительно интересно.",
        buttonText = "Далее",
        image = Res.drawable.onboarding_screen_1,
    ),
    OnboardingPage(
        title = "Что можно делать",
        description = "Публикуйте посты, задавайте вопросы, голосуйте за лучший контент и общайтесь в комментариях.",
        buttonText = "Далее",
        image = Res.drawable.onboarding_screen_2,
    ),
    OnboardingPage(
        title = "Возможности клиента",
        description = "Темы оформления, быстрый поиск по сабреддитам и пользователям, " +
                "фильтры по типу контента и умные рекомендации — всё в одном месте.",
        buttonText = "Далее",
        image = Res.drawable.onboarding_screen_3,
    ),
    OnboardingPage(
        title = "Офлайн‑режим",
        description = "Сохраняйте посты и комментарии заранее и читайте их, даже когда нет интернета.",
        buttonText = "Начать",
        image = Res.drawable.onboarding_screen_4,
    )
)
