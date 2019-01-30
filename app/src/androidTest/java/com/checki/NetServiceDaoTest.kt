package com.checki

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.checki.core.data.CheckiDatabase
import com.checki.core.data.NetService
import com.checki.core.data.NetServiceDao
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Database tests
 */
@RunWith(AndroidJUnit4::class)
class NetServiceDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var netServiceDao: NetServiceDao
    private lateinit var db: CheckiDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, CheckiDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        netServiceDao = db.netServiceDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetService() {
        val service1 = NetService("s1", "https://www.livi.fr/", 200, 0L, 0L)
        netServiceDao.insertAll(service1)
        val allServices = netServiceDao.getAll().waitForValue()
        assertEquals(allServices[0].name, service1.name)
    }

    @Test
    @Throws(Exception::class)
    fun getAllServices() {
        val service1 = NetService("s1", "https://www.livi.fr/", 200, 0L, 0L)
        val service2 = NetService("s2", "https://www.kry.se/", 200, 0L, 0L)
        netServiceDao.insertAll(service1, service2)
        val allServices = netServiceDao.getAll().waitForValue()
        assertEquals(allServices[0].name, service1.name)
        assertEquals(allServices[1].name, service2.name)
    }

    @Test
    @Throws(Exception::class)
    fun deleteOneService() {
        val service1 = NetService("s1", "https://www.livi.fr/", 200, 0L, 0L)
        val service2 = NetService("s2", "https://www.kry.se/", 200, 0L, 0L)
        netServiceDao.insertAll(service1, service2)
        netServiceDao.delete(service1)
        val allServices = netServiceDao.getAll().waitForValue()
        assertTrue(allServices.size == 1)
    }
}
