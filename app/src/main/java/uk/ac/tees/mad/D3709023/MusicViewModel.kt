package uk.ac.tees.mad.D3709023

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.D3709023.apiData.ApiInterface
import uk.ac.tees.mad.D3709023.apiData.MyData


class MusicViewModel : ViewModel() {
    private val apiInterface = Retrofit.Builder()
        .baseUrl("https://deezerdevs-deezer.p.rapidapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)

    private val _music = MutableLiveData<MyData>()
    val music: LiveData<MyData> = _music

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception?>()
    val error: LiveData<Exception?> = _error

    fun fetchMusic() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiInterface.getData("ed sheeran")
                if (response.isSuccessful) {
                    _music.value = response.body()
                    _error.value = null
                } else {
                    _error.value = Exception("Failed to fetch data")
                }
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }
}

@Composable
fun Box() {
    val musicViewModel: MusicViewModel = viewModel()
    val music by musicViewModel.music.observeAsState()
    val isLoading by musicViewModel.isLoading.observeAsState()
    val error by musicViewModel.error.observeAsState()

    LaunchedEffect(true) {
        musicViewModel.fetchMusic()
    }

    Box(
        music = music,
        isLoading = isLoading ?: false,
        error = error
    )
}