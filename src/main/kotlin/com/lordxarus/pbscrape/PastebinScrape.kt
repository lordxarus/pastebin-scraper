package com.lordxarus.pbscrape

import com.lordxarus.pbscrape.json.ScrapeEntry
import com.lordxarus.pbscrape.json.ScrapeList
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

val key = "606dcdfc4aad98e77bbadaadac6d4263"

fun main(args: Array<String>) {

    cacheRecentPastes()

}

fun cacheRecentPastes() {
    val client = OkHttpClient()
    val apiScrapeRequest = Request.Builder()
        .url("https://scrape.pastebin.com/api_scraping.php")
        .build()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val type = Types.newParameterizedType(List::class.java, ScrapeEntry::class.java)
    val adapter = moshi.adapter<List<ScrapeEntry>>(type)

    val reply = client.newCall(apiScrapeRequest).execute().body?.string()

    val cacheDir = File("/home/jeremy/.pbscrape/cache")

    val logger = Logger.getLogger("Pastebin")

    if (!cacheDir.exists()) {
        cacheDir.mkdirs()
    }

    val list = adapter.fromJson(reply)
    val iterator = list?.iterator()!!
    Thread.sleep(1000)
    Timer().scheduleAtFixedRate(object: TimerTask() {
        override fun run() {
            if (iterator.hasNext()) {
                val it = iterator.next()

                val file = File("/home/jeremy/.pbscrape/cache/${it.key}")

                if (!file.exists()) {
                    file.createNewFile()
                    logger.log(Level.INFO, "Downloading paste ${it.key} with title: ${it.title}")

                    val body = client.newCall(
                        Request.Builder()
                            .url(it.scrapeUrl)
                            .build()
                    ).execute().body

                    val fileOutputStream = FileOutputStream(file)
                    fileOutputStream.write(body?.bytes())
                    fileOutputStream.close()

                } else {
                    logger.log(Level.INFO, "Skipping paste ${it.key}")
                }
            } else {
                cacheRecentPastes()
            }

    } }, 10000, 2000)


}