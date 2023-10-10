package uz.gita.azizbek2048

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import uz.gita.azizbek2048.controller.Controller
import uz.gita.azizbek2048.databinding.ActivityMainBinding
import uz.gita.azizbek2048.model.SideEnum
import uz.gita.azizbek2048.pref.MyPref
import uz.gita.azizbek2048.utils.MyBgGenerator
import uz.gita.azizbek2048.utils.MyTouchListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var controller: Controller? = null
    private val views = ArrayList<TextView>(16)
    private val generatorBg = MyBgGenerator()
    private var count = 0
    private lateinit var mediaPlayer: MediaPlayer
    private val myPref by lazy { MyPref.getSharedPref() }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        count = myPref?.getInt("count", 0)!!
        controller = Controller.getInstance(this@MainActivity)
//        mediaPlayer = MediaPlayer.create(this, R.raw.button_sound)

        //count = controller.getScore()
        binding.score.text = count.toString()


        val isNewGame = intent.getBooleanExtra("isNewGame", false)
        if (isNewGame) {

            if (!myPref?.getBoolean("isCheck", false)!!) {
                attachTouchListener()
                loadViews()
                describeMatrix()
            } else {
                val s = myPref!!.getString("numbers", "")?.split("/")
                loadViews()
                showScore()
                attachTouchListener()


                for (i in 0 until 16) {
                    views[i].apply {
                        if (!s?.get(i).equals("")) {
                            if (s!![i].equals("0")) {
                                text = ""
                            } else {
                                text = s!![i]
                                this.setBackgroundResource(MyBgGenerator().getBgByAmount(s[i].toInt()))
                            }
                        }
                    }
                }
                controller!!.getNumberMatrix()
            }

            binding.restart.setOnClickListener {
                onClick()
            }

        }

        binding.home.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }
    }


    private fun loadViews() {
        for (i in 0 until binding.mainView.childCount) {
            val liner = binding.mainView.getChildAt(i) as LinearLayoutCompat
            for (j in 0 until liner.childCount) {
                views.add(liner.getChildAt(j) as TextView)
            }
        }
    }


    private fun describeMatrix() {
        val matrix = controller?.getMatrix()
        for (i in 0 until matrix?.size!!) {
            for (j in 0 until matrix[i].size) {
                views[i * matrix.size + j].apply {
                    this.text = if (matrix[i][j] == 0) "" else matrix[i][j].toString()
                    this.setBackgroundResource(generatorBg.getBgByAmount(matrix[i][j]))
                }

                if (matrix[i][j] == 2048) {
                    showWinnerDialog()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun attachTouchListener() {

        val listener = MyTouchListener(this)
        binding.constraint.setOnTouchListener(listener)
        listener.setHandleSideEnumListener { side ->
            when (side) {
                SideEnum.RIGHT -> {
                    if (!controller?.isClickable()!!) {
                        showWinnerDialog()
                    }

                    controller?.moveToRight()
//                    mediaPlayer.start()
                    showScore()
                    showScore()
                    describeMatrix()
                }

                SideEnum.LEFT -> {
                    if (!controller?.isClickable()!!) {
                        showWinnerDialog()
                    }

                    controller?.moveToleft()
//                    mediaPlayer.start()
                    showScore()
                    describeMatrix()

                }

                SideEnum.UP -> {
                    if (!controller?.isClickable()!!) {
                        showWinnerDialog()
                    }

                    controller?.moveToUp()
//                    mediaPlayer.start()
                    showScore()
                    describeMatrix()
                }

                SideEnum.DOWN -> {
                    if (!controller?.isClickable()!!) {
                        showWinnerDialog()
                    }

                    controller?.moveToDown()
//                    mediaPlayer.start()
                    showScore()
                    describeMatrix()
                }
            }
        }
    }

    private fun onClick() {
        count = 0
        binding.score.text = count.toString()
        controller?.restart()
        describeMatrix()
        showScore()
    }


    private fun showScore() {
        binding.score.text = controller?.getScore().toString()
    }


    override fun onPause() {
        super.onPause()
        controller?.saveNumber()
    }

    private fun showWinnerDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.game_diolog, null, false)

        val builder = AlertDialog.Builder(this, R.style.CustomDialogStyle)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog = builder.create()

        count = controller?.getScore()!!
        dialogView.findViewById<TextView>(R.id.textview1).text = count.toString()

        dialogView.findViewById<ImageView>(R.id.diolog_home).setOnClickListener {
            controller?.restart()
            onClick()
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }

        dialogView.findViewById<ImageView>(R.id.diolog_restart).setOnClickListener {
            controller?.restart()
            onClick()
            alertDialog.dismiss()
        }

        dialogView.findViewById<ImageView>(R.id.diolog_share).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "I won the game! Download 2048 now.")
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        alertDialog.show()
    }


    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity2::class.java))
        super.onBackPressed()
    }
}