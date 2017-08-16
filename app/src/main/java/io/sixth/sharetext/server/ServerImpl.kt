package io.sixth.sharetext.server

import com.koushikdutta.async.AsyncServerSocket
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import io.sixth.sharetext.data.JsonText
import io.sixth.sharetext.data.RandomCode
import io.sixth.sharetext.data.RandomPort
import io.sixth.sharetext.data.Text

/**
 * Created by eve on 13/08/17.
 */
class ServerImpl : Server {

    private val FORBIDDEN = "Forbidden"
    private val FORBIDDEN_CODE = 403
    private val OPTIONS = "OPTIONS"
    private val OK = "ok"
    private val CODE_HEADER = "x-text-code"
    private val METHODS_HEADER = "Access-Control-Allow-Methods"
    private val ORIGINS_HEADER = "Access-Control-Allow-Origin"
    private val HEADERS_HEADER = "Access-Control-Allow-Headers"
    private val ALL_METHODS = "GET,HEAD,PUT,PATCH,POST,DELETE"
    private val ALL_ORIGINS = "*"
    private val server: AsyncHttpServer = AsyncHttpServer()

    private var port = 0
    private var code = ""
    private var isActive = false

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

    private fun startServer(): AsyncServerSocket {
        return try {
            port = RandomPort.port
            server.listen(port)
        } catch (ex: Exception) {
            startServer()
        }
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
        startServer()
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
        return port
    }
}