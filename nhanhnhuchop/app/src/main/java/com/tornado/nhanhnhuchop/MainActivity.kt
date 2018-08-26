package com.tornado.nhanhnhuchop

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import org.json.JSONObject
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private var btnStart: Button? = null
    private var btnHignscore: Button? = null

    private fun hideStatusBar(){
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar();
        setContentView(R.layout.activity_main)


        btnStart = this.findViewById(R.id.start) as Button;
        btnStart!!.setOnClickListener {
            val intent = Intent(this, MainGame::class.java)
            startActivity(intent);
        }

    }

}
