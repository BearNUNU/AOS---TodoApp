import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SharedPreferences) : ViewModel() {

    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    init {
        loadDarkMode()
    }

    fun loadDarkMode() {
        viewModelScope.launch {
            val isDarkModeEnabled = pref.getBoolean("dark_mode", false)
            _isDarkMode.postValue(isDarkModeEnabled)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        pref.edit().putBoolean("dark_mode", enabled).apply()
    }
}

class SettingViewModelFactory(private val pref: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
