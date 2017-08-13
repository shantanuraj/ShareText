package io.sixth.sharetext.server

import com.koushikdutta.async.http.server.AsyncHttpServer

/**
 * Created by eve on 13/08/17.
 */
class ServerImpl : Server {

    val PORT = 3210

    val server: AsyncHttpServer = AsyncHttpServer()

    init {
        server.get("/", { _, response -> response.send("Hello, world!") })
    }

    override fun start() {
        server.listen(PORT)
    }

    override fun stop() {
        server.stop()
    }
}