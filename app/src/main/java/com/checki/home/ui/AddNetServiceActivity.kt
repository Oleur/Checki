package com.checki.home.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.checki.R
import com.checki.core.data.NetService
import com.checki.core.ui.bind
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.net.URL

/**
 * Activity for adding a new service to check
 */
class AddNetServiceActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SERVICE = "com.checki.home.ui.SERVICE"
    }

    // UI binding
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Back to home activity
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //region Internal methods

    private fun checkInputValidity(): Boolean {
        var check = true

        // name input must not be null or empty
        if (nameInput.text.isNullOrBlank()) {
            nameInput.error = getString(R.string.error_empty_text)
            check = false
        }

        // URL input must not be null or empty
        if (urlInput.text.isNullOrBlank()) {
            urlInput.error = getString(R.string.error_empty_text)
            check = false
        }

        // Check if the url is well formatted
        try {
            URL(urlInput.text.toString()).toURI()
        } catch (e: Exception) {
            urlInput.error = getString(R.string.error_wrong_url_format)
            check = false
        }

        return check
    }

    //endregion
}
