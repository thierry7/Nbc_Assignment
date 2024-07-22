package com.example.nbc_assignment.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nbc_assignment.model.Page
import com.example.nbc_assignment.repository.DataRepository
import com.example.nbc_assignment.repository.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private lateinit var repository: DataRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `fetchHomepageData sets Loading state`() = testScope.runBlockingTest {
        `when`(repository.getHomepageData()).thenReturn(DataResult.Loading)

        viewModel.getData()

        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.homepageData.value
        Assert.assertTrue(result is DataResult.Loading)
    }

    @Test
    fun `fetchHomepageData returns success`() = testScope.runBlockingTest {
        val mockPage = mock(Page::class.java)
        `when`(repository.getHomepageData()).thenReturn(DataResult.Success(mockPage))

        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.homepageData.value
        Assert.assertTrue(result is DataResult.Success)
        Assert.assertEquals(mockPage, (result as DataResult.Success).data)
    }

    @Test
    fun `fetchHomepageData returns error`() = testScope.runBlockingTest {
        val mockException = RuntimeException("Test exception")
        `when`(repository.getHomepageData()).thenReturn(DataResult.Error(mockException))

        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.homepageData.value
        Assert.assertTrue(result is DataResult.Error)
        Assert.assertEquals(mockException, (result as DataResult.Error).exception)
    }
}
