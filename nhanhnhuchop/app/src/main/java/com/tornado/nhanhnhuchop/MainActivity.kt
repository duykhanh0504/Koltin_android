package com.tornado.nhanhnhuchop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.json.JSONObject
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private var btnStart: Button? = null
    private var btnHignscore: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = this.findViewById(R.id.start) as Button;
        btnStart!!.setOnClickListener {
            val intent = Intent(this, MainGame::class.java)
            startActivity(intent);
        }

    }

}
