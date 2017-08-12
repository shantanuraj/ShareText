package io.sixth.sharetext

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onInitClick(view: View) {
        Toast.makeText(this, "Hello, world", Toast.LENGTH_LONG).show()
    }
}
