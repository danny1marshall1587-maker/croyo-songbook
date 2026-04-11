package com.garethevans.church.opensongtablet.bluetooth

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import java.util.*

class BluetoothShieldService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var timer: Timer? = null
    private val CHANNEL_ID = "CryoShieldServiceChannel"
    private val NOTIFICATION_ID = 888

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Cryo-Shield Active")
            .setContentText("Maintaining pedal connection & CPU priority...")
            .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        acquireWakeLock()
        startHeartbeat()

        return START_STICKY
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CryoSongbook::BluetoothShieldLock")
        wakeLock?.acquire()
        Log.d("CryoShield", "WakeLock acquired")
    }

    private fun startHeartbeat() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                // Perform a very short BLE scan to keep the radio energized
                try {
                    val adapter = BluetoothAdapter.getDefaultAdapter()
                    val scanner = adapter.bluetoothLeScanner
                    scanner?.startScan(object : ScanCallback() {
                        override fun onScanResult(callbackType: Int, result: ScanResult?) {
                            // Heartbeat signal received or radio pulsed
                        }
                    })
                    
                    // Stop scan after 2 seconds
                    android.os.Handler(mainLooper).postDelayed({
                        try {
                            scanner?.stopScan(object : ScanCallback() {})
                            Log.d("CryoShield", "Heartbeat scan cycle complete")
                        } catch (e: Exception) {
                            // Scanner might have been closed
                        }
                    }, 2000)
                } catch (e: Exception) {
                    Log.e("CryoShield", "Heartbeat failed", e)
                }
            }
        }, 0, 60000) // Every 60 seconds
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock?.release()
        timer?.cancel()
        Log.d("CryoShield", "Shield service destroyed, locks released")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Cryo-Shield Background Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}
