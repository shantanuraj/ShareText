package io.sixth.sharetext.sms

import android.Manifest
import android.content.pm.PackageManager
import android.provider.Telephony
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.sixth.sharetext.data.Text

/**
 * Created by eve on 13/08/17.
 */
class SmsReaderImpl constructor(val act: AppCompatActivity) : SmsReader {

    val APP_REQUEST_READ_SMS: Int = 0x100
    // Cursor position constants
    val CURSOR_ADDRESS  = 0
    val CURSOR_DATE     = 1
    val CURSOR_BODY     = 2
    val CURSOR_THREAD   = 3
    val CURSOR_TYPE     = 4
    // Callback for sms permission grant action
    var cb: () -> Unit = {}


    override fun hasSMSPermission(): Boolean {
        val permissionCheck =
                ContextCompat.checkSelfPermission(act, Manifest.permission.READ_SMS)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    override fun requestSMSPermission(cb: () -> Unit) {
        this.cb = cb
        if (!hasSMSPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(act, "Allow permission to read SMS", Toast.LENGTH_LONG).show()
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
        cb()
    }

    override fun onSMSPermissionReject() {
        Toast.makeText(act, "SMS permission denied", Toast.LENGTH_LONG).show()
    }

    override fun getTexts(): List<Text> {
        val sms = ArrayList<Text>()

        val cursor = act.contentResolver.query(Telephony.Sms.CONTENT_URI,
                arrayOf(Telephony.Sms.Inbox.ADDRESS,
                        Telephony.Sms.Inbox.DATE,
                        Telephony.Sms.Inbox.BODY,
                        Telephony.Sms.Inbox.THREAD_ID,
                        Telephony.Sms.Inbox.TYPE
                        ),
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                sms.add(Text(
                        cursor.getString(CURSOR_ADDRESS),
                        cursor.getLong(CURSOR_DATE),
                        cursor.getString(CURSOR_BODY),
                        cursor.getString(CURSOR_THREAD),
                        cursor.getInt(CURSOR_TYPE) == 2
                ))
                cursor.moveToNext()
            }
        }

        cursor.close()
        return sms
    }
}