package io.sixth.sharetext.main

import android.Manifest
import android.content.pm.PackageManager
import android.provider.Telephony
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import io.sixth.sharetext.data.Text

/**
 * Created by eve on 13/08/17.
 */
class SmsReaderImpl constructor(val act: AppCompatActivity) : SmsReader {

    val TAG = javaClass.canonicalName!!
    val APP_REQUEST_READ_SMS: Int = 0x100
    // Cursor position constants
    val CURSOR_SENDER   = 0
    val CURSOR_DATE     = 1
    val CURSOR_BODY     = 2

    override fun hasSMSPermission(): Boolean {
        val permissionCheck =
                ContextCompat.checkSelfPermission(act, Manifest.permission.READ_SMS)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    override fun requestSMSPermission() {
        if (!hasSMSPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(act, "For reading SMS", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.requestPermissions(act,
                        arrayOf(Manifest.permission.READ_SMS),
                        APP_REQUEST_READ_SMS)
            }
        }
    }

    override fun requestSMSPermissionResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            APP_REQUEST_READ_SMS -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onSMSPermissionGrant()
                } else {
                    onSMSPermissionReject()
                }
            }
        }
    }

    override fun onSMSPermissionGrant() {
        startTextServer()
    }

    override fun onSMSPermissionReject() {
        Toast.makeText(act, "SMS Permission denied", Toast.LENGTH_LONG).show()
    }

    override fun getTexts(): List<Text> {
        val sms = ArrayList<Text>()

        val cursor = act.contentResolver.query(Telephony.Sms.CONTENT_URI,
                arrayOf(Telephony.Sms.Inbox.ADDRESS,
                        Telephony.Sms.Inbox.DATE,
                        Telephony.Sms.Inbox.BODY),
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                sms.add(Text(
                        cursor.getString(CURSOR_SENDER),
                        cursor.getLong(CURSOR_DATE),
                        cursor.getString(CURSOR_BODY)
                ))
                cursor.moveToNext()
            }
        }

        cursor.close()
        return sms
    }

    override fun startTextServer() {
        val sms = getTexts()
        sms.forEach { s: Text -> Log.d(TAG, s.toString()) }
    }
}