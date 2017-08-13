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

    val server: AsyncHttpServer = AsyncHttpServer()

    init {
        server.get("/", { _, response -> response.send("ShareText server") })
    }

    override fun start(texts: List<Text>) {
        server.get("/texts", { _, response -> response.send(texts.toString()) })
        server.listen(PORT)

        val view = act.findViewById<View>(R.id.main_root_view)

        Snackbar.make(view,
                "Server started, open https://texts.sixth.io on your desktop",
                Snackbar.LENGTH_LONG)
                .show()
    }

    override fun stop() {
        server.stop()
    }
}