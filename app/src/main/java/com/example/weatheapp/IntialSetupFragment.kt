package com.example.weatheapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatheapp.databinding.FragmentIntialSetupBinding
import com.example.weatheapp.mainactivity.MainActivity

class IntialSetupFragment : Fragment() {

    lateinit var binding:FragmentIntialSetupBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.splashOkBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIntialSetupBinding.inflate(inflater,container,false)
        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IntialSetupFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}