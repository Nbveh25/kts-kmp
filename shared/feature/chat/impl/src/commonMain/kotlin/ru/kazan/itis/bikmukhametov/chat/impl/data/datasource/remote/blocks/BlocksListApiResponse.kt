package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.blocks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kazan.itis.bikmukhametov.chat.api.model.BlockModel
import ru.kazan.itis.bikmukhametov.chat.api.model.BlocksListResult

@Serializable
internal data class BlocksListApiResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: BlocksListDataDto? = null,
)

@Serializable
internal data class BlocksListDataDto(
    @SerialName("blocks") val blocks: List<BlockItemDto> = emptyList(),
)

@Serializable
internal data class BlockItemDto(
    @SerialName("id") val id: String = "",
    @SerialName("type") val type: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("next_block_id") val nextBlockId: String? = null,
)

internal fun BlockItemDto.toModel(): BlockModel = BlockModel(
    id = id,
    type = type,
    name = name,
)

internal fun BlocksListDataDto.toResult(): BlocksListResult = BlocksListResult(
    blocks = blocks.map { it.toModel() },
)
