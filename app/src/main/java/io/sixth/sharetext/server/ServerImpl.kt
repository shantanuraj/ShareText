package io.sixth.sharetext.server

import com.koushikdutta.async.http.server.AsyncHttpServer
import io.sixth.sharetext.data.JsonText
import io.sixth.sharetext.data.RandomCode
import io.sixth.sharetext.data.Text

/**
 * Created by eve on 13/08/17.
 */
class ServerImpl : Server {

    val PORT = 3210
    val server: AsyncHttpServer = AsyncHttpServer()

    var code = ""
    var isActive = false

    init {
        server.get("/", { _, response -> response.send("ShareText server") })
    }

    override fun start(texts: List<Text>): String {
        val jsonTexts = JsonText.parse(texts)
        server.get("/texts", { _, response -> response.send(jsonTexts) })
        server.listen(PORT)
        isActive = true
        code = RandomCode.code
        return code
    }

    override fun stop() {
        server.stop()
        isActive = false
        code = ""
    }

    override fun isRunning(): Boolean {
        return isActive
    }
}