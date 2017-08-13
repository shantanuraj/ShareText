package io.sixth.sharetext.server

import io.sixth.sharetext.data.Text

/**
 * Created by eve on 13/08/17.
 */
interface Server {
    fun start(texts: List<Text>)
    fun stop()
}