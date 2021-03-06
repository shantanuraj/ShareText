package io.sixth.sharetext

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.sixth.sharetext.server.Server
import io.sixth.sharetext.server.ServerImpl
import io.sixth.sharetext.sms.SmsReader
import io.sixth.sharetext.sms.SmsReaderImpl
import io.sixth.sharetext.view.MainView
import io.sixth.sharetext.view.MainViewImpl
import io.sixth.sharetext.wifi.WiFi
import io.sixth.sharetext.wifi.WiFiImpl


class MainActivity : AppCompatActivity() {

    private lateinit var sms: SmsReader
    private lateinit var wifi: WiFi
    private lateinit var server: Server
    private lateinit var mainView: MainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        server = ServerImpl()
        wifi = WiFiImpl(this)
        sms = SmsReaderImpl(this)

        setContentView(R.layout.activity_main)
        mainView = MainViewImpl(this, sms, server, wifi)
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
        mainView.onServerStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        sms.requestSMSPermissionResult(requestCode, permissions, grantResults)
    }
}
