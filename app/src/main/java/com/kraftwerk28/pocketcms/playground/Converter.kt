package com.kraftwerk28.pocketcms.playground

import androidx.databinding.InverseMethod

class Converter {
    companion object {
        @JvmStatic
        fun intToString(a: Int) = a.toString()

        @JvmStatic
        @InverseMethod("intToString")
        fun stringToInt(s: String) = s.toInt()
    }
}
