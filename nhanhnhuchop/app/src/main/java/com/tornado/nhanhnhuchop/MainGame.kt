package com.tornado.nhanhnhuchop

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.*
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat
import android.support.v7.app.AppCompatActivity
import android.util.JsonReader
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tornado.nhanhnhuchop.Model.item
import org.json.JSONObject
import java.io.InputStream
import java.io.StringReader
import com.google.android.gms.ads.*
import java.util.*
import kotlin.ranges.CharRange.Companion.EMPTY
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.support.v7.app.AlertDialog
import android.view.*
import android.view.animation.BounceInterpolator
import com.tornado.nhanhnhuchop.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_game.*
import kotlinx.android.synthetic.main.pause_dialog.view.*
import kotlinx.coroutines.experimental.delay
import java.lang.Float.intBitsToFloat
import kotlin.concurrent.schedule


class MainGame : AppCompatActivity(), View.OnClickListener {


    override fun onClick(p0: View?) {

        val item_id = p0?.id
        when (item_id) {
            1 -> {

            }
            2 -> {

            }
            3 -> {

            }
            4 -> {

            }
        }
    }

    lateinit var mInterstitialAd: InterstitialAd

    var listquestion: MutableList<item> = ArrayList();
    var numberquestion: Int =20
    var currentquestion: Int =0
    var maxquesion: Int =10
    var Score: Int =0
    var HighScore: Int =0
    var circlescore:ImageView? = null


    var txtquestion:TextView? = null;
    var container:FrameLayout? = null;
    var time:TextView? = null;
    var avatar:ImageView? = null;
    var loginDialog: AlertDialog? = null;

    var pauseDialog: AlertDialog? = null;

    val strwin = arrayOf<String>("Hay lắm","quá chuẩn","tuyệt vời")
    val strlose = arrayOf<String>("Dễ vậy mà ","Sai rồi nha","chúc may mắn lần sau ")

    private var isPaused = false
    private var resumeFromMillis:Long = 0

    private var btnpause:ImageButton? =null

    private var btnAnswer1: FrameLayout? = null
    private var btnAnswer2: FrameLayout? = null
    private var btnAnswer3: FrameLayout? = null
    private var btnAnswer4: FrameLayout? = null

    private var txtAnswer1: TextView? = null
    private var txtAnswer2: TextView? = null
    private var txtAnswer3: TextView? = null
    private var txtAnswer4: TextView? = null

    private var txtwin: TextView? = null
    private var txtlose: TextView? = null
    private var txtans: TextView? = null
    private var imagewin: ImageView? = null
    private var imagelose: ImageView? = null
    private var wincontent:LinearLayout? = null
    private var losecontent:LinearLayout? = null
    var timeRemaining:Int = 100
   // private var bgImage:ImageView?=null
    private var bgImage: MutableList<ImageView?> = ArrayList();

