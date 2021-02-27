package com.example.composepegel.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WaterModelResponse(
    val shortname: String = "",
    val longname: String = ""
)