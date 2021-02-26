package com.example.composepegel.model

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

@ExperimentalSerializationApi
@Serializable
data class GaugeZeroModel(
    val unit: String = "",
    val value : Double = 0.0,
    val validFrom: String = ""
)