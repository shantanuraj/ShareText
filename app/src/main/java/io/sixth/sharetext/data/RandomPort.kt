package io.sixth.sharetext.data

import java.security.SecureRandom

/**
 * Created by eve on 13/08/17.
 */
internal object RandomPort {

    private val Random = SecureRandom()

    val port: Int
        get() {
            val index = Random.nextInt(65535 - 1024)
            return PORTS[1024 + index]
        }
}
