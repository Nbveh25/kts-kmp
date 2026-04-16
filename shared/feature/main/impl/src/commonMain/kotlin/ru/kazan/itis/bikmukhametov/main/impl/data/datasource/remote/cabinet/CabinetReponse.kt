package ru.kazan.itis.bikmukhametov.main.impl.data.datasource.remote.cabinet

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.main.api.model.space.BillingModel
import ru.kazan.itis.bikmukhametov.main.api.model.space.CabinetModel

/** Ответ GET /api/cabinets/list — как `projects/list`: `data.cabinets`. */
@Serializable
data class CabinetListResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: CabinetListData,
)

@Serializable
data class CabinetListData(
    @SerialName("cabinets") val cabinets: List<CabinetDto>,
)

/** Одиночный кабинет (legacy / get_by_domain) — оставлено при необходимости других эндпоинтов */
@Serializable
data class CabinetResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: CabinetData
)

@Serializable
data class CabinetData(
    @SerialName("cabinet") val cabinet: CabinetDto
)

@Serializable
data class CabinetDto(
    @SerialName("_id") val id: String,
    @SerialName("host") val host: String,
    @SerialName("domain") val domain: String,
    @SerialName("name") val name: String,
    @SerialName("category") val category: String,
    @SerialName("billing") val billing: BillingDto,
    @SerialName("should_pay") val shouldPay: Boolean,
    @SerialName("cabinet_role") val cabinetRole: String,
    @SerialName("permissions") val permissions: List<String>,
    @SerialName("date_created") val dateCreated: String,
    @SerialName("created_by") val createdBy: String
)

@Serializable
data class BillingDto(
    val balance: Double,
    @SerialName("trial_until") val trialUntil: String,
    @SerialName("split") val split: String,
    @SerialName("tariff") val tariff: String,
    @SerialName("had_linked_card") val hadLinkedCard: Boolean
)

fun BillingDto.toModel(): BillingModel = BillingModel(
    balance = this.balance,
    trialUntil = this.trialUntil,
    tariff = this.tariff,
    hasLinkedCard = this.hadLinkedCard
)

fun CabinetDto.toModel(): CabinetModel = CabinetModel(
    id = this.id,
    host = this.host,
    domain = this.domain,
    name = this.name,
    category = this.category,
    billing = this.billing.toModel(),
    shouldPay = this.shouldPay,
    role = this.cabinetRole,
    permissions = this.permissions,
    createdAt = this.dateCreated,
    createdBy = this.createdBy
)
