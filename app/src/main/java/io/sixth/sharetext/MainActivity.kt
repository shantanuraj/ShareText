package io.sixth.sharetext

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.sixth.sharetext.main.SmsReader
import io.sixth.sharetext.main.SmsReaderImpl


class MainActivity : AppCompatActivity() {

    lateinit var impl: SmsReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        impl = SmsReaderImpl(this)
        setContentView(R.layout.activity_main)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        impl.requestSMSPermissionResult(requestCode, permissions, grantResults)
    }

    fun onInitClick(view: View) {
        if (impl.hasSMSPermission()) {
            impl.onSMSPermissionGrant()
        } else {
            impl.requestSMSPermission()
        }
    }
}
