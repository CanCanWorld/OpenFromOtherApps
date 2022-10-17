package com.zrq.openfromotherapps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tencent.mmkv.MMKV
import com.zrq.openfromotherapps.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity(), OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        requestPermissions()
        MMKV.initialize(this)
        initData()
        initEvent()
    }

    private lateinit var mBinding: ActivityMainBinding
    private val dirList = ArrayList<String>()
    private lateinit var adapter: DirAdapter
    private val basePath = "/storage/emulated/0"
    private var nowPath = "/storage/emulated/0"

    private fun initData() {
        adapter = DirAdapter(this, dirList, this)
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        loadDirs()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initEvent() {
        mBinding.tvBack.setOnClickListener {
            if (nowPath != basePath) {
                nowPath = File(nowPath).parentFile?.absolutePath ?: basePath
                loadDirs()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
            return
        }
        return
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadDirs() {
        dirList.clear()
        val filePath = getExternalFilesDir(null)!!.absolutePath
        Log.d(TAG, "initData: $filePath")
        val file = File(nowPath)
        val listFiles = file.listFiles()
        if (listFiles != null)
            for (listFile in listFiles) {
                if (listFile.isDirectory) {
                    dirList.add(listFile.absolutePath)
                }
            }
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "MainActivity"

        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onResume() {
        super.onResume()
        mBinding.tvNowDir.text = MMKV.defaultMMKV().decodeString("path", basePath)
    }

    override fun onNextClick(view: View, position: Int) {
        Log.d(TAG, "onNextClick: ")
        nowPath = dirList[position]
        loadDirs()
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, "onItemClick: ")
        mBinding.tvNowDir.text = dirList[position]
        MMKV.defaultMMKV().encode("path", dirList[position])
    }
}