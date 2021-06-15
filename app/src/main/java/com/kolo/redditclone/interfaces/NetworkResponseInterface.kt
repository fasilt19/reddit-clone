package com.kolo.redditclone.interfaces

import com.android.volley.NetworkError
import com.android.volley.VolleyError

interface NetworkResponseInterface {
    fun onResponse(response:String)
    fun onError(error: VolleyError)
}