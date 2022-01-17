package com.example.cityaqi.network

import kotlinx.coroutines.channels.Channel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WebServiceProvider {

    private var webSocket: WebSocket? = null

    private val socketOkHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .hostnameVerifier { _, _ -> true }
        .build()

    private var mWebSocketListener: AppWebSocketListener? = null

    fun startSocket(webSocketListener: AppWebSocketListener) {
        mWebSocketListener = webSocketListener
        webSocket = socketOkHttpClient.newWebSocket(
            Request.Builder()
                .url("ws://city-ws.herokuapp.com/")
                .build(),
            webSocketListener
        )
    }

    fun startSocket(): Channel<SocketUpdate> =
        with(AppWebSocketListener()) {
            startSocket(this)
            this.socketEventChannel
        }


    fun stopSocket() {
        try {
            webSocket?.close(NORMAL_CLOSURE_STATUS, null)
            webSocket = null
            mWebSocketListener?.socketEventChannel?.close()
            mWebSocketListener = null
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}