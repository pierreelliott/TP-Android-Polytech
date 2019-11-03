package com.example.tp2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.InverseMethod
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tp2.database.MyDatabase
import com.example.tp2.databinding.FragmentPersonalDataBinding
import com.example.tp2.model.User
import com.example.tp2.viewmodel.IdentityViewModel
import com.example.tp2.viewmodel.PersonalDataViewModel
import com.example.tp2.viewmodelfactory.IdentityViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

object LongConverter {
    @JvmStatic
    @InverseMethod("stringToDate")
    fun dateToString(
        value: Long
    ): String {
        val date = Date(value)
        val f = SimpleDateFormat("dd/MM/yy")
        val dateText = f.format(date)
        return dateText
    }

    @JvmStatic
    fun stringToDate(        value: String
    ): Long {
        val f = SimpleDateFormat("dd/MM/yy")
        val d = f.parse(value)
        return d.time
    }
}

interface PersonalDateEventListener {
    fun onGender(gender: String)
}

class PersonalDataFragment : Fragment() {

    private lateinit var binding: FragmentPersonalDataBinding
    private lateinit var viewModel: IdentityViewModel // Variable du ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = MyDatabase.getInstance(application).userDao
        val viewModelFactory = IdentityViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(IdentityViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_data, container, false)
        binding.lifecycleOwner = this

        binding.viewmodel = viewModel // Modification de l'initialisation du binding

        binding.apply {
            evBirthday.hint = getString(R.string.birthdayDate)
            btValidate.text = getString(R.string.validate)
        }

        viewModel.navigateToOtherActivity.observe(this, Observer { user ->
            user?.let {
                val message = viewModel.user.value?.gender + " " + LongConverter.dateToString(viewModel.user.value?.birthdayDate?:0)
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()

                viewModel.doneValidateNavigating()

                this.findNavController().navigate(
                    R.id.action_personalDataFragment_to_listFragment
//                    IdentityFragmentDirections
//                        .actionPersonalDataFragmentToListFragment()
                )
            }
        })

        return binding.root
    }

//    private fun validate(view: View) {
//        val message = viewModel.user.value?.gender + " " +
//                LongConverter.dateToString(viewModel.user.value?.birthdayDate?:0)
//
//        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
//    }

    private fun validate(view: View) {
        viewModel.onValidate()
        view.findNavController().navigate(IdentityFragmentDirections.actionIdentityFragmentToPersonalDataFragment(viewModel.user.value?: User()))
    }
}