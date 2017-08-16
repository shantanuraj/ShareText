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
class MainViewImpl constructor(private val act: AppCompatActivity,
                               private val sms: SmsReader,
                               private val server: Server): MainView {
    private val rootView = act.findViewById<ConstraintLayout>(R.id.main_root_view)!!
    private val initButton = act.findViewById<Button>(R.id.init_button)!!
    private val bannerText = act.findViewById<TextView>(R.id.text_banner)!!

    init {
        bannerText.textSize = act.resources.getDimension(R.dimen.regular_text)
        initButton.setOnClickListener { onInitClick() }
    }

    private fun startServer() {
        val code = server.start({
            sms.getTexts()
        })
        onServerStart(code)
    }

    private fun stopServer() {
        server.stop()
        onServerStop()
    }

    override fun onServerStart(code: String) {
        bannerText.textSize = act.resources.getDimension(R.dimen.large_text)
        bannerText.text = code
        initButton.text = act.getString(R.string.button_init_stop)
        showSnackbar(act.getString(R.string.text_server_start))
    }

    override fun onServerStop() {
        bannerText.textSize = act.resources.getDimension(R.dimen.regular_text)
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

    override fun showSnackbar(text: String) {
        Snackbar.make(rootView,
                text,
                Snackbar.LENGTH_LONG)
                .show()
    }
}