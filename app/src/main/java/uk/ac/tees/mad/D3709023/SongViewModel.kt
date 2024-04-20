package uk.ac.tees.mad.D3709023

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.D3709023.apiData.Album
import uk.ac.tees.mad.D3709023.apiData.Data
import uk.ac.tees.mad.D3709023.apiData.MyData
import uk.ac.tees.mad.D3709023.apiData.RetrofitInstance

class SongViewModel:ViewModel()
{
    private val _song = MutableStateFlow(Data(Album(""),""))
    val song: StateFlow<Data> = _song.asStateFlow()

    fun fetchLatestSong(){
        viewModelScope.launch {
            val response= RetrofitInstance.api.getData(query = "ed sheeran")
            if (response.){
                response.body()?.let{
                    _song.value = it
                }
            }
        }
    }
}