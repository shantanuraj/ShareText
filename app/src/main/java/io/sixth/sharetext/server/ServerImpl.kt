package io.sixth.sharetext.server

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.koushikdutta.async.http.server.AsyncHttpServer
import io.sixth.sharetext.R
import io.sixth.sharetext.data.Text

/**
 * Created by eve on 13/08/17.
 */
class ServerImpl constructor(val act: AppCompatActivity) : Server {

    val PORT = 3210
    var isActive = false

    val server: AsyncHttpServer = AsyncHttpServer()

    init {
        server.get("/", { _, response -> response.send("ShareText server") })
    }

    override fun start(texts: List<Text>) {
        server.get("/texts", { _, response -> response.send(texts.toString()) })
        server.listen(PORT)
        isActive = true

        val view = act.findViewById<View>(R.id.main_root_view)

        Snackbar.make(view,
                "Open https://texts.sixth.io",
                Snackbar.LENGTH_LONG)
                .show()
    }

    override fun stop() {
        server.stop()
        isActive = false
    }

    override fun isRunning(): Boolean {
        return isActive
    }
}