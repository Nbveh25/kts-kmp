package ru.kazan.itis.bikmukhametov.chat.impl.presentation.model

/** Выбранное вложение перед отправкой (байты в памяти). */
class PickedAttachment(
    val fileName: String,
    val mimeType: String?,
    val bytes: ByteArray,
    val sendAsFile: Boolean,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PickedAttachment) return false
        return fileName == other.fileName &&
            mimeType == other.mimeType &&
            sendAsFile == other.sendAsFile &&
            bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + sendAsFile.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
