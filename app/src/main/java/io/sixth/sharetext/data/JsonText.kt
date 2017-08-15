package io.sixth.sharetext.data

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by eve on 13/08/17.
 */
internal object JsonText {
    fun parse(text: Text): JSONObject {
        val json = JSONObject()
        json.put("thread", text.thread)
        json.put("address", text.address)
        json.put("message", text.message)
        json.put("date", text.date)
        json.put("sent", text.sent)
        return json
    }

    fun parse(texts: List<Text>): JSONObject {
        val json = JSONObject()
        val messages = JSONArray()

        val jsonTexts = texts.map { text -> JsonText.parse(text) }
        jsonTexts.forEach { jsonText -> messages.put(jsonText) }

        json.put("texts", messages)
        return json
    }
}