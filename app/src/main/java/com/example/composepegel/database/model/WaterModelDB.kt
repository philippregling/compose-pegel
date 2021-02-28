package com.example.composepegel.database.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class WaterModelDB(
    @PrimaryKey
    @Index
    var shortname: String = "",
    var longname: String = "",
    var stations: RealmList<StationModelDB> = RealmList()
) : RealmObject()