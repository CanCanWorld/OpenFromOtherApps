package com.zrq.openfromotherapps

import android.view.View

interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)

    fun onNextClick(view: View, position: Int)
}