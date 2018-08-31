package com.tornado.nhanhnhuchop

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.BounceInterpolator
import android.widget.Button
import com.tornado.nhanhnhuchop.utils.px
import org.json.JSONObject
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private var btnStart: Button? = null
    private var btnHignscore: Button? = null
    var loginDialog: AlertDialog? = null;

    private fun hideStatusBar(){
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private fun translatestart() {
        var from:Float = 300.px.toFloat()
        var to:Float = 0.px.toFloat()

        val ty1 = ObjectAnimator.ofFloat(btnStart, View.TRANSLATION_X, from, to)
        ty1.setDuration(1000)
        ty1.interpolator = BounceInterpolator()
        ty1.start()
    }
    private fun translatehighscore() {
        var from:Float = -300.px.toFloat()
        var to:Float = 0.px.toFloat()
        val ty1 = ObjectAnimator.ofFloat(btnHignscore, View.TRANSLATION_X, from, to)
        ty1.setDuration(1000)
        ty1.interpolator = BounceInterpolator()
        ty1.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar();
        setContentView(R.layout.activity_main)


        btnStart = this.findViewById(R.id.start) as Button
        btnHignscore = this.findViewById(R.id.highscore) as Button
        btnStart!!.setOnClickListener {
            val intent = Intent(this, MainGame::class.java)
            startActivity(intent);
            finish()
        }
        btnHignscore!!.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.dialog_about, null)
            builder.setView(dialogLayout)
            builder.setCancelable(true)
            loginDialog = builder.create()
            loginDialog?.show()
        }
        translatestart()
        translatehighscore()


    }

}
