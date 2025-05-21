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
import java.io.File

class Einkaufszettel : Application() {
    companion object {
        lateinit var res: Resources
        lateinit var dataFile: File
    }

    override fun onCreate() {
        super.onCreate()
        res = resources
        dataFile = File(applicationContext.dataDir, storageFileName)
    }
}
