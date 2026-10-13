package com.kamikadze328.smssender

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.kamikadze328.smssender.domain.battery.ProcessBatteryStatusUseCase
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

class BatteryMonitorWorker(
    context: Context,
    params: WorkerParameters,
    private val processBatteryStatusUseCase: ProcessBatteryStatusUseCase,
) : CoroutineWorker(context, params) {
    companion object {
        private const val WORK_NAME = "battery_monitor"

        fun setup(applicationContext: Context) {
            val request = PeriodicWorkRequestBuilder<BatteryMonitorWorker>(12, TimeUnit.HOURS).build()

            WorkManager.getInstance(applicationContext)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    request,
                )
        }
    }

    override suspend fun doWork(): Result {
        Log.d("kek", "Battery monitoring started")
        return try {
            processBatteryStatusUseCase.onReceiveBatteryLevel(applicationContext)
            Result.success()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("kek", "Battery monitoring failed", e)
            Result.retry()
        }
    }
}