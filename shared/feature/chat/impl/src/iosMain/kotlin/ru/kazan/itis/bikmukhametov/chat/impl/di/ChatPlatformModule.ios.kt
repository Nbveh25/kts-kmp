package ru.kazan.itis.bikmukhametov.chat.impl.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.kazan.itis.bikmukhametov.chat.impl.data.platform.AttachmentContentReader
import ru.kazan.itis.bikmukhametov.chat.impl.data.platform.IosAttachmentContentReader

actual fun chatPlatformModule(): Module = module {
    single<AttachmentContentReader> { IosAttachmentContentReader() }
}
