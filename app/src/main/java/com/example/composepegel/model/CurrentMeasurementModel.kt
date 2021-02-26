package com.example.composepegel.model

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import kotlinx.serialization.*

@ExperimentalSerializationApi
@Serializable
data class CurrentMeasurementModel(
    val timeStamp: String = "",
    val value: Double = 0.0,
    val trend: Int = 1,
    val stateMnwMhw: String = "",
    val stateNswHsw: String = ""
)