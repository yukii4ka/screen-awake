package dev.yukii.screenawake

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat

class AwakeService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var timer: CountDownTimer? = null

    companion object {
        const val CHANNEL_ID = "screen_awake_channel"
        const val ACTION_STOP = "dev.yukii.screenawake.STOP"
        const val EXTRA_DURATION_MS = "duration_ms"
        const val NOTIF_ID = 1
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }

        val durationMs = intent?.getLongExtra(EXTRA_DURATION_MS, 30 * 60 * 1000L)
            ?: 30 * 60 * 1000L

        acquireWakeLock()
        startForeground(NOTIF_ID, buildNotification(durationMs))
        startTimer(durationMs)

        return START_NOT_STICKY
    }

    private fun acquireWakeLock() {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "ScreenAwake::WakeLock"
        ).apply { acquire(24 * 60 * 60 * 1000L) }
    }

    private fun startTimer(durationMs: Long) {
        timer = object : CountDownTimer(durationMs, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(NOTIF_ID, buildNotification(millisUntilFinished))
            }
            override fun onFinish() {
                stopSelf()
            }
        }.start()
    }

    private fun buildNotification(remainingMs: Long): Notification {
        val mins = remainingMs / 1000 / 60
        val secs = (remainingMs / 1000) % 60
        val timeStr = "%d:%02d".format(mins, secs)

        val stopIntent = PendingIntent.getService(
            this, 0,
            Intent(this, AwakeService::class.java).apply { action = ACTION_STOP },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Screen awake")
            .setContentText("Remaining: $timeStr")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        timer?.cancel()
        wakeLock?.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
