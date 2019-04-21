package com.example.vital.lab2

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var loadButton: Button
    lateinit var adapter: TitleAdapter
    lateinit var mDbWorkerThread: DbWorkerThread
    var mDb: MovieDataBase? = null
    val http = OkHttpClient()
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TitleAdapter(ArrayList(), this)
        recyclerView.adapter = adapter
        val fm = supportFragmentManager
        val movieFragment = MovieFragment()

        adapter.onItemClick = {
            item ->
            run {
                if (movieFragment.dialog != null) {
                    return@run
                }
                val args = Bundle()
                args.putString("title", adapter.items.find { it -> it.Title == item }?.Title)
                args.putString("year", adapter.items.find { it -> it.Title == item }?.Year)
                args.putString("type", adapter.items.find { it -> it.Title == item }?.Type)
                args.putString("imdbID", adapter.items.find { it -> it.Title == item }?.imdbID)
                movieFragment.arguments = args
                movieFragment.show(fm, item)
            }
        }

        loadButton = btnLoad
        loadButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            addTitles()
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.items.clear()
                page = 1
            }

        })

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = MovieDataBase.getInstance(this)
        if (!isConnectedToInternet()) {
            fetchDataFromDb()
        }
    }

    fun addTitles() {
        progressBar.visibility = View.VISIBLE
        val req = Request.Builder().url("https://www.omdbapi.com/?apikey=c8d2a36b&s=${editText.text}&page=$page").build()

        http.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("test", e?.message.toString())
            }

            override fun onResponse(call: Call?, response: Response?) {
                val res: MovieSearchResults? = Gson().fromJson(response?.body()?.charStream(), MovieSearchResults::class.java)

                res?.Search?.map {
                    mDb?.movieDao()?.insert(it)
                }

                this@MainActivity.runOnUiThread {
                    progressBar.visibility = View.INVISIBLE

                    res?.let {
                        it.Search?.let { it1 -> adapter.items.addAll(it1) }
                        adapter.notifyDataSetChanged()
                    }

                }
            }
        })

        page++
    }

    fun Context.isConnectedToInternet(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun fetchDataFromDb() {
        val task = Runnable {
            val weatherData =
                    mDb?.movieDao()?.getAll()
            if (weatherData != null && weatherData.isNotEmpty()) {
                adapter.items.addAll(weatherData)
            }
        }
        mDbWorkerThread.postTask(task)
    }
}
