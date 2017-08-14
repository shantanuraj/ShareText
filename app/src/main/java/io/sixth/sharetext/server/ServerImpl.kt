package io.sixth.sharetext.server

import android.util.Log
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import io.sixth.sharetext.data.JsonText
import io.sixth.sharetext.data.RandomCode
import io.sixth.sharetext.data.Text

/**
 * Created by eve on 13/08/17.
 */
class ServerImpl : Server {

    val PORT = 3210
    val FORBIDDEN = "Forbidden"
    val FORBIDDEN_CODE = 403
    val OPTIONS = "OPTIONS"
    val OK = "ok"
    var CODE_HEADER = "x-text-code"
    val server: AsyncHttpServer = AsyncHttpServer()

    var code = ""
    var isActive = false

    init {
        server.addAction(OPTIONS, "/*", { req, res ->
            authMiddleware(req, res)
        })
        server.get("/", { req, res ->
            when {
                authMiddleware(req, res) -> res.send("ShareText server")
            }
        })
    }

    fun authMiddleware(request: AsyncHttpServerRequest,
                       response: AsyncHttpServerResponse): Boolean {
        val codeHeader = request.headers.get(CODE_HEADER)

        return when {
            request.method == OPTIONS -> {
                response.send(OK)
                false
            }
            (codeHeader != null && codeHeader == code) -> true
            else -> {
                response.code(FORBIDDEN_CODE)
                response.send(FORBIDDEN)
                false
            }
        }
    }

    override fun start(texts: List<Text>): String {
        val jsonTexts = JsonText.parse(texts)
        server.get("/texts", { req, res ->
            when {
                authMiddleware(req, res) -> res.send(jsonTexts)
            }
        })
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