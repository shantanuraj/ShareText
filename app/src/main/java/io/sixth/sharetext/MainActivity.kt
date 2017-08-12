package io.sixth.sharetext

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import io.sixth.sharetext.main.SmsReader
import io.sixth.sharetext.main.SmsReaderImpl


class MainActivity : AppCompatActivity() {

    lateinit var impl: SmsReader
    lateinit var initButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        impl = SmsReaderImpl(this)
        setContentView(R.layout.activity_main)
        initButton = findViewById(R.id.init_button)
        initButton.setOnClickListener { startTextServer() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        impl.requestSMSPermissionResult(requestCode, permissions, grantResults)
    }

    fun startTextServer() {
        if (impl.hasSMSPermission()) {
            impl.startTextServer()
        } else {
            impl.requestSMSPermission()
        }
    }
}
