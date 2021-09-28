package com.app.mtaeremit

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mySessionId = 0

    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pref = applicationContext.getSharedPreferences("remit_pref", MODE_PRIVATE)
        editor = pref?.edit()


        downloadButton.setOnClickListener {
            downloadDynamicModule()
        }

        startButton.setOnClickListener {

            if (pref?.getBoolean(PREFS_IS_DFM_DOWNLOADED, false) != true) {
                Toast.makeText(this@MainActivity, "Please click the download button first", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val intent = Intent()
            intent.setClassName("com.app.mtaeremit", "com.app.dynamicfeature.DynamicActivity")
            startActivity(intent)
        }
    }


    private fun downloadDynamicModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val request = SplitInstallRequest
            .newBuilder()
            .addModule("dynamicfeature")
            .build()
        val listener = SplitInstallStateUpdatedListener { splitInstallSessionState ->
            if (splitInstallSessionState.sessionId() == mySessionId) {
                when (splitInstallSessionState.status()) {
                    SplitInstallSessionStatus.INSTALLED -> {
                        editor?.putBoolean(PREFS_IS_DFM_DOWNLOADED, true)
                        editor?.apply()

                        Toast.makeText(this, "Dynamic Module downloaded", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        splitInstallManager.registerListener(listener)
        splitInstallManager.startInstall(request)
            .addOnFailureListener { e -> Log.d("TAG", "Exception: $e") }
            .addOnSuccessListener { sessionId -> mySessionId = sessionId }
    }

    companion object {
        private val PREFS_IS_DFM_DOWNLOADED = "prefs_dfm_download"
    }

}