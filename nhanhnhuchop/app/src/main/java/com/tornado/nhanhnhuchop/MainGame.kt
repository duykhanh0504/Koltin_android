package com.tornado.nhanhnhuchop

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tornado.nhanhnhuchop.Model.item
import org.json.JSONObject
import java.io.InputStream
import java.io.StringReader

class MainGame : AppCompatActivity() {

    var listquestion: List<item> = ArrayList();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)
        var obj = JSONObject(readJSONFromAsset());
        listquestion = getListQuestionFromJson(obj.getString("result"));
    }

    fun getListQuestionFromJson(jsonStr: String): List<item> {

        val groupListType = object : TypeToken<ArrayList<item>>() {}.getType()
        val itemList = Gson().fromJson<List<item>>(jsonStr, groupListType)

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
}
