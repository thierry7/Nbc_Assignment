package com.example.nbc_assignment.repository

import android.content.Context
import android.content.res.AssetManager
import com.example.nbc_assignment.model.Item
import com.example.nbc_assignment.utils.ItemDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.FileNotFoundException
import java.io.InputStream

@ExperimentalCoroutinesApi
class DataRepositoryTest {

    private lateinit var context: Context
    private lateinit var gson: Gson
    private lateinit var dataRepository: DataRepository

    @Before
    fun setUp() {
        context = Mockito.mock(Context::class.java)
        val assetManager = Mockito.mock(AssetManager::class.java)
        Mockito.`when`(context.assets).thenReturn(assetManager)

        // Load the mock JSON from test resources
        val inputStream: InputStream = this::class.java.classLoader?.getResourceAsStream("assets/homepage.json")!!
        Mockito.`when`(assetManager.open("homepage.json")).thenReturn(inputStream)

        gson = GsonBuilder()
            .registerTypeAdapter(Item::class.java, ItemDeserializer())
            .create()

        dataRepository = DataRepository(context, gson)
    }

    @Test
    fun `getHomepageData returns success when data is valid`() = runTest {
        val result = dataRepository.getHomepageData()

        println("Result: $result")

        assert(result is DataResult.Success)
        val page = (result as DataResult.Success).data
        println("Page: ${page.page}")
        println("Shelves: ${page.shelves.size}")

        assert(page.page == "HOMEPAGE") { "Expected HOMEPAGE but was ${page.page}" }
        assert(page.shelves.size == 3) { "Expected 3 shelves but was ${page.shelves.size}" }
    }

    @Test
    fun `getHomepageData returns error when file not found`() = runTest {
        Mockito.`when`(context.assets.open("homepage.json")).thenThrow(FileNotFoundException::class.java)

        val result = dataRepository.getHomepageData()

        assert(result is DataResult.Error)
    }

    @Test
    fun `getHomepageData returns error when JSON is invalid`() = runTest {
        val invalidJsonStream: InputStream = "invalid json".byteInputStream()
        Mockito.`when`(context.assets.open("homepage.json")).thenReturn(invalidJsonStream)

        val result = dataRepository.getHomepageData()

        assert(result is DataResult.Error)
    }

    @Test
    fun `getHomepageData returns error when JSON is empty`() = runTest {
        val emptyJsonStream: InputStream = "".byteInputStream()
        Mockito.`when`(context.assets.open("homepage.json")).thenReturn(emptyJsonStream)

        val result = dataRepository.getHomepageData()

        assert(result is DataResult.Error)
    }
}