    private fun hideStatusBar(){
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


        val timer1 = object: CountDownTimer(100000,1000){
            override fun onTick(millisUntilFinished: Long) {

                 timeRemaining =  (millisUntilFinished / 1000).toInt()

                if (isPaused){

                    //resumeFromMillis = millisUntilFinished
                    this.cancel()
                  //  GameOver()
                }else{
                    if(millisUntilFinished / 1000 < 16)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            time?.setTextColor(getColor(R.color.red))
                        };
                    }
                    time?.setText("Thời gian : " + timeRemaining)
                }

                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                time?.setText("0")
                this.cancel()
               GameOver()
            }

        }

    val timewinlose = object: CountDownTimer(1200,1000){
        override fun onTick(millisUntilFinished: Long) {


            //here you can have your logic to set text to edittext
        }

        override fun onFinish() {
            losecontent?.visibility = View.INVISIBLE
            wincontent?.visibility = View.INVISIBLE
            this.cancel()
        }

    }

    val timerforanswer = object: CountDownTimer(1000,1000){
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            FadeOut()

            Timer().schedule(100) {
            if (currentquestion < numberquestion) {
                currentquestion++
                nextquestion()
            } else {
                timer1.cancel()
                GameOver()
            }

                FadeIn()
            }
            cancel()
        }

    }


    fun InitGame()
    {
     //   timer1.cancel()
        wincontent?.visibility = View.INVISIBLE
        losecontent?.visibility = View.INVISIBLE
        numberquestion = 20
        currentquestion= 0
        maxquesion = 10
        Score = 0
        HighScore = 0
        circlescore?.visibility = View.GONE
        avatar?.setBackgroundResource(R.drawable.one)

        isPaused = false

        /*object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                time?.setText("" + millisUntilFinished / 1000)
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                time?.setText("0")
            }

        }.start()*/
        timer1.start()

    }

    fun FadeIn()
    {
        this@MainGame.runOnUiThread(java.lang.Runnable {
        var animation = AnimationUtils.loadAnimation(this@MainGame, R.anim.fade_in)
        txtquestion?.startAnimation(animation)
        txtAnswer1?.startAnimation(animation)
            txtAnswer2?.startAnimation(animation)
            txtAnswer3?.startAnimation(animation)
            txtAnswer4?.startAnimation(animation)
            if( loginDialog?.isShowing == true) {
                loginDialog?.dismiss()
            }
    })
    }

    fun FadeOut()
    {
        val animation = AnimationUtils.loadAnimation(this@MainGame, R.anim.fade_out)
        txtquestion?.startAnimation(animation)
        txtAnswer1?.startAnimation(animation)
        txtAnswer2?.startAnimation(animation)
        txtAnswer3?.startAnimation(animation)
        txtAnswer4?.startAnimation(animation)
    }


    fun MutableList<item>.randomElement()
            = if (this.isEmpty()) null else this[Random().nextInt(this.size)]

    fun <T, L : MutableList<T>> L.shuffle(): L {
        val rng = Random()

        for (index in 0..this.size - 1) {
            val randomIndex = rng.nextInt(this.size)

            // Swap with the random position
            val temp = this[index]
            this[index] = this[randomIndex]
            this[randomIndex] = temp
        }

        return this
    }

    fun <T> Iterable<T>.shuffle(): List<T> = this.toList().shuffle()

    override fun onDestroy() {
        super.onDestroy();
        timewinlose.cancel()
        timer1.cancel()
        timerforanswer.cancel()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        setContentView(R.layout.activity_main_game)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.dialog_custom, null)
        builder.setView(dialogLayout)
        builder.setCancelable(false)
        loginDialog = builder.create()
        loginDialog?.show()

        val adView = findViewById(R.id.adview) as AdView
        btnpause = findViewById(R.id.btnpause) as ImageButton
         txtquestion = findViewById(R.id.question) as TextView
         time = findViewById(R.id.time) as TextView
        avatar = findViewById(R.id.avatar) as ImageView
        avatar?.setY((475.px).toFloat());
        var score1 = findViewById(R.id.score1) as ImageView
        var score2 = findViewById(R.id.score2) as ImageView
        var score3 = findViewById(R.id.score3) as ImageView
        var score4 = findViewById(R.id.score4) as ImageView
        var score5 = findViewById(R.id.score5) as ImageView
        var score6 = findViewById(R.id.score6) as ImageView
        var score7 = findViewById(R.id.score7) as ImageView
        var score8 = findViewById(R.id.score8) as ImageView
        var score9 = findViewById(R.id.score9) as ImageView
        var score10 = findViewById(R.id.score10) as ImageView
        circlescore = findViewById(R.id.circlescore) as ImageView

        bgImage.add(score1)
        bgImage.add(score2)
        bgImage.add(score3)
        bgImage.add(score4)
        bgImage.add(score5)
        bgImage.add(score6)
        bgImage.add(score7)
        bgImage.add(score8)
        bgImage.add(score9)
        bgImage.add(score10)

        if(width == 480 || height == 800)
        {
            val layoutParams:FrameLayout.LayoutParams =  FrameLayout.LayoutParams(38.px, 38.px);
            score1.setLayoutParams(layoutParams)
            score2.setLayoutParams(layoutParams)
            score3.setLayoutParams(layoutParams)
            score4.setLayoutParams(layoutParams)
            score5.setLayoutParams(layoutParams)
            score6.setLayoutParams(layoutParams)
            score7.setLayoutParams(layoutParams)
            score8.setLayoutParams(layoutParams)
            score9.setLayoutParams(layoutParams)
            score10.setLayoutParams(layoutParams)

        }
        else if(width == 720 || height == 1280)
        {
            val layoutParams:FrameLayout.LayoutParams =  FrameLayout.LayoutParams(40.px, 40.px);
            score1.setLayoutParams(layoutParams)
            score2.setLayoutParams(layoutParams)
            score3.setLayoutParams(layoutParams)
            score4.setLayoutParams(layoutParams)
            score5.setLayoutParams(layoutParams)
            score6.setLayoutParams(layoutParams)
            score7.setLayoutParams(layoutParams)
            score8.setLayoutParams(layoutParams)
            score9.setLayoutParams(layoutParams)
            score10.setLayoutParams(layoutParams)

        }
         //container = findViewById(R.id.container) as FrameLayout

        btnAnswer1 = findViewById(R.id.answer1) as FrameLayout
        btnAnswer2 = findViewById(R.id.answer2) as FrameLayout
        btnAnswer3 = findViewById(R.id.answer3) as FrameLayout
        btnAnswer4 = findViewById(R.id.answer4) as FrameLayout

        txtAnswer1 = findViewById(R.id.txtanswer1) as TextView
        txtAnswer2 = findViewById(R.id.txtanswer2) as TextView
        txtAnswer3 = findViewById(R.id.txtanswer3) as TextView
        txtAnswer4 = findViewById(R.id.txtanswer4) as TextView

        txtwin = findViewById(R.id.txtwin) as TextView
        txtlose = findViewById(R.id.txtlose) as TextView
        txtans = findViewById(R.id.txtans) as TextView
        imagewin = findViewById(R.id.win) as ImageView
        imagelose = findViewById(R.id.lose) as ImageView

        wincontent = findViewById(R.id.contentwin) as LinearLayout
        losecontent = findViewById(R.id.contentlose) as LinearLayout

        InitGame()

        btnAnswer1!!.setOnClickListener {
            btnAnswer1?.setBackgroundResource(R.drawable.button_blue)
            CheckAnswer( 0)
        }
        btnAnswer2!!.setOnClickListener {
            btnAnswer2?.setBackgroundResource(R.drawable.button_blue)
            CheckAnswer( 1)
        }
        btnAnswer3!!.setOnClickListener {
            btnAnswer3?.setBackgroundResource(R.drawable.button_blue)
            CheckAnswer( 2)
        }
        btnAnswer4!!.setOnClickListener {
            btnAnswer4?.setBackgroundResource(R.drawable.button_blue)
            CheckAnswer( 3)
        }
