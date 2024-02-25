package com.keithsmyth.androidshowcase.domain

object DomainFormatUtils {

    fun idFromApiResourceUrl(url: String): Int {
        return url.split("/").dropLastWhile { it.isEmpty() }.last().toInt()
    }

    fun capitaliseName(name: String): String {
        return name.replaceFirstChar {it.titlecase() }
    }
}
