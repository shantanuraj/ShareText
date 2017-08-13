package io.sixth.sharetext

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import io.sixth.sharetext.sms.SmsReader
import io.sixth.sharetext.sms.SmsReaderImpl
import io.sixth.sharetext.server.Server
import io.sixth.sharetext.server.ServerImpl


class MainActivity : AppCompatActivity() {

    lateinit var smsReader: SmsReader
    lateinit var server: Server
    lateinit var initButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        server = ServerImpl(this)
        smsReader = SmsReaderImpl(this, server)
        setContentView(R.layout.activity_main)
        initButton = findViewById(R.id.init_button)
        initButton.setOnClickListener { startTextServer() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        smsReader.requestSMSPermissionResult(requestCode, permissions, grantResults)
    }

    fun startTextServer() {
        if (smsReader.hasSMSPermission()) {
            server.start(smsReader.getTexts())
        } else {
            smsReader.requestSMSPermission()
        }
    }
}
