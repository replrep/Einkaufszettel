package de.cbrunzema.einkaufszettel

import android.app.Application
import android.content.res.Resources
import android.util.Log
import kotlinx.serialization.json.Json
import java.io.BufferedReader

class Einkaufszettel : Application() {
    companion object {
        lateinit var res: Resources
    }

    override fun onCreate() {
        super.onCreate()
        res = resources

        Log.e("AAA", "before save")
        saveEinkaufszettel(itemStoreDefault)
        Log.e("AAA", "after save")

        Log.e("AAA", "before load")
        val data = loadEinkaufszettel()
        Log.e("AAA", "after load")
        Log.e("AAA", "" + data)
    }

    fun loadEinkaufszettel(): Set<ShoppingItem> {
        val json =
            applicationContext.openFileInput(storageFileName).bufferedReader(Charsets.UTF_8).use(
                BufferedReader::readText
            )
        return Json.decodeFromString(json)
    }

    fun saveEinkaufszettel(items: Collection<ShoppingItem>) {
        applicationContext.openFileOutput(storageFileName, MODE_PRIVATE).use {
            it.write(Json.encodeToString(items).toByteArray(Charsets.UTF_8))
        }
    }
}
