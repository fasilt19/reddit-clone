package com.kolo.redditclone.utils

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kolo.redditclone.interfaces.NetworkResponseInterface
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

object AppUtils {
    private val secondMillis = 1000
    private val minuteMillis = 60 * secondMillis
    private val hourMillis = 60 * minuteMillis
    private val daysMillis = 24 * hourMillis
    fun Context.newRequest(url:String,networkResponseInterface: NetworkResponseInterface){
        val stringRequest = StringRequest(Request.Method.GET,url, {
            networkResponseInterface.onResponse(it)
        }){
            networkResponseInterface.onError(it)
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    fun getTimeAgo(timeStamp: Long): CharSequence? {
        var time = timeStamp
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }


        val diff: Long = now - time
        return when {
            diff < minuteMillis -> {
                "just now"
            }
            diff < 2 * minuteMillis -> {
                "1 min"
            }
            diff < 50 * minuteMillis -> {
                " ${diff / minuteMillis} min"
            }
            diff < 90 * minuteMillis -> {
                "1 h"
            }
            diff < 24 * hourMillis -> {
                " ${diff / hourMillis} h"
            }
            diff < 48 * hourMillis -> {
                "yesterday"
            }
            else -> {
                " ${diff / daysMillis} d"
            }
        }
    }
    fun prettyCount(number: Number): String? {
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val numValue = number.toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(
                numValue / 10.0.pow((base * 3).toDouble())
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(numValue)
        }
    }
    fun hideKeyboard(context: Context, view: View) {
        try {
            val inputMethodManager =
                context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}