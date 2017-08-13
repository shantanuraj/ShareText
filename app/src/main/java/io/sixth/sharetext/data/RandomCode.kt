package io.sixth.sharetext.data

import java.security.SecureRandom

/**
 * Created by eve on 13/08/17.
 */
internal object RandomCode {

    val CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    val LENGTH = 4
    val Random = SecureRandom()

    val code: String
        get() {
            val code = StringBuilder()
            for (i in 1..LENGTH) {
                val index = Random.nextInt(CHARACTERS.size)
                code.append(CHARACTERS[index])
            }
            return code.toString()
        }
}