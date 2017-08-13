package io.sixth.sharetext.view

import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import io.sixth.sharetext.R
import io.sixth.sharetext.server.Server
import io.sixth.sharetext.sms.SmsReader

/**
 * Created by eve on 13/08/17.
 */
class MainViewImpl constructor(act: AppCompatActivity,
                               val sms: SmsReader,
                               val server: Server): MainView {
    val initButton = act.findViewById<Button>(R.id.init_button)!!
    val rootView = act.findViewById<ConstraintLayout>(R.id.main_root_view)!!

    init {
        initButton.setOnClickListener { onInitClick() }
    }

    override fun onInitClick() {
        if (!server.isRunning() && sms.hasSMSPermission()) {
            server.start(sms.getTexts())
        } else {
            sms.requestSMSPermission()
        }
    }

    override fun onServerStart(code: String) {
        Snackbar.make(rootView,
                "Secure code: $code",
                Snackbar.LENGTH_LONG)
                .show()
    }
}