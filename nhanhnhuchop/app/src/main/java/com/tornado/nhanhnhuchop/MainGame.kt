package com.tornado.nhanhnhuchop

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tornado.nhanhnhuchop.Model.item
import org.json.JSONObject
import java.io.InputStream
import java.io.StringReader
import com.google.android.gms.ads.*
import java.util.*

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

    var txtquestion:TextView? = null;
    var container:FrameLayout? = null;

    private var btnAnswer1: Button? = null
    private var btnAnswer2: Button? = null
    private var btnAnswer3: Button? = null
    private var btnAnswer4: Button? = null


    fun MutableList<item>.randomElement()
            = if (this.isEmpty()) null else this[Random().nextInt(this.size)]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)
        val adView = findViewById(R.id.adview) as AdView

         txtquestion = findViewById(R.id.question) as TextView
         //container = findViewById(R.id.container) as FrameLayout

        btnAnswer1 = findViewById(R.id.answer1) as Button
        btnAnswer2 = findViewById(R.id.answer2) as Button
        btnAnswer3 = findViewById(R.id.answer3) as Button
        btnAnswer4 = findViewById(R.id.answer4) as Button

        btnAnswer1!!.setOnClickListener {

        }
        btnAnswer2!!.setOnClickListener {

        }
        btnAnswer3!!.setOnClickListener {

        }
        btnAnswer3!!.setOnClickListener {

        }

        val adRequest = AdRequest.Builder()
                .build()
        adView.loadAd(adRequest)

        AsyncTaskLoading().execute();

    }

    fun getListQuestionFromJson(jsonStr: String): MutableList<item> {

        val groupListType = object : TypeToken<ArrayList<item>>() {}.getType()
        val itemList = Gson().fromJson<MutableList<item>>(jsonStr, groupListType)

        return itemList
    }

    fun readJSONFromAsset(): String? {
        var json: String? = null
        try {
            val  inputStream: InputStream = assets.open("filename.json")
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
            var temp: MutableList<item> = ArrayList();
            try {
                var obj = JSONObject(readJSONFromAsset());
                temp = getListQuestionFromJson(obj.getString("result"));
                for (a in 1..15) {
                    val number = temp.randomElement()
                    Result.add(number as item);
                    temp.remove(number as item)

                }

            } catch (e: Exception) {

            }

            return Result
        }

        override fun onPostExecute(result: MutableList<item>) {
            super.onPostExecute(result)
            listquestion = result
            nextquestion()

        }
    }
}
