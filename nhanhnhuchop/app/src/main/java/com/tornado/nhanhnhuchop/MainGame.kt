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
import kotlinx.coroutines.experimental.delay
import java.lang.Float.intBitsToFloat


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
    var numberquestion: Int =15
    var currentquestion: Int =0
    var maxquesion: Int =10
    var Score: Int =0


    var txtquestion:TextView? = null;
    var container:FrameLayout? = null;
    var time:TextView? = null;
    var avatar:ImageView? = null;
    var loginDialog: AlertDialog? = null;

    private var isPaused = false
    private var resumeFromMillis:Long = 0


    private var btnAnswer1: Button? = null
    private var btnAnswer2: Button? = null
    private var btnAnswer3: Button? = null
    private var btnAnswer4: Button? = null
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

    private fun timer(millisInFuture:Long,countDownInterval:Long):CountDownTimer{
        return object: CountDownTimer(millisInFuture,countDownInterval){
            override fun onTick(millisUntilFinished: Long) {

                val timeRemaining = "Time : " + millisUntilFinished / 1000

                if (isPaused){

                    resumeFromMillis = millisUntilFinished
                    cancel()
                }else{
                    time?.setText(timeRemaining)
                }

                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                time?.setText("0")
                GameOver()
            }

        }

    }

    fun InitGame()
    {
        numberquestion = 15
        currentquestion= 0
        maxquesion = 10
        Score = 0
        avatar?.setBackgroundResource(R.drawable.one)

        /*object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                time?.setText("" + millisUntilFinished / 1000)
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                time?.setText("0")
            }

        }.start()*/
        timer(60000,1000).start();

    }

    fun FadeIn()
    {
        var animation = AnimationUtils.loadAnimation(this@MainGame, R.anim.fade_in)
        txtquestion?.startAnimation(animation)
        btnAnswer1?.startAnimation(animation)
        btnAnswer2?.startAnimation(animation)
        btnAnswer3?.startAnimation(animation)
        btnAnswer4?.startAnimation(animation)
    }

    fun FadeOut()
    {
        val animation = AnimationUtils.loadAnimation(this@MainGame, R.anim.fade_out)
        txtquestion?.startAnimation(animation)
        btnAnswer1?.startAnimation(animation)
        btnAnswer2?.startAnimation(animation)
        btnAnswer3?.startAnimation(animation)
        btnAnswer4?.startAnimation(animation)
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

        val adView = findViewById(R.id.adview) as AdView
        InitGame()
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

        btnAnswer1 = findViewById(R.id.answer1) as Button
        btnAnswer2 = findViewById(R.id.answer2) as Button
        btnAnswer3 = findViewById(R.id.answer3) as Button
        btnAnswer4 = findViewById(R.id.answer4) as Button

        btnAnswer1!!.setOnClickListener {
            CheckAnswer( 0)
        }
        btnAnswer2!!.setOnClickListener {
            CheckAnswer( 1)
        }
        btnAnswer3!!.setOnClickListener {
            CheckAnswer( 2)
        }
        btnAnswer3!!.setOnClickListener {
            CheckAnswer( 3)
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
        var from:Int =435.px - (45.px*score)
        var to:Int = 475.px

        val ty1 = ObjectAnimator.ofFloat(avatar, View.TRANSLATION_Y, from.toFloat(), to.toFloat())
        ty1.setDuration(1000)
        ty1.interpolator = BounceInterpolator()
        ty1.start()
    }


    fun CheckAnswer( ans:Int){
        var result: Boolean? = false
        FadeOut()
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
            if(Score ==maxquesion)
            {
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
        Handler().postDelayed({
            if(currentquestion < maxquesion )
            {
                currentquestion++
                nextquestion()
            }
            else
            {
                GameOver()
            }
            FadeIn()
        }, 500)


    }

    fun GameOver()
    {
        val intent = Intent(this, GameOverActivity::class.java)
        startActivity(intent);
        finish()
    }

    fun readJSONFromAsset(): String? {
        var json: String? = null
        try {

            var i = (1..3).random()
            val sharedPreferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

            var id:String = sharedPreferences.getString(PREFS_ID, "")
            if(id.compareTo("")!=0)
            {
                val separate1 = id.split(",".toRegex())
                var check = false
                while (!check) {
                    for (temp in separate1) {
                        if (temp.compareTo(i.toString()) == 0) {
                            i = (1..3).random();
                            check = false;
                            break;
                        }
                        check = true;
                    }
                    if(check == true) {
                        id = id + ",$i";
                        if (separate1.size == 2)
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
            val  inputStream: InputStream = assets.open(url)
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun nextquestion(){
        txtquestion?.text = listquestion.get(currentquestion).question
        var array = listquestion.get(currentquestion).answer
        btnAnswer1?.text = array.get(0);
        btnAnswer2?.text = array.get(1);
        btnAnswer3?.text = array.get(2);
        btnAnswer4?.text = array.get(3);
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
