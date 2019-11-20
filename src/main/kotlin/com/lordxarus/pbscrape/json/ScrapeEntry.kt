package com.lordxarus.pbscrape.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScrapeEntry(
    @Json(name = "scrape_url") val scrapeUrl: String,
    @Json(name = "full_url") val fullUrl: String,
    val date: String,
    val key: String,
    val size: String,
    val title: String,
    val syntax: String,
    val user: String
)