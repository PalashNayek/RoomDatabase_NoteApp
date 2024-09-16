package com.palash.roomdatabase_noteapp

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utils {
    fun showToast(context: Context, msg:String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun getCurrentDateTime(format:String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(Date())
    }
}