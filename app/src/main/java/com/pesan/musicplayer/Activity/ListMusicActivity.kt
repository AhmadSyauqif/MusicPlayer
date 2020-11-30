package com.pesan.musicplayer.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.pesan.musicplayer.Adapter.ListLaguAdapter
import com.pesan.musicplayer.Model.ModelListMusic
import com.pesan.musicplayer.Networking.Api
import com.pesan.musicplayer.R
import kotlinx.android.synthetic.main.activity_list_music.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

@Suppress("DEPRECATION")
class ListMusicActivity : AppCompatActivity(), ListLaguAdapter.onSelectData {

    var listLaguAdapter: ListLaguAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelListLagu: MutableList<ModelListMusic> = ArrayList()

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_music)

        //set Transparent Statusbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Mohon Tunggu")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Sedang menampilkan data...")

        rvListMusic!!.setHasFixedSize(true)
        rvListMusic!!.layoutManager = LinearLayoutManager(this)

        //get data Music
        listMusic
    }

    private val listMusic: Unit
        get() {
            progressDialog!!.show()
            AndroidNetworking.get(Api.ListMusic)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            progressDialog!!.dismiss()
                            val playerArray = response.getJSONArray("post")
                            for (i in 0 until playerArray.length()) {
                                if (i > 3) {
                                    val temp = playerArray.getJSONObject(i)
                                    val dataApi = ModelListMusic()
                                    dataApi.strId = temp.getString("id")
                                    dataApi.strCoverLagu = temp.getString("coverartikel")
                                    dataApi.strNamaBand = temp.getString("namaband")
                                    dataApi.strJudulMusic = temp.getString("judulmusic")
                                    modelListLagu.add(dataApi)
                                    showListMusic()
                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@ListMusicActivity,
                                "Gagal menampilkan data!", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        progressDialog!!.dismiss()
                        Toast.makeText(
                            this@ListMusicActivity,
                            "Tidak ada jaringan internet!", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

    private fun showListMusic() {
//        listLaguAdapter = ListLaguAdapter(this@ListLaguAdapter, modelListLagu, this) gatau knp
        rvListMusic!!.adapter = listLaguAdapter
    }

    //send data to activity Detail Lagu
    override fun onSelected(modelListLagu: ModelListMusic) {
        val intent = Intent(this@ListMusicActivity, MusicDetailActivity::class.java)
        intent.putExtra("detailLagu", modelListLagu)
        startActivity(intent)
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}