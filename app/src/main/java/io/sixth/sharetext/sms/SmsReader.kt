package io.sixth.sharetext.sms

import io.sixth.sharetext.data.Text

/**
 * Created by eve on 13/08/17.
 */
interface SmsReader {
    fun hasSMSPermission(): Boolean
    fun requestSMSPermission()
    fun requestSMSPermissionResult(requestCode: Int, permissions: Array<out String>,
                                   grantResults: IntArray)
    fun onSMSPermissionGrant()
    fun onSMSPermissionReject()
    fun getTexts(): List<Text>
}