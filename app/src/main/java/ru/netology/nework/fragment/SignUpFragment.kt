package ru.netology.nework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentSignUpBinding
import ru.netology.nework.viewmodel.SignUpViewModel

@AndroidEntryPoint
class SignUpFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSignUpBinding.inflate(
            inflater,
            container,
            false
        )

        val signUpViewModel: SignUpViewModel by viewModels()

        with(binding) {
            signUpButton.setOnClickListener {
                val login = loginInput.text?.trim().toString()
                val name = nameInput.text?.trim().toString()
                val password = passwordInput.text?.trim().toString()
                signUpViewModel.signeUp(name, login, password)
                findNavController().navigate(R.id.tabsFragment)
            }
        }

        return binding.root
    }

}