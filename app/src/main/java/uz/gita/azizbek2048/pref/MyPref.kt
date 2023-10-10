package uz.gita.azizbek2048.pref

import android.content.Context
import android.content.SharedPreferences

class MyPref {


    companion object {
        var sharedpreference: SharedPreferences? = null
        var mypref: MyPref? = null


        fun init(context: Context) {
            mypref = MyPref(context)
        }

        fun getSharedPref(): SharedPreferences? = sharedpreference

    }


    private constructor(context: Context) {
        sharedpreference = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    }
}