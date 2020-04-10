package jp.techacademy.marika.autoslideshowapp

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE = 100
    private var mTimer: Timer?=null
//タイマー用の変数
    private var mTimerSec=0.0
    private var mHandler =Handler()
    private var mImageView=imageview






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // タイマーの作成
        mTimer = Timer()
        mTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mTimerSec += 0.1
//                mImageView.setImageURI()//？？
                mHandler.post {
//                    imageview.text = String.format("%.1f", mTimerSec) ここをimageViewにしたい
                }
            }
        }, 200, 200) // 最初に始動させるまで 200ミリ秒、ループの間隔を 200ミリ秒 に設定

        start.setOnClickListener {
            if (mTimer == null) {


                mTimer!!.cancel()
                mTimerSec = 0.0
                //imageview.setImageURI() = String.format("%.1f", mTimerSec)写真が止まる //
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
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

        if (cursor!!.moveToFirst()) {
            do {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageview.setImageURI(imageUri)

                Log.d("ANDROID", "URI : " + imageUri.toString())
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
}


//        next.setOnClickListener(this)
//
//        back.setOnClickListener(this)
//
//        start.setOnClickListener(this)
//
//    }
//
//    override fun onClick(){

        //クリック機能



