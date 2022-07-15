package com.example.jyghiretest.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncAppDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val appDataRepository: AppDataRepository,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if (workerParameters.runAttemptCount > 3) {
            return Result.failure()
        }

        return appDataRepository.sync().fold(
            onSuccess = { Result.success() },
            onFailure = { Result.retry() }
        )
    }

}
