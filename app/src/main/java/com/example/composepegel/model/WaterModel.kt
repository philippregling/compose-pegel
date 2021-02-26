package com.example.composepegel.model

import kotlinx.serialization.Serializable

@Serializable
data class WaterModel(
    val shortname: String = "",
    val longname: String = ""
)