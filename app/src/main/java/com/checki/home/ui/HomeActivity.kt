package com.checki.home.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checki.R
import com.checki.core.data.NetService
import com.checki.core.ui.bind
import com.checki.home.viewmodels.NetServiceViewModel
import com.checki.home.viewmodels.TimerViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Activity that display all services stored in the database.
 * Allow the user to add new ones or remove existing service directly in the list by swiping left.
 */
class HomeActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_SERVICE = 2000
    }

    // UI binding
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

        // Add Swipe to delete to the recyclerview
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val netServiceToDelete = serviceViewModel.allNetServices.value?.get(pos - 1)

                // Remove service from the recyclerview
                serviceAdapter.deleteService(pos)

                // Delete service in database
                serviceViewModel.delete(netServiceToDelete)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(serviceContainer)

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
        val checkTimeObserver = Observer<Long> {
            // Ping all services
            serviceViewModel.pingAllServices(it)
        }

        val timeObserver = Observer<Long> {
            // Refresh the list to display the timestamp in real time
            serviceAdapter.notifyDataSetChanged()
        }

        timerViewModel.liveDataCheckTime.observe(this, checkTimeObserver)
        timerViewModel.liveDataTime.observe(this, timeObserver)
    }

    private fun subscribeServices() {
        val servicesObserver = Observer<MutableList<NetService>> { servicesList ->
            // Update the adapter data
            servicesList?.let { serviceAdapter.setServices(it) }
        }

        serviceViewModel.allNetServices.observe(this, servicesObserver)
    }

    //endregion
}
