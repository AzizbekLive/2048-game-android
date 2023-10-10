package uz.gita.azizbek2048.app

import android.app.Application
import uz.gita.azizbek2048.pref.MyPref


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MyPref.init(this)

    }

}