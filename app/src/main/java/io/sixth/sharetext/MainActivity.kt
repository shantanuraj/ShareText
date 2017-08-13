package io.sixth.sharetext

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.sixth.sharetext.server.Server
import io.sixth.sharetext.server.ServerImpl
import io.sixth.sharetext.sms.SmsReader
import io.sixth.sharetext.sms.SmsReaderImpl
import io.sixth.sharetext.view.MainView
import io.sixth.sharetext.view.MainViewImpl


class MainActivity : AppCompatActivity() {

    lateinit var sms: SmsReader
    lateinit var server: Server
    lateinit var mainView: MainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        server = ServerImpl(this)
        sms = SmsReaderImpl(this, server)

        setContentView(R.layout.activity_main)
        mainView = MainViewImpl(this, sms, server)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        sms.requestSMSPermissionResult(requestCode, permissions, grantResults)
    }
}
