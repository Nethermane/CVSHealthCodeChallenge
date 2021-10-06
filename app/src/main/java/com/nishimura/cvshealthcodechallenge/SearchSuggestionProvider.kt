package com.nishimura.cvshealthcodechallenge

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.nishimura.cvshealthcodechallenge.SearchSuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}
