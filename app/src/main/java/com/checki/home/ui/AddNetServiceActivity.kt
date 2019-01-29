package com.checki.home.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.checki.R
import com.checki.core.data.NetService
import com.checki.core.extensions.bind
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddNetServiceActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SERVICE = "com.checki.home.ui.SERVICE"
    }

    private val nameInput: TextInputEditText by bind(R.id.add_service_name)
    private val urlInput: TextInputEditText by bind(R.id.add_service_url)
    private val validateBtn: MaterialButton by bind(R.id.add_service_validate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        // Setup action bar
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Add click listener to validate button
        validateBtn.setOnClickListener {
            if (checkInputValidity()) {
                val currentTime = System.currentTimeMillis()
                val netService = NetService(
                    name = nameInput.text.toString(),
                    url = urlInput.text.toString(),
                    createdAt = currentTime,
                    lastCheckedAt = currentTime
                )
                setResult(RESULT_OK, Intent().putExtra(EXTRA_SERVICE, netService))
                supportFinishAfterTransition()
            }
        }
    }

    //region Internal methods

    private fun checkInputValidity(): Boolean {
        var check = true
        if (nameInput.text.isNullOrBlank()) {
            nameInput.error = getString(R.string.error_empty_text)
            check = false
        }

        if (urlInput.text.isNullOrBlank()) {
            urlInput.error = getString(R.string.error_empty_text)
            check = false
        }

        return check
    }

    //endregion
}
