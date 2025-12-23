package com.example.lessons

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import com.example.lessons.utils.LocaleUtil

@HiltAndroidApp
class Lessons: Application() {
	override fun attachBaseContext(base: Context) {
		super.attachBaseContext(LocaleUtil.wrapContextWithSystemLocale(base))
	}
}