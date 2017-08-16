package io.sixth.sharetext.server

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
    val CODE_HEADER = "x-text-code"
    val METHODS_HEADER = "Access-Control-Allow-Methods"
    val ORIGINS_HEADER = "Access-Control-Allow-Origin"
    val HEADERS_HEADER = "Access-Control-Allow-Headers"
    val ALL_METHODS = "GET,HEAD,PUT,PATCH,POST,DELETE"
    val ALL_ORIGINS = "*"
    val server: AsyncHttpServer = AsyncHttpServer()

    var code = ""
    var isActive = false

    init {
        server.addAction(OPTIONS, "\\S+", { req, res ->
            authMiddleware(req, res)
        })
        server.get("/", { req, res ->
            when {
                authMiddleware(req, res) -> res.send(code)
            }
        })
    }

    private fun authMiddleware(request: AsyncHttpServerRequest,
                               response: AsyncHttpServerResponse): Boolean {
        val codeHeader = request.headers.get(CODE_HEADER)

        response.headers.set(METHODS_HEADER, ALL_METHODS)
        response.headers.set(ORIGINS_HEADER, ALL_ORIGINS)
        response.headers.set(HEADERS_HEADER, CODE_HEADER)

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

    override fun start(getTexts: () -> List<Text>): String {

        server.get("/texts", { req, res ->
            when {
                authMiddleware(req, res) -> res.send(JsonText.parse(getTexts()))
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

    override fun getPort(): Int {
        return PORT
    }
}