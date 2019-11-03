package com.example.tp2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp2.database.UserDao
import com.example.tp2.model.User
import kotlinx.coroutines.*

class IdentityViewModel(
    private val database: UserDao,
    application: Application,
    private val userID: Long = 0L // userID
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        Log.i("IdentityViewModel", "created")
//        _user.value = User(0, "Doe", "John")
        initializeUser()
    }

    private fun initializeUser() {
        uiScope.launch {
            _user.value = getUserFromDatabase()
        }
    }

    private suspend fun getUserFromDatabase(): User? {
        return withContext(Dispatchers.IO) {

            var user = database.get(userID) // userID
            if (user == null) {
                user = User()
                user.id = insert(user)
            }
            user
        }
    }

    private suspend fun insert(user: User): Long {
        var id = 0L
        withContext(Dispatchers.IO) {
            id = database.insert(user)
        }
        return id
    }

//    fun onValidate() {
//        uiScope.launch {
//            val user = user.value ?: return@launch
//            update(user)
//        }
//    }

    private suspend fun update(user: User) {
        withContext(Dispatchers.IO) {
            database.update(user)
        }
    }

    private suspend fun get(id: Long) {
        withContext(Dispatchers.IO) {
            database.get(id)
        }
    }

    fun onGender(gender: String) {
        _user.value?.gender = gender
    }

    fun getFullName() = user.value?.firstname.plus(" ").plus(user.value?.lastname)

    override fun onCleared() {
        super.onCleared()
        Log.i("IdentityViewModel", "destroyed")
        viewModelJob.cancel()
    }

    // ================= Navigation ======================

    private val _navigateToPersonalDataFragment = MutableLiveData<User>()

    val navigateToPersonalDataFragment: LiveData<User>
        get() = _navigateToPersonalDataFragment

    fun doneNavigating() {
        _navigateToPersonalDataFragment.value = null
    }

    fun onValidateIdentity() {
        uiScope.launch {
            val user = user.value ?: return@launch

            if(user.firstname.isNullOrEmpty())
                return@launch

            if(user.lastname.isNullOrEmpty())
                return@launch

            update(user)

            _navigateToPersonalDataFragment.value = user
        }
    }

    private val _navigateToOtherActivity = MutableLiveData<User>()

    val navigateToOtherActivity: LiveData<User>
        get() = _navigateToOtherActivity

    fun doneValidateNavigating() {
        _navigateToOtherActivity.value = null
    }

    fun onValidate() {
        uiScope.launch {
            val user = user.value ?: return@launch

            if(user.gender.isNullOrEmpty())
                return@launch

            update(user)

            _navigateToOtherActivity.value = user
        }
    }
}