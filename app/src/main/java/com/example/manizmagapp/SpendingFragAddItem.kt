package com.example.manizmagapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SpendingFragAddItem() : Fragment() {

    private lateinit var kategorijaInput: AutoCompleteTextView
    private lateinit var iznosInput: TextInputEditText
    private lateinit var iznosLayout: TextInputLayout
    private lateinit var kategorijaLayout: TextInputLayout
    private lateinit var kategorija: String
    private var iznos: Double? = null
    private lateinit var addItemBtn: Button
    private lateinit var closeBtn: ImageButton
    private lateinit var authorization: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private var korisnik: Korisnik? = null

    companion object{
        private const val ARG_KORISNIK = "arg_korisnik"

        fun newInstance(korisnik: Korisnik?): SpendingFragAddItem {
            val fragment = SpendingFragAddItem()
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
        return inflater.inflate(R.layout.spending_fragment_add, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        kategorijaLayout = view.findViewById(R.id.kategorija_dropdown_layout)
        kategorijaInput = view.findViewById(R.id.kategorija_dropdown)
        PopulateDropDown()

        iznosInput = view.findViewById(R.id.spending_amount_input)
        iznosLayout = view.findViewById(R.id.spending_amount_input_layout)

        iznosInput.addTextChangedListener {
            ResetErrorIznos(it)
        }

        kategorijaInput.addTextChangedListener {
            ResetErrorKategorija(it)
        }

        addItemBtn = view.findViewById(R.id.add_item_btn)
        addItemBtn.setOnClickListener{
            SaveSpendingItemToDB()
        }

        closeBtn = view.findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener {
            CloseCurrentFragment()
        }
    }

    private fun PopulateDropDown() {

        val kategorije = resources.getStringArray(R.array.spending_kategorije)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spending_fragment_add_dd_item, kategorije)
        kategorijaInput.setAdapter(arrayAdapter)
    }

    private fun SaveSpendingItemToDB(){
        iznos = iznosInput.text.toString().toDoubleOrNull()
        kategorija = kategorijaInput.text.toString()
        if(iznos == null)
            iznosLayout.error = "Vrednost mora biti uneta u polje Iznos."
        else if (kategorija == "Kategorija")
            kategorijaLayout.error = "Morate odabrati neku opciju."
        else
            SaveToDB()
    }

    private fun SaveToDB(){
        authorization = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Spending")
            .child(authorization.currentUser?.uid.toString())

        val spendingItem = SpendingItem(kategorija, iznos)
        val dataMap = hashMapOf(
            "spendingItem" to spendingItem,
            "username" to korisnik!!.korisnickoIme
        )

        databaseRef.push().setValue(dataMap).addOnCompleteListener {
            Toast.makeText(requireContext(), "Uspesno dodat trosak.", Toast.LENGTH_SHORT).show()
            CloseCurrentFragment()
        }.addOnFailureListener{ err ->
            Toast.makeText(requireContext(), "Addition of Spending Item Failed. Error ${err.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ResetErrorIznos(editable: Editable?){
        if(editable!!.count()>0)
            iznosLayout.error = null
    }

    private fun ResetErrorKategorija(editable: Editable?){
        if(editable!!.count()>0)
            kategorijaLayout.error = null
    }

    private fun CloseCurrentFragment(){
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
    }
}