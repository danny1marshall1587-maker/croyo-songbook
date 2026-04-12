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
                    val adapter = BluetoothAdapter.getDefaultAdapter() ?: return
                    val scanner = adapter.bluetoothLeScanner ?: return
                    
                    // Android 12+ requires BLUETOOTH_SCAN permission.
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        if (androidx.core.content.ContextCompat.checkSelfPermission(
                                this@BluetoothShieldService,
                                android.Manifest.permission.BLUETOOTH_SCAN
                            ) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            Log.w("CryoShield", "Skipping heartbeat: BLUETOOTH_SCAN permission not granted.")
                            return
                        }
                    }

                    val callback = object : ScanCallback() {
                        override fun onScanResult(callbackType: Int, result: ScanResult?) {
                            // Heartbeat signal received or radio pulsed
                        }
                    }
                    
                    scanner.startScan(callback)
                    
                    // Stop scan after 2 seconds
                    android.os.Handler(mainLooper).postDelayed({
                        try {
                            scanner.stopScan(callback)
                            Log.d("CryoShield", "Heartbeat scan cycle complete")
                        } catch (se: SecurityException) {
                            Log.e("CryoShield", "Permission lost during stopScan")
                        } catch (e: Exception) {
                            // Scanner might have been closed
                        }
                    }, 2000)
                } catch (se: SecurityException) {
                    Log.e("CryoShield", "Heartbeat failed due to SecurityException", se)
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
