package com.lordxarus.pbscrape.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScrapeList (
    val scrapeEntryList: List<ScrapeEntry>
)
