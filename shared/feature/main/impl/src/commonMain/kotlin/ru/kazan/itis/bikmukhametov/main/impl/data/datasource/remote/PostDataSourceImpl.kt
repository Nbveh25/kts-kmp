package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote

import ru.kazan.itis.bikmukhametov.main.api.datasource.remote.PostDataSource
import ru.kazan.itis.bikmukhametov.main.api.model.PostModel

@Suppress("MaxLineLength")
internal class PostDataSourceImpl: PostDataSource {
    override suspend fun getPosts(): List<PostModel> {
        return listOf(
            PostModel(
                id = 1L,
                title = "Android для Reddit",
                subtitle = "Подборка интересных сабреддитов",
                description = "Следите за трендами, новыми библиотеками и лучшими практиками разработки под Android.",
                imageUrl = "https://avatars.mds.yandex.net/i?id=27e5e33c11e83a398bb54146eb09808d_l-4820979-images-thumbs&n=13"
            ),
            PostModel(
                id = 2L,
                title = "Compose UI вдохновение",
                subtitle = "Идеи интерфейсов и анимаций",
                description = "Собранные примеры красивых интерфейсов, которые можно реализовать на Jetpack Compose.",
                imageUrl = "https://avatars.mds.yandex.net/i?id=092cd94ca80d232792ad4ae72edb43bf2c2ee692-8209451-images-thumbs&ref=rim&n=33&w=250&h=250"
            ),
            PostModel(
                id = 3L,
                title = "Kotlin и мультиплатформа",
                subtitle = "Лучшие статьи недели",
                description = "Подборка материалов о KMP, архитектуре, DI и оптимизации общего кода.",
                imageUrl = "https://avatars.mds.yandex.net/i?id=47e130a0d472f33df6a5169e78dedb9c_l-2036054-images-thumbs&n=13"
            ),
            PostModel(
                id = 4L,
                title = "Open Source проекты",
                subtitle = "Во что можно законтрибьютить",
                description = "Подборка открытых проектов для практики и развития навыков в реальном коде.",
                imageUrl = "https://avatars.mds.yandex.net/i?id=147571e14fcb82fa9577bb23bc988eaa_l-4220231-images-thumbs&n=13"
            ),
            PostModel(
                id = 5L,
                title = "Android для Reddit",
                subtitle = "Подборка интересных сабреддитов",
                description = "Следите за трендами, новыми библиотеками и лучшими практиками разработки под Android.",
                imageUrl = "https://avatars.mds.yandex.net/i?id=db97b8ac382f2e1444953c54c1d59394_l-5226902-images-thumbs&n=13"
            ),
            PostModel(
                id = 6L,
                title = "Compose UI вдохновение",
                subtitle = "Идеи интерфейсов и анимаций",
                description = "Собранные примеры красивых интерфейсов, которые можно реализовать на Jetpack Compose.",
                imageUrl = "https://avatars.mds.yandex.net/i?id=424d3a9a2eafa168975eaef6eef03d77239ca2e6-5332503-images-thumbs&n=13"
            ),
        )



    }
}
