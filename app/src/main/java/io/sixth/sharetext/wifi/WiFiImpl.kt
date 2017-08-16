package io.sixth.sharetext.wifi

import android.content.Context
import android.net.wifi.WifiManager
import android.support.v7.app.AppCompatActivity
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder

/**
 * Created by eve on 16/08/17.
 */
class WiFiImpl constructor(private val act: AppCompatActivity) : WiFi {

    override fun getLocalIP(): String {
        val wm = act.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ip = wm.connectionInfo.ipAddress

        val formattedIp = when {
            ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN -> Integer.reverseBytes(ip)
            else -> ip
        }

        val ipArray = BigInteger.valueOf(formattedIp.toLong()).toByteArray()

        return try {
            InetAddress.getByAddress(ipArray).hostAddress
        } catch (ex: UnknownHostException) {
            ""
        }
    }
}