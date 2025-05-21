// This is Einkaufszettel, an app for remembering your shopping list
//
// Copyright (C) 2025 Claus Brunzema <mail@cbrunzema.de>
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

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
