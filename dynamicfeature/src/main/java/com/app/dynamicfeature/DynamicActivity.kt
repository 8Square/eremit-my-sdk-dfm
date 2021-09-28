package com.app.dynamicfeature

import android.os.Bundle
import android.widget.Button
import com.app.mtaeremit.BaseSplitActivity
import com.eightsquarei.eremit.EremitSdk
import com.library.eightsquarei.model.EnvType

class DynamicActivity : BaseSplitActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)

        findViewById<Button>(R.id.startEremitButton).setOnClickListener {
            startEremit()
        }

    }

    private fun startEremit() {
        EremitSdk.Builder()
            .apiKey("API_KEY_HERE")
            .envType(EnvType.SIT)
            .build().start(this)
        finish()
    }
}