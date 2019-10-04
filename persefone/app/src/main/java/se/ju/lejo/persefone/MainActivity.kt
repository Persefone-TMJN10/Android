package se.ju.lejo.persefone

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.root.bluetoothtester.Bluetooth.Connection.BluetoothConnection
import com.root.bluetoothtester.Bluetooth.Connection.ClientThread
import com.root.bluetoothtester.Bluetooth.Messaging.MessageReader
import com.root.bluetoothtester.Bluetooth.Streaming.ServiceController
import se.ju.lejo.persefone.Bluetooth.BluetoothHandler
import se.ju.lejo.persefone.Fragments.ConnectToBTFragment
import se.ju.lejo.persefone.Fragments.HistoryFragment
import se.ju.lejo.persefone.Fragments.TimerFragment
import se.ju.lejo.persefone.Main.MainPagerAdapter
import se.ju.lejo.persefone.Main.MainScreen
import se.ju.lejo.persefone.Main.getMainScreenForMenuItem
import java.util.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    // SERVICE
    var serviceController: ServiceController? = null

    // RECEIVERS
    var receiverClientThreadStatusChanged: BroadcastReceiver? = null
    var receiverBluetoothFoundDevice: BroadcastReceiver? = null

    // BLUETOOTH
    var bluetoothConnection: BluetoothConnection = BluetoothConnection(this)

    // MESSAGING
    var messageReader: MessageReader? = null

    // GUI
    var timerFragment: TimerFragment? = null
    var connectToBTFragment: ConnectToBTFragment? = null
    var historyFragment: HistoryFragment? = null

    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mainPagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupReceivers()
        serviceController = ServiceController(this)

        setUpNavBar()
    }

    private fun setUpNavBar() {
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)

        mainPagerAdapter.setItems(arrayListOf(MainScreen.CONNECT, MainScreen.TIMER, MainScreen.HISTORY))

        val defaultScreen = MainScreen.CONNECT
        scrollToScreen(defaultScreen)
        selectBottomNavigationViewMenuItem(defaultScreen.menuItemId)
        supportActionBar?.setTitle(defaultScreen.titleStringId)

        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        viewPager.adapter = mainPagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                val selectedScreen = mainPagerAdapter.getItems()[position]
                selectBottomNavigationViewMenuItem(selectedScreen.menuItemId)
                supportActionBar?.setTitle(selectedScreen.titleStringId)

                if (position == 2) {
                    println("PAGE 2")
                    historyFragment?.updateRecycleViewAdapter()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(baseContext).unregisterReceiver(receiverClientThreadStatusChanged!!)
        baseContext.unregisterReceiver(receiverBluetoothFoundDevice)

    }

    /**
     * Scrolls ViewPager to show the provided screen.
     */
    private fun scrollToScreen(mainScreen: MainScreen) {
        val screenPosition = mainPagerAdapter.getItems().indexOf(mainScreen)
        if (screenPosition != viewPager.currentItem) {
            viewPager.currentItem = screenPosition
        }
    }

    /**
     * Selects the specified item in the bottom navigation view.
     */
    private fun selectBottomNavigationViewMenuItem(@IdRes menuItemId: Int) {
        bottomNavigationView.setOnNavigationItemSelectedListener(null)
        bottomNavigationView.selectedItemId = menuItemId
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    /**
     * Listener implementation for registering bottom navigation clicks.
     */
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        getMainScreenForMenuItem(menuItem.itemId)?.let {
            scrollToScreen(it)
            supportActionBar?.setTitle(it.titleStringId)
            return true
        }
        return false
    }

    private fun constructFragments() {
        connectToBTFragment = ConnectToBTFragment()
        historyFragment = HistoryFragment()
    }

    fun startConnection() {
        BluetoothHandler.confirmBluetoothPermissions(this)
        BluetoothHandler.toggleDiscovery()
    }

    private fun setupReceivers() {

        receiverClientThreadStatusChanged = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent!!.action

                if(action.equals(ClientThread.ACTION_STATUS_CHANGED)) {

                    val extraStatus = intent.getIntExtra(ClientThread.EXTRA_STATUS, 0)

                    when(extraStatus) {

                        ClientThread.STATUS_CONNECTED -> {

                            serviceController!!.startBluetoothStreamService()
                            messageReader = MessageReader()
                            messageReader!!.constructMessageHandler(baseContext)
                            messageReader!!.start()

                            scrollToScreen(MainScreen.TIMER)

                        }
                    }
                }
            }
        }

        receiverBluetoothFoundDevice = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent!!.action

                if(action.equals(BluetoothDevice.ACTION_FOUND)) {

                    val foundDevice: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    if(foundDevice!!.address == "98:D3:51:FD:7A:96"){

                        bluetoothConnection.startClient(
                            foundDevice,
                            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                        )
                    }
                }
            }
        }

        var intentFilterClientThreadStatusChange = IntentFilter(ClientThread.ACTION_STATUS_CHANGED)
        LocalBroadcastManager.getInstance(this.baseContext).registerReceiver(
            receiverClientThreadStatusChanged!!,
            intentFilterClientThreadStatusChange)


        var intentFilterFoundDevice = IntentFilter(BluetoothDevice.ACTION_FOUND)
        baseContext.registerReceiver(receiverBluetoothFoundDevice, intentFilterFoundDevice)

    }
}