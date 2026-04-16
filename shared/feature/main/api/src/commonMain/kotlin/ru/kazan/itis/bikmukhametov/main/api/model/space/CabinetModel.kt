package ru.kazan.itis.bikmukhametov.main.api.model.space

/**
 * Бизнес-модель Кабинета.
 * Упрощённая версия, типы подобраны под ответ API.
 */
data class CabinetModel(
    val id: String,
    val host: String,
    val domain: String,
    val name: String,
    val category: String,
    val billing: BillingModel,
    val shouldPay: Boolean,
    val role: String,
    val permissions: List<String>,
    val createdAt: String,
    val createdBy: String
)

/**
 * Бизнес-модель Биллинга.
 */
data class BillingModel(
    val balance: Double,
    val trialUntil: String,
    val tariff: String,
    val hasLinkedCard: Boolean
)
