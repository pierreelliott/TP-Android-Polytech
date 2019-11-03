package com.example.tp2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp2.PersonalDateEventListener
import com.example.tp2.model.User

class PersonalDataViewModel(userParam: User) : ViewModel(), PersonalDateEventListener
{
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        Log.i("PersonalDataViewModel", "created")
        _user.value = userParam
    }
    override fun onGender(gender: String) {
        _user.value?.gender = gender
    }

    fun getFullName() = user.value?.firstname.plus(" ").plus(user.value?.lastname)
}