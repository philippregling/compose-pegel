package com.example.composepegel.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@ExperimentalSerializationApi
@Serializable
data class CurrentMeasurementModelResponse(
    @Serializable(with = ZonedDateTimeAsStringSerializer::class)
    val timeStamp: ZonedDateTime = ZonedDateTime.now(),
    val value: Double = 0.0,
    val trend: Int = 0,
    val stateMnwMhw: String = "",
    val stateNswHsw: String = ""
)

object ZonedDateTimeAsStringSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("timestamp", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
    }

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val string = decoder.decodeString()
        return ZonedDateTime.parse(string, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    }
}