package com.example.nbc_assignment.repository

import android.content.Context
import com.example.nbc_assignment.model.Page
import com.example.nbc_assignment.model.Shelf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val exception: Exception) : DataResult<Nothing>()
    data object Loading : DataResult<Nothing>()
}
class DataRepository @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {
    suspend fun getHomepageData(): DataResult<Page> {
        return withContext(Dispatchers.IO) {
            try {
                Timber.i("Attempting to read homepage data")

                val assetManager = context.assets
                val inputStream = assetManager.open("homepage.json")
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))

                val type = object : TypeToken<Page>() {}.type
                val page = gson.fromJson<Page>(bufferedReader, type)
                if (page == null || page.shelves.isEmpty()) {
                    Timber.i("Parsed Page object is null or has empty shelves")
                    DataResult.Success(null)
                }
                Timber.i("Successfully parsed homepage data: $page")

                DataResult.Success(removeDuplicateShelves(page))

            } catch (e: Exception) {
                Timber.tag("Error reading homepage data").e(e)
                DataResult.Error(e)
            }
        }
    }

    private fun removeDuplicateShelves(page: Page): Page {
        Timber.i("Removing duplicate shelves")

        val uniqueShelves = mutableMapOf<String, Shelf>()
        page.shelves.forEach { shelf ->
            if (shelf.title.isBlank()) {
                Timber.w("Found shelf with null or blank title: $shelf")
            } else {
                uniqueShelves[shelf.title] = shelf
            }
        }
        val sortedShelves = uniqueShelves.values.toList().sortedByDescending {
            it.title.contains("Trending", ignoreCase = true)
        }
        Timber.i("Sorted shelves: $sortedShelves")

        return page.copy(shelves = sortedShelves)
    }
}