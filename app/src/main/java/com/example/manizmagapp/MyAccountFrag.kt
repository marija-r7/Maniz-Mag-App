package com.example.manizmagapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText


class MyAccountFrag : Fragment() {
    private var korisnik: Korisnik? = null
    private lateinit var korisnickoIme: TextInputEditText
    private lateinit var imePrezime: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var logoutImage: ImageView

    companion object{
        private const val ARG_KORISNIK = "arg_korisnik"

        fun newInstance(korisnik: Korisnik?): MyAccountFrag {
            val fragment = MyAccountFrag()
            val args = Bundle()
            args.putParcelable(ARG_KORISNIK, korisnik)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            korisnik = it.getParcelable(ARG_KORISNIK)!!
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.my_account_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        InitializeValues(view)

        logoutImage.setOnClickListener{
            val intent: Intent = Intent(requireContext(), LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
    }
    private fun InitializeValues(view: View)
    {
        korisnickoIme = view.findViewById(R.id.RegUsernameEText)
        imePrezime = view.findViewById(R.id.RegFullNameEText)
        email = view.findViewById(R.id.RegEmailEText)
        korisnickoIme.setText(korisnik!!.korisnickoIme)
        imePrezime.setText(korisnik!!.imePrezime)
        email.setText(korisnik!!.email)
        logoutImage = view.findViewById(R.id.imageViewLogout)
    }

}