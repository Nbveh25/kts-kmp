package ru.kazan.itis.bikmukhametov.database.locale

enum class AppLanguage(val tag: String) {
    RU("ru"),
    EN("en"),
    ;

    companion object {
        fun fromStoredTag(tag: String?): AppLanguage =
            entries.firstOrNull { it.tag == tag } ?: RU
    }
}
