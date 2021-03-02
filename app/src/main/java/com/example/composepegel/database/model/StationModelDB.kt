package com.example.composepegel.database.model

import com.example.composepegel.model.TimeSeriesModel
import com.example.composepegel.model.WaterModel
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.Index
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

open class StationModelDB(
    @PrimaryKey
    @Index
    var uuid: String = "",
    var number: String = "",
    var shortname: String = "",
    var longname: String = "",
    var km: Double = 0.0,
    var agency: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : RealmObject() {
    @LinkingObjects("stations")
    val water: RealmResults<WaterModelDB>? = null
}