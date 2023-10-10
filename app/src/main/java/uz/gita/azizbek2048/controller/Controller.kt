package uz.gita.azizbek2048.controller

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import uz.gita.azizbek2048.MainActivity
import uz.gita.azizbek2048.R
import uz.gita.azizbek2048.pref.MyPref
import java.lang.StringBuilder
import kotlin.random.Random


class Controller private constructor(private val context: Context) {

    fun getScore(): Int {
        return count
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: Controller
        var myPref = MyPref.getSharedPref()
        private var count = myPref?.getInt("count", 1)!!


        fun getInstance(context: Context): Controller {
            if (!Companion::instance.isInitialized)
                instance = Controller(context)
            return instance
        }
    }

    private val matrix = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )

    fun getMatrix(): Array<Array<Int>> = matrix


    init {
        addNewElement()
        addNewElement()
    }

    private fun addNewElement() {
        val list = ArrayList<Pair<Int, Int>>(16)
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (matrix[i][j] == 0) list.add(Pair(i, j))
            }
        }

        if (list.isEmpty() && !isClickable()) {
            restart()
        } else {
            val randomPos = Random.nextInt(0, list.size)
            matrix[list[randomPos].first][list[randomPos].second] = 2
        }
    }

    fun Array<Array<Int>>.copyData() = map { it.clone() }.toTypedArray()

    fun moveToleft() {
        val temp = matrix.copyData()
        for (i in 0 until 4) {
            val ls = ArrayList<Int>(4)
            var isAdded = false
            for (j in 0 until 4) {
                if (matrix[i][j] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[i][j])
                    continue
                }
                if (ls.last() == matrix[i][j]) {
                    if (isAdded) {
                        ls.add(matrix[i][j])
                        isAdded = false
                    } else {
                        ls[ls.size - 1] *= 2
                        isAdded = true
                    }
                    count += matrix[i][j]
                } else {
                    ls.add(matrix[i][j])
                    isAdded = false
                }
            }
            for (k in 0 until 4) {
                matrix[i][k] = if (k < ls.size) ls[k] else 0
            }
        }
        if (!temp.contentDeepEquals(matrix)) {
            addNewElement()
            startMediaPlayer()
        }
    }

    fun moveToRight() {
        val temp = matrix.copyData()

        for (i in 0 until 4) {
            val ls = ArrayList<Int>(4)
            var isAdded = false
            for (j in matrix[i].size - 1 downTo 0 step 1) {
                if (matrix[i][j] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[i][j])
                    continue
                }
                if (ls.last() == matrix[i][j]) {
                    if (isAdded) {
                        ls.add(matrix[i][j])
                        isAdded = false
                    } else {
                        ls[ls.size - 1] *= 2
                        isAdded = true
                    }
                    count += matrix[i][j]
                } else {
                    ls.add(matrix[i][j])
                    isAdded = false
                }
            }
            ls.reverse()
            while (ls.size < 4) {
                ls.add(0, 0)
            }
            for (k in 0 until 4) {
                matrix[i][k] = ls[k]
            }
        }
        if (!temp.contentDeepEquals(matrix)) {
            addNewElement()
            startMediaPlayer()
        }

    }

    fun moveToUp() {
        val temp = matrix.copyData()

        for (j in 0 until 4) {
            val ls = ArrayList<Int>(4)
            var isAdded = false
            for (i in 0 until 4) {
                if (matrix[i][j] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[i][j])
                    continue
                }
                if (ls.last() == matrix[i][j]) {
                    if (isAdded) {
                        ls.add(matrix[i][j])
                        isAdded = false
                    } else {
                        ls[ls.size - 1] *= 2
                        isAdded = true
                    }
                    count += matrix[i][j]
                } else {
                    ls.add(matrix[i][j])
                    isAdded = false
                }
            }
            for (k in 3 downTo 0) {
                matrix[k][j] = if (k < ls.size) ls[k] else 0
            }
        }
        if (!temp.contentDeepEquals(matrix)) {
            addNewElement()
            startMediaPlayer()
        }
    }

    fun moveToDown() {
        val temp = matrix.copyData()

        for (j in 0 until 4) {
            val ls = ArrayList<Int>(4)
            var isAdded = false
            for (i in 3 downTo 0) {
                if (matrix[i][j] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[i][j])
                    continue
                }
                if (ls.last() == matrix[i][j]) {
                    if (!isAdded) {
                        ls[ls.size - 1] *= 2
                        isAdded = true
                    } else {
                        ls.add(matrix[i][j])
                        isAdded = false
                    }
                    count += matrix[i][j]
                } else {
                    ls.add(matrix[i][j])
                    isAdded = false
                }
            }
            var index = 0
            for (i in 3 downTo 0) {
                matrix[i][j] = if (index < ls.size) ls[index++] else 0
            }
        }
        if (!temp.contentDeepEquals(matrix)) {
            addNewElement()
            startMediaPlayer()
        }

    }


    fun restart() {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                matrix[i][j] = 0
            }
        }
        count = 0
        addNewElement()
        addNewElement()
    }

    fun saveNumber() {
        var sb = StringBuilder()
        for (i in 0 until 16) {
            sb.append(matrix[i / 4][i % 4].toString()).append("/")
        }
        myPref?.edit()?.putInt("count", count)?.apply()
        myPref?.edit()?.putString("numbers", sb.toString())?.apply()
        myPref?.edit()?.putBoolean("isCheck", true)?.apply()
    }

    fun getNumberMatrix() {
        val s = myPref?.getString("numbers", "")?.split("/")

        for (i in 0 until 16) {
            matrix[i / 4][i % 4] = s!![i].toInt()
        }
    }

    fun isClickable(): Boolean {
        for (i in matrix.indices) {
            val emptyList = ArrayList<Int>(4)
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) {
                    return true
                }
                if (emptyList.isEmpty()) {
                    emptyList.add(matrix[i][j])
                    matrix[i][j]
                } else {
                    if (emptyList.last() == matrix[i][j]) {
                        return true
                    } else {
                        emptyList.add(matrix[i][j])
                    }
                }
            }
        }

        for (i in matrix[0].indices) {
            val emptyList = ArrayList<Int>(4)

            for (j in matrix.indices) {
                if (matrix[j][i] == 0) {
                    return true
                }
                if (emptyList.isEmpty()) {
                    emptyList.add(matrix[j][i])
                } else {
                    if (emptyList.last() == matrix[j][i])
                        return true
                    else
                        emptyList.add(matrix[j][i])
                }
            }
        }

        return false
    }
    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.button_sound)
    fun startMediaPlayer() {
        mediaPlayer.start()
    }
}