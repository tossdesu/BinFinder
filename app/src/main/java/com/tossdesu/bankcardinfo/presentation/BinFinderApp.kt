package com.tossdesu.bankcardinfo.presentation

import android.app.Application
import com.tossdesu.bankcardinfo.di.ApplicationComponent
import com.tossdesu.bankcardinfo.di.DaggerApplicationComponent

class BinFinderApp : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }
}