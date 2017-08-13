package io.sixth.sharetext.server

import com.koushikdutta.async.http.server.AsyncHttpServer
import io.sixth.sharetext.data.Text

/**
 * Created by eve on 13/08/17.
 */
class ServerImpl : Server {

    val PORT = 3210

    val server: AsyncHttpServer = AsyncHttpServer()

    init {
        server.get("/", { _, response -> response.send("ShareText server") })
    }

    override fun start(texts: List<Text>) {
        server.get("/texts", { _, response -> response.send(texts.toString()) })
        server.listen(PORT)
    }

    override fun stop() {
        server.stop()
    }
}