package com.checki.home.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checki.R
import com.checki.core.data.NetService
import com.checki.core.extensions.bind
import com.checki.core.extensions.makeLongToast
import com.checki.home.viewmodels.NetServiceViewModel
import com.checki.home.viewmodels.TimerViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_SERVICE = 2000
    }

    private val serviceContainer: RecyclerView by bind(R.id.service_container)
    private val toolbar: Toolbar by bind(R.id.toolbar)
    private val fab: FloatingActionButton by bind(R.id.fab)

    // ViewModel instances
    private lateinit var serviceViewModel: NetServiceViewModel
    private lateinit var timerViewModel: TimerViewModel

    // Adapter and divider item
    private val serviceAdapter by lazy { NetServiceAdapter() }
    private val dividerItemDecoration by lazy {
        NetServiceDividerItem(ResourcesCompat.getDrawable(resources, R.drawable.divider_service, theme)!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        // Setup toolbar
        setSupportActionBar(toolbar)

        // Setup service container
        serviceContainer.layoutManager = LinearLayoutManager(applicationContext)
        serviceContainer.adapter = serviceAdapter
        serviceContainer.addItemDecoration(dividerItemDecoration)

        // Attach ViewModels to the activity life cycle
        serviceViewModel = ViewModelProviders.of(this).get(NetServiceViewModel(application)::class.java)
        timerViewModel = ViewModelProviders.of(this).get(TimerViewModel::class.java)
        subscribeServices()
        subscribeTimer()

        // Add click listener on FAB to insert a new service
        fab.setOnClickListener {
            // Launch with result the activity to create a new service
            startActivityForResult(Intent(
                this@HomeActivity, AddNetServiceActivity::class.java), REQUEST_CODE_SERVICE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == REQUEST_CODE_SERVICE && resultCode == RESULT_OK) {
            intentData?.let { data ->
                val netService = data.getParcelableExtra<NetService>(AddNetServiceActivity.EXTRA_SERVICE)
                serviceViewModel.insert(netService)
            }
        }
    }

    override fun onDetachedFromWindow() {
        serviceContainer.clearOnScrollListeners()
        serviceContainer.removeItemDecoration(dividerItemDecoration)
        serviceContainer.clearOnChildAttachStateChangeListeners()
        serviceContainer.adapter = null
        super.onDetachedFromWindow()
    }

    //region Internal methods

    private fun subscribeTimer() {
        val elapsedTimeObserver = Observer<Long> {
            // Ping all services
            serviceViewModel.pingAllServices(it)
        }

        timerViewModel.elapsedTime.observe(this, elapsedTimeObserver)
    }

    private fun subscribeServices() {
        val servicesObserver = Observer<List<NetService>> { servicesList ->
            // Update the adapter data
            servicesList?.let { serviceAdapter.setServices(it) }
        }

        serviceViewModel.allNetServices.observe(this, servicesObserver)
    }

    //endregion
}
