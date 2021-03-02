package com.example.composepegel.database

import android.util.Log
import com.example.composepegel.database.model.StationModelDB
import com.example.composepegel.database.model.WaterModelDB
import com.example.composepegel.model.StationModel
import com.example.composepegel.model.WaterModel
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where

interface DatabaseClient {
    suspend fun queryWaters(): List<WaterModel>

    suspend fun queryStations(): List<StationModel>

    suspend fun saveWaters(watersToSave: List<WaterModel>)

    suspend fun saveStations(stationsToSave: List<StationModel>)

    suspend fun queryWaterForShortName(shortName: String): WaterModel

    suspend fun queryStationsForWater(waterShortName: String): List<StationModel>

    suspend fun addStationsToWater(waterShortName: String, stations: List<StationModel>)

    suspend fun queryStationForUuid(uuid: String): StationModel
}

class DatabaseClientImpl : DatabaseClient {

    override suspend fun queryWaters(): List<WaterModel> {
        Realm.getDefaultInstance()?.use {
            val results = it.where<WaterModelDB>().sort("shortname").findAll()
            return results.convertWatersFromDB()
        }
        return emptyList()
    }

    override suspend fun queryStations(): List<StationModel> {
        Realm.getDefaultInstance()?.use {
            val results = it.where<StationModelDB>().sort("shortname").findAll()
            return results.convertStationsFromDB()
        }
        return emptyList()
    }

    override suspend fun saveWaters(watersToSave: List<WaterModel>) {
        Realm.getDefaultInstance()?.use {
            it.executeTransactionAsync {
                it.insertOrUpdate(watersToSave.convertWaters())
            }
        }
    }

    override suspend fun saveStations(stationsToSave: List<StationModel>) {
        Realm.getDefaultInstance()?.use {
            it.executeTransactionAsync {
                it.insertOrUpdate(stationsToSave.convertStations())
            }
        }
    }

    override suspend fun queryWaterForShortName(shortName: String): WaterModel {
        Realm.getDefaultInstance()?.use {
            val result = it.where<WaterModelDB>().equalTo("longname", shortName).findFirst()
            return result?.convert() ?: WaterModel()
        }
        return WaterModel()
    }

    override suspend fun queryStationsForWater(waterShortName: String): List<StationModel> {
        Realm.getDefaultInstance()?.use {
            val list =
                it.where<StationModelDB>().equalTo("water.shortname", waterShortName)
                    .sort("longname").findAll()
            return list.convertStationsFromDB()
        }
        return emptyList()
    }

    override suspend fun addStationsToWater(waterShortName: String, stations: List<StationModel>) {
        Realm.getDefaultInstance()?.use {
            it.executeTransaction {
                val waterDB =
                    it.where<WaterModelDB>().equalTo("shortname", waterShortName).findFirst()
                val list = RealmList<StationModelDB>()
                list.addAll(stations.convertStations())
                it.insertOrUpdate(list)
                val stationsDB =
                    it.where<StationModelDB>().`in`("uuid", stations.map { it.uuid }.toTypedArray())
                        .findAll()
                val newList = RealmList<StationModelDB>()
                newList.addAll(stationsDB)
                waterDB?.stations = newList
            }
        }
    }

    override suspend fun queryStationForUuid(uuid: String): StationModel {
        Realm.getDefaultInstance()?.use {
            val result = it.where<StationModelDB>().equalTo("uuid", uuid).findFirst()
            return result?.convert() ?: StationModel()
        }
        return StationModel()
    }
}