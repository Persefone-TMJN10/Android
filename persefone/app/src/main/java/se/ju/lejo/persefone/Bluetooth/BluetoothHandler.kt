package se.ju.lejo.persefone.Bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import se.ju.lejo.persefone.MainActivity

class BluetoothHandler {

    private var activity: MainActivity? = null

    constructor(activity: MainActivity) {
        this.activity = activity
    }

    companion object {

        private val adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        fun toggleBluetooth() {

            if(adapter!!.isEnabled) {
                adapter.disable()
            }
            else {
                adapter.enable()
            }

        }

        fun toggleDiscovery() {

            if(adapter.isDiscovering) {
                adapter.cancelDiscovery()
            }
            else {
                adapter.startDiscovery()
            }

        }

        fun isEnabled(): Boolean {
            return adapter.isEnabled
        }

        fun isDiscovering(): Boolean {
            return adapter.isDiscovering
        }

        fun confirmBluetoothPermissions(activity: Activity) {

            var permissionCheck = activity!!.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
            permissionCheck += activity!!.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")


            if(permissionCheck != 0){

                activity!!.requestPermissions(
                    Array<String>(2) {
                        Manifest.permission.ACCESS_FINE_LOCATION;
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    1001
                )

            }

        }

    }

}