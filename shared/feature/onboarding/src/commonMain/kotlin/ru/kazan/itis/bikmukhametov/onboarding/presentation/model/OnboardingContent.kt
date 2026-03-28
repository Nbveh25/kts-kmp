package ru.kazan.itis.bikmukhametov.onboarding.presentation.model

import ru.kazan.itis.bikmukhametov.onboarding.generated.resources.Res
import ru.kazan.itis.bikmukhametov.onboarding.generated.resources.onboarding_screen_1
import ru.kazan.itis.bikmukhametov.onboarding.generated.resources.onboarding_screen_2
import ru.kazan.itis.bikmukhametov.onboarding.generated.resources.onboarding_screen_3

internal val onboardingPagesUi = listOf(
    OnboardingPage(
        title = "Весь бизнес в одном боте",
        description = "Smartbot Pro — это экосистема для автоматизации общения. " +
                "Создавайте сложных ИИ-ассистентов, воронки продаж и интеграции с CRM без единой строчки кода.",
        buttonText = "Далее",
        image = Res.drawable.onboarding_screen_1,
    ),
    OnboardingPage(
        title = "Конструктор сценариев",
        description = "Собирайте цепочки сообщений, принимайте платежи, " +
                "сегментируйте базу и подключайте ChatGPT для умных ответов вашим клиентам.",
        buttonText = "Далее",
        image = Res.drawable.onboarding_screen_2,
    ),
    OnboardingPage(
        title = "Контроль в кармане",
        description = "Отвечайте клиентам лично, когда бот не справляется. " +
                "Управляйте статусами, меняйте переменные пользователей и следите за уведомлениями в реальном времени.",
        buttonText = "Далее",
        image = Res.drawable.onboarding_screen_3,
    ),
)
