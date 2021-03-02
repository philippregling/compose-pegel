package com.example.composepegel.database

import com.example.composepegel.database.model.StationModelDB
import com.example.composepegel.database.model.WaterModelDB
import com.example.composepegel.model.StationModel
import com.example.composepegel.model.WaterModel
import io.realm.Realm
import io.realm.kotlin.createObject

fun List<WaterModel>.convertWaters(): List<WaterModelDB> {
    return map { it.convert() }
}

fun WaterModel.convert(): WaterModelDB {
    val waterModelDB = WaterModelDB()
    waterModelDB.shortname = this.shortname
    waterModelDB.longname = this.longname
    //Stations get added in a different method, this is standalone
    return waterModelDB
}

fun List<WaterModelDB>.convertWatersFromDB(): List<WaterModel> {
    return map { it.convert() }
}

fun WaterModelDB.convert(): WaterModel {
    return WaterModel(
        shortname, longname
    )
}

fun List<StationModel>.convertStations(): List<StationModelDB> {
    return map { it.convert() }
}

fun StationModel.convert(): StationModelDB {
    val dbModel = StationModelDB()
    dbModel.uuid = this.uuid
    dbModel.number = this.number
    dbModel.shortname = this.shortname
    dbModel.longname = this.longname
    dbModel.km = this.km
    dbModel.agency = this.agency
    dbModel.latitude = this.latitude
    dbModel.longitude = this.longitude
    return dbModel
}

fun List<StationModelDB>.convertStationsFromDB(): List<StationModel> {
    return map { it.convert() }
}

fun StationModelDB.convert(): StationModel {
    return StationModel(
        uuid, number, shortname, longname, km, agency, water?.firstOrNull()?.convert(), latitude, longitude
    )
}