package com.zrq.openfromotherapps

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.tencent.mmkv.MMKV
import com.zrq.openfromotherapps.databinding.ActivityFileBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class FileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MMKV.initialize(this)
        initData()
    }

    private lateinit var binding: ActivityFileBinding
    private val basePath = "/storage/emulated/0"

    private fun initData() {
        binding.tvPath.text = MMKV.defaultMMKV().decodeString("path", basePath)
        Toast.makeText(this, uriToFile(intent.data), Toast.LENGTH_SHORT).show()
    }

    private fun uriToFile(uri: Uri?): String? {
        if (uri == null || uri.path == null) {
            return null
        }
        val pathUri = uri.path!!.lowercase(Locale.getDefault())
        val fileName = pathUri.substring(pathUri.lastIndexOf("/") + 1, pathUri.length - 2)
        Log.d(TAG, "uriToFile: $fileName")
        val filePath = MMKV.defaultMMKV().decodeString("path", basePath)

        val file = File(filePath, fileName)
        val parentFile = file.parentFile
        if (parentFile != null) {
            if (!parentFile.exists()) {
                parentFile.mkdir()
            }
        }
        if (file.exists()) {
            return "文件已存在"
        }
        val inputStream: InputStream?
        return try {
            file.createNewFile()
            inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file.absoluteFile)
            write(inputStream, outputStream)
            "文件已保存到本地"
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d(TAG, "uriToFile: $e")
            "错误异常：${e.message}"
        }
    }

    private fun write(inputStream: InputStream?, outputStream: FileOutputStream) {
        val buffer = ByteArray(1024 * 1024)
        try {
            while (true) {
                val len = inputStream!!.read(buffer)
                if (len < 0) break
                outputStream.write(buffer, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outputStream.flush()
            inputStream?.close()
            outputStream.close()
        }
    }

    companion object {
        const val TAG = "FileActivity"
    }

}