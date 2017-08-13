package io.sixth.sharetext.view

import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import io.sixth.sharetext.R
import io.sixth.sharetext.server.Server
import io.sixth.sharetext.sms.SmsReader

/**
 * Created by eve on 13/08/17.
 */
class MainViewImpl constructor(val act: AppCompatActivity,
                               val sms: SmsReader,
                               val server: Server): MainView {
    val initButton = act.findViewById<Button>(R.id.init_button)!!
    val rootView = act.findViewById<ConstraintLayout>(R.id.main_root_view)!!
    val bannerText = act.findViewById<TextView>(R.id.text_banner)!!

    init {
        initButton.setOnClickListener { onInitClick() }
    }

    fun startServer() {
        val code = server.start(sms.getTexts())
        onServerStart(code)
    }

    fun stopServer() {
        server.stop()
        bannerText.text = act.getString(R.string.text_banner_prompt)
        initButton.text = act.getString(R.string.button_init_start)
    }

    override fun onInitClick() {
        when {
            server.isRunning() -> stopServer()
            sms.hasSMSPermission() -> startServer()
            else -> sms.requestSMSPermission({ startServer() })
        }
    }

    override fun onServerStart(code: String) {
        bannerText.text = code
        initButton.text = act.getString(R.string.button_init_stop)
        showSnackbar("Open https://texts.sixth.io")
    }

    override fun showSnackbar(text: String) {
        Snackbar.make(rootView,
                text,
                Snackbar.LENGTH_LONG)
                .show()
    }
}