8
        btnpause!!.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.pause_dialog, null)
            builder.setView(dialogLayout)
            builder.setCancelable(true)
            pauseDialog = builder.create()
            pauseDialog?.show()
            //login button click of custom layout
            dialogLayout.tiep.setOnClickListener {
                pauseDialog?.dismiss()
            }
            //cancel button click of custom layout
            dialogLayout.thoat.setOnClickListener {
                //dismiss dialog
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent);
                finish();

            }
        }

        val adRequest = AdRequest.Builder()
                .build()
        adView.loadAd(adRequest)

        AsyncTaskLoading().execute();


    }

    fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    fun getListQuestionFromJson(jsonStr: String): MutableList<item> {

        val groupListType = object : TypeToken<ArrayList<item>>() {}.getType()
        val itemList = Gson().fromJson<MutableList<item>>(jsonStr, groupListType)

        return itemList
    }

    private fun translateup(score:Int) {
        var from:Int =475.px - (45.px*score)
        var to:Int = 430.px - (45.px*score)

        val ty1 = ObjectAnimator.ofFloat(avatar, View.TRANSLATION_Y, from.toFloat(), to.toFloat())
        ty1.setDuration(1000)
        ty1.interpolator = BounceInterpolator()
        ty1.start()
    }

    private fun translatedown(score:Int) {
        if(Score >0) {
            circlescore?.visibility = View.VISIBLE
            circlescore?.setY((475.px).toFloat() - (45.px * HighScore) + 30.px);
            var from: Int = 435.px - (45.px * score)
            var to: Int = 475.px

            val ty1 = ObjectAnimator.ofFloat(avatar, View.TRANSLATION_Y, from.toFloat(), to.toFloat())
            ty1.setDuration(1000)
            ty1.interpolator = BounceInterpolator()
            ty1.start()
        }
    }

    fun Next(ans:Int)
    {
        if(ans == listquestion.get(currentquestion).rightanswer)
        {

            bgImage.get(Score)?.setBackgroundResource(R.drawable.upscore)
            val bgImage = bgImage.get(Score)?.background as AnimationDrawable
            bgImage.start()
            translateup(Score)
            if(Score > 3  && Score < 7  )
            {
                avatar?.setBackgroundResource(R.drawable.two)
            }
            else if(Score > 6 )
            {
                avatar?.setBackgroundResource(R.drawable.three)
            }
            Score++
            if(Score > HighScore)
            {
                HighScore = Score
                if(  circlescore?.visibility == View.VISIBLE)
                {
                    circlescore?.visibility = View.GONE
                }

            }
            if(Score ==maxquesion)
            {
                timer1.cancel()
                GameOver()
            }

        }
        else
        {
            translatedown(Score)
            while(Score >0) {

                bgImage?.get(Score)?.setBackgroundResource(R.drawable.downscore)
                val bgImage = bgImage?.get(Score)?.background as AnimationDrawable
                bgImage.start()
                Score--


            }

            avatar?.setBackgroundResource(R.drawable.one)

            bgImage?.get(Score)?.setBackgroundResource(R.drawable.downscore)
            val bgImage = bgImage?.get(Score)?.background as AnimationDrawable
            bgImage.start()
        }
    }

    fun CheckAnswer( ans:Int){

        btnAnswer1?.isEnabled = false
        btnAnswer2?.isEnabled = false
        btnAnswer3?.isEnabled = false
        btnAnswer4?.isEnabled = false

        if(ans != listquestion.get(currentquestion).rightanswer)
        {
            losecontent?.visibility = View.VISIBLE
            var i = (1..3).random()
            txtlose?.setText(strlose[i-1])
            if(i == 1)
            {
                imagelose?.setBackgroundResource(R.drawable.lose1)
            }
            else if(i ==2)
            {
                imagelose?.setBackgroundResource(R.drawable.lose2)
            }
            else if(i==3)
            {
                imagelose?.setBackgroundResource(R.drawable.lose3)
            }
            timewinlose.start()
            if(listquestion.get(currentquestion).rightanswer == 0)
            {
                btnAnswer1?.setBackgroundResource(R.drawable.button_redans)
                txtans?.setText("đáp án : A")
            }
            if(listquestion.get(currentquestion).rightanswer == 1)
            {
                btnAnswer2?.setBackgroundResource(R.drawable.button_redans)
                txtans?.setText("đáp án : B")
            }
            if(listquestion.get(currentquestion).rightanswer == 2)
            {
                btnAnswer3?.setBackgroundResource(R.drawable.button_redans)
                txtans?.setText("đáp án : C")
            }
            if(listquestion.get(currentquestion).rightanswer == 3)
            {
                btnAnswer4?.setBackgroundResource(R.drawable.button_redans)
                txtans?.setText("đáp án : D")
            }

        }
        else
        {


            wincontent?.visibility = View.VISIBLE

            var i = (1..3).random()


            txtwin?.setText(strwin[i-1])
            if(i == 1)
            {
                imagewin?.setBackgroundResource(R.drawable.win1)
            }
            else if(i ==2)
            {
                imagewin?.setBackgroundResource(R.drawable.win1)
            }
            else if(i==3)
            {
                imagewin?.setBackgroundResource(R.drawable.win1)
            }
            timewinlose.start()
        }
        Next(ans)
        timerforanswer.start()


    }

    override fun onBackPressed() {
            //super.onBackPressed()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.pause_dialog, null)
        builder.setView(dialogLayout)
        builder.setCancelable(true)
        pauseDialog = builder.create()
        pauseDialog?.show()
        //login button click of custom layout
        dialogLayout.tiep.setOnClickListener {
            pauseDialog?.dismiss()
        }
        //cancel button click of custom layout
        dialogLayout.thoat.setOnClickListener {
            //dismiss dialog
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish();

        }

    }

    fun GameOver()
    {
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("score",HighScore)
        if(timeRemaining < 2)
        {
            timeRemaining = 0
        }
        intent.putExtra("time", timeRemaining)
        intent.putExtra("total",numberquestion)
        startActivity(intent);
        finish()
    }

    fun readJSONFromAsset(): String? {
        var json: String? = null
        try {

            var i = (1..10).random()
            val sharedPreferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

            var id:String = sharedPreferences.getString(PREFS_ID, "")
            if(id.compareTo("")!=0)
            {
                val separate1 = id.split(",".toRegex())
                var check = false
                while (!check) {
                    for (temp in separate1) {
                        if (temp.compareTo(i.toString()) == 0) {
                            i = (1..10).random();
                            check = false;
                            break;
                        }
                        check = true;
                    }
                    if(check == true) {
                        id = id + ",$i";
                        if (separate1.size == 9)
                        {
                            id = "";
                        }
                    }
                }
            }
            else {
                id =  "$i"
            }
            val editor = sharedPreferences.edit()
            editor.putString(PREFS_ID, id)
            editor.apply()

            var url:String = "filename$i.json";
            //var url:String = "filename10.json";
            val  inputStream: InputStream = assets.open(url)
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun nextquestion(){
        this@MainGame.runOnUiThread(java.lang.Runnable {
            btnAnswer1?.isEnabled = true
            btnAnswer2?.isEnabled = true
            btnAnswer3?.isEnabled = true
            btnAnswer4?.isEnabled = true
            btnAnswer1?.setBackgroundResource(R.drawable.button_green)
            btnAnswer2?.setBackgroundResource(R.drawable.button_green)
            btnAnswer3?.setBackgroundResource(R.drawable.button_green)
            btnAnswer4?.setBackgroundResource(R.drawable.button_green)
            txtquestion?.text = listquestion.get(currentquestion).question
            var array = listquestion.get(currentquestion).answer
            txtAnswer1?.text = array.get(0);
            txtAnswer2?.text = array.get(1);
            txtAnswer3?.text = array.get(2);
            txtAnswer4?.text = array.get(3);
        })

       /* var i =0;
        for ( temp in array) {
            val button_dynamic = Button(this)
            button_dynamic.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            button_dynamic.text = temp
            val id =  currentquestion + i
            button_dynamic.id = i
            i++
            button_dynamic!!.setOnClickListener(this)
            container?.addView(button_dynamic)
        }*/

    }

    inner class AsyncTaskLoading : AsyncTask<String, String, MutableList<item>>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg p0: String?): MutableList<item> {

            var Result: MutableList<item> = ArrayList();
          //  var temp: MutableList<item> = ArrayList();
            try {
                var obj = JSONObject(readJSONFromAsset());
                Result = getListQuestionFromJson(obj.getString("result"));
                Result = Result.shuffle()
               /* for (a in 1..15) {
                    val number = temp.randomElement()
                    Result.add(number as item);
                    temp.remove(number as item)

                }*/

            } catch (e: Exception) {

            }

            return Result
        }

        override fun onPostExecute(result: MutableList<item>) {
            super.onPostExecute(result)
            listquestion = result
            nextquestion()
            loginDialog?.dismiss()

        }
    }
}
