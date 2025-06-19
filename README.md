## Einkaufszettel

This is a minimalistic kotlin/jetpack/android app to note down stuff
for your next shopping trip ('Einkaufszettel' is German for
tiny-scrap-of-paper-used-for-a-list-of-things-to-buy). It can register
items in two levels, one for frequently needed things (like eggs or
milk), one for less frequently needed things (like socks). Registered
items can be put on the actual shopping list with a single tap. There
is also support for remembering one-time things that do not need to be
registered persistently.

### Installation

Download all the sources right here and launch it via Android Studio
on your attached device

or

Download all the sources, run gradle (`./gradlew :app:assembleDebug`) and
an installable apk will appear as `app/build/outputs/apk/debug/app-debug.apk`

or

Install the latest pre-built apk from the [Releases](https://github.com/replrep/Einkaufszettel/releases) section


### Appearance / Usage

The main screen looks something like this:

![main screen](images/screenshot-main.png)

If you long-tap an item the edit dialog shows up:

![edit dialog](images/screenshot-edit.png)

---
Copyright (C) 2025 Claus Brunzema <mail@cbrunzema.de>

Distributed under the terms of the GNU General Public License, see 
file COPYING
