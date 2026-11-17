package com.v2ray.ang

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.Configuration
import androidx.work.WorkManager
import com.v2ray.ang.AppConfig.ANG_PACKAGE
import com.v2ray.ang.handler.AngConfigManager
import com.v2ray.ang.handler.AppLocaleManager
import com.v2ray.ang.handler.MmkvManager
import com.v2ray.ang.handler.SettingsManager
import com.v2ray.ang.ui.compose.ThemeManager

class AngApplication : Application() {
    companion object {
        lateinit var application: AngApplication
    }

    /**
     * Attaches the base context to the application.
     * @param base The base context.
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.let(ContextCompat::getContextForLanguage))
        application = this
    }

    private val workManagerConfiguration: Configuration = Configuration.Builder()
        .setDefaultProcessName("${ANG_PACKAGE}:bg")
        .build()

    /**
     * Initializes the application.
     */
    override fun onCreate() {
        super.onCreate()

        MmkvManager.initialize(this)

        AppLocaleManager.initialize(this)

        // Initialize WorkManager with the custom configuration
        WorkManager.initialize(this, workManagerConfiguration)

        // Ensure critical preference defaults are present in MMKV early
        SettingsManager.initApp(this)

        // Initialize theme state from MMKV
        ThemeManager.refresh()

        // Add bundled servers from assets/servers.json when the server list is empty
        seedBundledServers()
    }

    private fun seedBundledServers() {
        // Only in the main process, not in the ":bg" WorkManager process
        val isMainProcess = if (Build.VERSION.SDK_INT >= 28) {
            getProcessName() == packageName
        } else {
            true
        }
        if (!isMainProcess) return

        Thread {
            try {
                                if (MmkvManager.decodeServerList("").isEmpty()) {
                    val json = assets.open("servers.json").bufferedReader().use { it.readText() }
                    AngConfigManager.importBatchConfig(json, "", false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
