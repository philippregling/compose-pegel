package com.example.composepegel.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@ExperimentalSerializationApi
@Serializable
data class GaugeZeroModelResponse(
    val unit: String = "",
    val value: Double = 0.0,
    @Serializable(with = LocalDateAsStringSerializer::class)
    val validFrom: LocalDate = LocalDate.now()
)

object LocalDateAsStringSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("validFrom", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val string = decoder.decodeString()
        return LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}