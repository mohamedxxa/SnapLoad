package com.snapload.app

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    private val splashDelayMillis = 2000L
    private val requiredPermissions = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private val permissionLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { /* النتيجة تُعالج بعد المهلة بغض النظر عن الاستجابة */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val tvAppName = findViewById<TextView>(R.id.tvAppName)
        val tvTagline = findViewById<TextView>(R.id.tvTagline)

        runFadeInAnimation(ivLogo, tvAppName, tvTagline)
        requestNecessaryPermissions()

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMain()
        }, splashDelayMillis)
    }

    private fun runFadeInAnimation(vararg views: android.view.View) {
        val animators = views.mapIndexed { index, view ->
            ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
                duration = 600
                startDelay = index * 150L
            }
        }
        AnimatorSet().apply {
            playTogether(animators)
            start()
        }
    }

    private fun requestNecessaryPermissions() {
        val missing = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isNotEmpty()) {
            permissionLauncher.launch(missing.toTypedArray())
        }
    }

    private fun navigateToMain() {
        if (isFinishing) return
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
