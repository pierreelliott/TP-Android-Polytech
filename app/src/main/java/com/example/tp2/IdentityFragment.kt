package com.example.tp2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.tp2.R
import com.example.tp2.database.MyDatabase
import com.example.tp2.databinding.FragmentIdentityBinding
import com.example.tp2.model.User
import com.example.tp2.viewmodel.IdentityViewModel
import com.example.tp2.viewmodelfactory.IdentityViewModelFactory

class IdentityFragment : Fragment() {
    private lateinit var binding: FragmentIdentityBinding
    private lateinit var viewModel: IdentityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = MyDatabase.getInstance(application).userDao
        val viewModelFactory = IdentityViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(IdentityViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_identity,
            container, false)

        binding.viewmodel = viewModel

        binding.apply {
//            tvTitle.text = getString(R.string.title)
            tiFirstname.hint = getString(R.string.firstname)
            tiLastname.hint = getString(R.string.lastname)
            btValidate.text = getString(R.string.validate)
        }

        // Code qui remplace la fonction onValidate()
        viewModel.navigateToPersonalDataFragment.observe(this, Observer { user ->
            user?.let {
                Log.d("TAG", "Ready to navigate")
                print("#########################")
                print("Ready to navigate")
                this.findNavController().navigate(
                    IdentityFragmentDirections
                        .actionIdentityFragmentToPersonalDataFragment(user)
                )

                viewModel.doneNavigating()
            }
        })

        return binding.root
    }
    private fun validate(view: View) {
        view.findNavController()
            .navigate(IdentityFragmentDirections.actionIdentityFragmentToPersonalDataFragment(viewModel.user.value?:User()))
    }
}