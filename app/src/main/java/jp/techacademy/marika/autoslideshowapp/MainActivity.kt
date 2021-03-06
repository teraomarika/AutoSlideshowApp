package jp.techacademy.marika.autoslideshowapp

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {


    private val PERMISSIONS_REQUEST_CODE = 100

    private var mTimer: Timer? = null

    //タイマー用の変数
    private var mTimerSec = 0.0
    private var mHandler = Handler()

    var imageUriArray = ArrayList<Uri>()
    var cursor: Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        next.setOnClickListener { Log.d("button", "NEXT") }
        back.setOnClickListener {
            Log.d("button", "BACK")
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                    Log.d("android", "許可された")
                } else {
                    Log.d("android", "許可しなかた")
                }
        }
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )




        start.setOnClickListener {
            if (start.text == "START") {
                start.text = "STOP"
                next.isEnabled = false
                back.isEnabled = false
                mTimer = Timer()
                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        mTimerSec += 2.0
                        mHandler.post {

                            if (cursor!!.moveToNext()) {
                                val fieldIndex =
                                    cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor!!.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                                    )
                                imageview.setImageURI(imageUri)

                            }
                        }
                    }
                }, 2000, 2000) // 最初に始動させるまで 100ミリ秒、ループの間隔を 100ミリ秒 に設定
            } else if (start.text == "STOP") {
                start.text = "START"
                if (mTimer != null) {
                    mTimer!!.cancel()
                    mTimer = null
                    next.isEnabled = true
                    back.isEnabled = true

                }
            }
        }





        next.setOnClickListener {

            if (cursor!!.moveToNext()) {
                val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    )
                mHandler.post {
                    imageview.setImageURI(imageUri)
                }
            } else if (cursor!!.moveToFirst()) {
                val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    )
                mHandler.post {
                    imageview.setImageURI(imageUri)
                }
            }
        }


        back.setOnClickListener {

            if (cursor!!.moveToPrevious()) {
                val fieldIndex =
                    cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    )
                mHandler.post {
                    imageview.setImageURI(imageUri)
                }
            } else if (cursor!!.moveToLast()) {
                val fieldIndex =
                    cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    )
                mHandler.post {
                    imageview.setImageURI(imageUri)

                }


            }
        }

    }
}









