package se.ju.lejo.persefone.Bluetooth

import android.app.Service
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import se.ju.lejo.persefone.Data.RestHandler
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BluetoothService constructor(): Service() {

    var backgroundThread: BackgroundThread? = null

    override fun onCreate() {
        println("SERVICE CREATED!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        backgroundThread = BackgroundThread(BluetoothClient.clientSocket as BluetoothSocket)
        backgroundThread!!.isRunning = true
        backgroundThread!!.start()

        println("SERVICE STARTED!!!!")

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return StreamBinder()
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundThread!!.cancel()

        var retry: Boolean = true

        while(retry) {

            try {
                backgroundThread!!.join()
            } catch (e: IOException) {}

            retry = false

        }
    }

    inner class StreamBinder constructor(): Binder() {



    }

    inner class BackgroundThread constructor(): Thread() {

        var socket: BluetoothSocket? = null
        var stream: DataInputStream? = null
        var isRunning: Boolean? = false
        var inputArray: ArrayList<String>? = null
        var restHandler: RestHandler? = null

        constructor(socket: BluetoothSocket): this() {
            inputArray = ArrayList<String>()
            restHandler = RestHandler()

            var tmpStream: InputStream? = null

            try {
                tmpStream = socket.inputStream
            } catch (e: IOException) {}

            stream = DataInputStream(tmpStream)
        }

        override fun run() {

            var buffer: ByteArray = ByteArray(1024)
            var bytes: Int? = null

            println("STARTING MF INPUT THREAD")

            var msg: String? = String()

            while(isRunning!!) {
                try {
                    bytes = stream!!.read(buffer)
                    msg += String(buffer, 0, bytes)

                    if(msg!!.split(",").size >= 3){
                        messageTranslator(msg!!.split(","))
                        msg = ""
                    }

                } catch (e: IOException) {}

            }

        }

        fun cancel() {
            try {
                socket!!.close()
            } catch (e: IOException) {}

            isRunning = false
        }

        fun messageTranslator(message: List<String>) {

            var calendar: Calendar? = Calendar.getInstance()
            var dateFormatter: SimpleDateFormat? = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var timeString: String? = dateFormatter!!.format(calendar!!.time)

            when(message.get(2)) {

                "1" -> {
                    println("1 osv")
                    restHandler?.sendClockInPostRequest(message.get(1), timeString!!)
                }

                "0" -> {
                    println("0 osv")
                    restHandler?.requestUpdateSession(message.get(1), timeString!!)
                }

            }



        }

    }

}