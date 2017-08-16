package io.sixth.sharetext.data

import java.security.SecureRandom

/**
 * Created by eve on 13/08/17.
 */
internal object RandomPort {

    private val PORTS = (1024..65535).toList()
    private val Random = SecureRandom()

    val port: Int
        get() {
            val index = Random.nextInt(PORTS.size)
            return PORTS[index]
        }
}