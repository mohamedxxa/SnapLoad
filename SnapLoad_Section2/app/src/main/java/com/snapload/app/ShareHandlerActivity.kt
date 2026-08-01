package com.snapload.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ShareHandlerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_handler)

        val tvShareLink = findViewById<TextView>(R.id.tvShareLink)
        val sharedLink = extractSharedLink()
        tvShareLink.text = sharedLink ?: getString(R.string.label_no_link_found)

        Handler(Looper.getMainLooper()).postDelayed({
            forwardToMain(sharedLink)
        }, 1200L)
    }

    private fun extractSharedLink(): String? {
        return when {
            intent?.action == Intent.ACTION_SEND && intent.type == "text/plain" ->
                intent.getStringExtra(Intent.EXTRA_TEXT)
            else -> null
        }
    }

    private fun forwardToMain(link: String?) {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_SHARED_LINK, link)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(mainIntent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    companion object {
        const val EXTRA_SHARED_LINK = "extra_shared_link"
    }
}
