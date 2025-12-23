package com.example.lessons.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleUtil {
    fun wrapContextWithSystemLocale(base: Context): Context {
        val sysLocale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            base.resources.configuration.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            base.resources.configuration.locale
        }

        val config = Configuration(base.resources.configuration)
        config.setLocale(sysLocale)

        return base.createConfigurationContext(config)
    }
}
