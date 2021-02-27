package com.example.composepegel.util

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.example.composepegel.R
import com.example.composepegel.model.StationModel

fun AppCompatActivity.shareStationDetails(stationModel: StationModel) {
    ShareCompat.IntentBuilder(this)
        .setHtmlText(stationModel.toString())
        .setChooserTitle(getString(R.string.share_details))
        .setType("text/plain")
        .startChooser()
}