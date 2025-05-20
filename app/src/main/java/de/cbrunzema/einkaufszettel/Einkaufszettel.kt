package de.cbrunzema.einkaufszettel

import android.app.Application
import android.content.res.Resources

class Einkaufszettel : Application() {
    companion object {
        lateinit var res: Resources
    }

    override fun onCreate() {
        super.onCreate()
        res = resources
    }
}