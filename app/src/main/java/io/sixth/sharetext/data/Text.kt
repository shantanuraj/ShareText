package io.sixth.sharetext.data

/**
 * Created by eve on 13/08/17.
 */
data class Text(val address: String,
                val date: Long,
                val message: String,
                val thread: Int,
                val sent: Boolean)