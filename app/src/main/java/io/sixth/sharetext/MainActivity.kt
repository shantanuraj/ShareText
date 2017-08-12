package io.sixth.sharetext

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import android.provider.Telephony
import android.util.Log
import io.sixth.sharetext.data.Text


class MainActivity : AppCompatActivity() {

    val APP_REQUEST_READ_SMS: Int = 0x100
    // Cursor position constants
    val CURSOR_SENDER   = 0
    val CURSOR_DATE     = 1
    val CURSOR_BODY     = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            APP_REQUEST_READ_SMS -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    processSMS()
                } else {
                    Toast.makeText(this, "No SMS Permission!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun hasSMSPermission(): Boolean {
        val permissionCheck =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    fun requestSMSPermission(): Unit {
        if (!hasSMSPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(applicationContext, "For reading SMS", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Requesting SMS Permission!", Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_SMS),
                        APP_REQUEST_READ_SMS)
            }
        } else {
            processSMS()
        }
    }

    fun onInitClick(view: View) {
        requestSMSPermission()
    }

    fun processSMS(): Unit {
        val sms = getAllSMS()
        sms.forEach { s: Text -> Log.d("NETFLIX", s.toString()) }
    }

    fun getAllSMS(): ArrayList<Text> {
        val sms = ArrayList<Text>()
        val cursor = contentResolver.query(Telephony.Sms.CONTENT_URI,
                arrayOf(Telephony.Sms.Inbox.ADDRESS,
                        Telephony.Sms.Inbox.DATE,
                        Telephony.Sms.Inbox.BODY),
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER)

        val smsCount = cursor!!.count

        if (cursor.moveToFirst()) {
            for (i in 0..smsCount - 1) {
                sms.add(Text(
                        cursor.getString(CURSOR_SENDER),
                        cursor.getLong(CURSOR_DATE),
                        cursor.getString(CURSOR_BODY)
                ))
                cursor.moveToNext()
            }
        } else {
            Log.d("NETFLIX", "Read fail")
        }

        cursor.close()
        return sms
    }
}
