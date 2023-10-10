package uz.gita.azizbek2048.utils

import uz.gita.azizbek2048.R

class MyBgGenerator {
    private val hashMap = hashMapOf(
        0 to R.drawable.bg_main,
        2 to R.drawable.bg_blue,
        4 to R.drawable.bg_ping,
        8 to R.drawable.bg_red,
        16 to R.drawable.bg_green,
        32 to R.drawable.bg_bluesifat,
        64 to R.drawable.bg_yellow,
        128 to R.drawable.bg_yellowish,
        256 to R.drawable.bg_stone,
        512 to R.drawable.bg_fio,
        1024 to R.drawable.bg_greenish,
        2048 to R.drawable.bg_egg,
        4096 to R.drawable.bg_egg,
    )

    fun getBgByAmount(amount: Int): Int = hashMap.getOrDefault(amount, R.drawable.bg_egg)
}