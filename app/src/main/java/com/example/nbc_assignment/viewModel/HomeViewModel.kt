package com.example.nbc_assignment.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbc_assignment.model.Page
import com.example.nbc_assignment.repository.DataRepository
import com.example.nbc_assignment.repository.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DataRepository
): ViewModel() {
    private val _homepageData = MutableStateFlow<DataResult<Page>?>(null)
    val homepageData: StateFlow<DataResult<Page>?> = _homepageData

    init {
        fetchHomepageData()
    }

    private fun fetchHomepageData() {
        viewModelScope.launch {
            try {
                val result = repository.getHomepageData()
                _homepageData.value = result
            } catch (e: Exception) {
                Timber.e(e, "Error fetching homepage data")
                _homepageData.value = DataResult.Error(e)
            }
        }
    }
}