package com.tornado.nhanhnhuchop

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat
import android.text.Html
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class GameOverActivity : AppCompatActivity() {

    private var image: ImageView? = null
    private var score: TextView? = null
    private var time: TextView? = null
    private var total: TextView? = null
    private var thoat: Button? = null
    private var tiep: Button? = null

    private var highscore:String = ""
    private var intime:String = ""
    private var totalquestion:String = ""

    private fun hideStatusBar(){
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
        finish();
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar();
        setContentView(R.layout.activity_game_over)


        highscore = "Bạn đã trả lời được <font color='blue'>" +  intent.getIntExtra("score",0) + "</font> câu" //getResources().getString(R.string.score).format( intent.getIntExtra("score",0))
        intime = "Trong thời gian <font color='red'>" + (100 - intent.getIntExtra("time",0)) +"</font> giây"//getResources().getString(R.string.timer).format( intent.getIntExtra("time",0))
        totalquestion = "Tổng số câu hỏi : <font color='green'>" + intent.getIntExtra("total",0) + "</font>" //getResources().getString(R.string.total)

        image = this.findViewById(R.id.image) as ImageView
        score = this.findViewById(R.id.txtscore) as TextView
        time = this.findViewById(R.id.txttime) as TextView
        total = this.findViewById(R.id.txttotalscore) as TextView
        thoat = this.findViewById(R.id.thoat) as Button
        tiep = this.findViewById(R.id.tiep) as Button

        var i = (1..3).random()

        if(i==1)
        {
            image?.setBackgroundResource(R.drawable.win1)
        }
        else if (i ==2)
        {
            image?.setBackgroundResource(R.drawable.win2)
        }
        else if (i ==3)
        {
            image?.setBackgroundResource(R.drawable.win3)
        }
       /* else if (i ==4)
        {

        }
        else if (i ==5)
        {

        }
        else if (i ==6)
        {

        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            score?.setText(Html.fromHtml(highscore, Html.FROM_HTML_MODE_LEGACY))
            time?.setText(Html.fromHtml(intime, Html.FROM_HTML_MODE_LEGACY))
            total?.setText(Html.fromHtml(totalquestion, Html.FROM_HTML_MODE_LEGACY))
        }
        else
        {
            @Suppress("DEPRECATION")
            score?.setText(Html.fromHtml(highscore))
            @Suppress("DEPRECATION")
            time?.setText(Html.fromHtml(intime))
            @Suppress("DEPRECATION")
            total?.setText(Html.fromHtml(totalquestion))
        }


        thoat!!.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish();
        }

        tiep!!.setOnClickListener{
            val intent = Intent(this, MainGame::class.java)
            startActivity(intent);
            finish();
        }
    }
}
