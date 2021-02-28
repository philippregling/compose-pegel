package com.example.composepegel.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log

interface NetworkStateClient {

    fun register(context: Context)

    fun unregister(context: Context)

    var isOffline: Boolean
}

class NetworkStateClientImpl() : NetworkStateClient {

    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            isOffline = false
        }

        override fun onLost(network: Network) {
            isOffline = true
        }
    }

    override fun register(context: Context) {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(NetworkRequest.Builder().build(), callback)
        isOffline = cm.activeNetwork == null
    }

    override fun unregister(context: Context) {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(callback)
    }

    override var isOffline: Boolean = false
}