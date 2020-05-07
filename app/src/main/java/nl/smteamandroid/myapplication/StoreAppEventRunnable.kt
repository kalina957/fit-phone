package nl.smteamandroid.myapplication

import android.app.usage.UsageStats
import android.content.pm.PackageManager
import android.util.Log
import nl.smteamandroid.myapplication.database.AppDatabase
import nl.smteamandroid.myapplication.database.AppEvent
import java.lang.Exception

class StoreAppEventRunnable(val packageName : String, val usage : UsageStats, val db : AppDatabase, val packageManager : PackageManager) : Thread() {

    override fun run() {

        val appName : String? = getAppName(packageName)
        val getCategoryAsync = GetCategoryASync()
        val category : String? = getCategoryAsync.execute(packageName).get()

        db.appEventDao().insertRow(
            AppEvent(
                null,
                packageName,
                appName,
                category,
                usage.lastTimeUsed,
                usage.totalTimeInForeground
            )
        )
        Log.d("categories", category.toString())
    }

    private fun getAppName(packageName : String) : String?{
        var appName : String?
        try{
            appName = packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(
                    packageName,
                    PackageManager.GET_META_DATA
                )
            ) as String
        }
        catch (e : Exception){
            appName = null
        }

        return appName
    }
}