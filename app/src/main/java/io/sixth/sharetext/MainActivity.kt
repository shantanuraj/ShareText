package io.sixth.sharetext

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val APP_REQUEST_READ_SMS: Int = 0x100

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
                    Toast.makeText(this, "Got SMS Permission!", Toast.LENGTH_LONG).show()
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
            Toast.makeText(this, "Has SMS Permission!", Toast.LENGTH_LONG).show()
        }
    }

    fun onInitClick(view: View) {
        requestSMSPermission()
    }
}
