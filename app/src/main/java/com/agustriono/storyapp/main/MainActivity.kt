package com.agustriono.storyapp.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Display.Mode
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agustriono.storyapp.R
import com.agustriono.storyapp.databinding.ActivityMainBinding
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    private var modelMain: MutableList<ModelMain> = ArrayList()
    lateinit var mainAdapter: MainAdapter


    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val layoutManager = LinearLayoutManager(this)
        binding.rvListJudulCerita.setLayoutManager(layoutManager)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListJudulCerita.addItemDecoration(itemDecoration)

        //transparent background searchview
        val searchPlateId = searchData.getContext()
            .resources.getIdentifier("android:id/search_plate", null, null)

        val searchPlate = searchData.findViewById<View>(searchPlateId)
        searchPlate?.setBackgroundColor(Color.TRANSPARENT)
        searchData.setImeOptions(EditorInfo.IME_ACTION_DONE)
        searchData.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mainAdapter.filter.filter(newText)
                return true
            }
        })


        fabBackTop.setOnClickListener { view: View? ->
            rvListJudulCerita.smoothScrollToPosition(
                0
            )
        }

        //get data json
        getListCerita()
    }

    private fun getListCerita() {
        val client = AsyncHttpClient()
        val url = "ISI DENGAN ALAMAT API MASING_MASING"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {

                val result = String(responseBody!!)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObjectData = jsonArray.getJSONObject(i)
                        val dataApi = ModelMain()
                        dataApi.judul = jsonObjectData.getString("judul")
                        dataApi.image = jsonObjectData.getString("gambar")
                        dataApi.asal = jsonObjectData.getString("asal")
                        dataApi.sinopsis = jsonObjectData.getString("sinopsis")
                        dataApi.cerita = jsonObjectData.getString("cerita")
                        dataApi.sumber = jsonObjectData.getString("sumber")
                        modelMain.add(dataApi)
                    }
                    val mainAdapter = MainAdapter(this@MainActivity, modelMain)
                    binding.rvListJudulCerita.adapter = mainAdapter
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error!!.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return true
    }
}
