package com.nishimura.cvshealthcodechallenge

import android.content.ContentResolver
import android.content.Context

import android.provider.SearchRecentSuggestions


class SearchRecentSuggestionsLimited(
    context: Context?,
    authority: String?,
    mode: Int,
    private val limit: Int = 5
) : SearchRecentSuggestions(context, authority, mode) {

    override fun truncateHistory(cr: ContentResolver, maxEntries: Int) {
        super.truncateHistory(cr, limit)
    }
}
