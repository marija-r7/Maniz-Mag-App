package com.example.manizmagapp

import android.os.Bundle
import android.text.Editable
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class IncomeFragAddItem : Fragment() {
    private var korisnik: Korisnik? = null
    private lateinit var closeBtn: ImageButton
    private lateinit var izvorLayout: TextInputLayout
    private lateinit var izvorInput: AutoCompleteTextView
    private lateinit var iznosInput: TextInputEditText
    private lateinit var iznosLayout: TextInputLayout
    private lateinit var addItemBtn: Button
    private lateinit var authorization: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private var iznos: Double? = 0.0
    private lateinit var izvor: String

    companion object{
        private const val ARG_KORISNIK = "arg_korisnik"

        fun newInstance(korisnik: Korisnik?): IncomeFragAddItem {
            val fragment = IncomeFragAddItem()
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
        return inflater.inflate(R.layout.income_fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        izvorLayout = view.findViewById(R.id.izvor_dropdown_layout)
        izvorInput = view.findViewById(R.id.izvor_dropdown)
        PopulateDropDown()

        iznosLayout = view.findViewById(R.id.income_amount_input_layout)
        iznosInput = view.findViewById(R.id.income_amount_input)

        iznosInput.addTextChangedListener {
            ResetErrorIznos(it)
        }

        izvorInput.addTextChangedListener {
            ResetErrorIzvor(it)
        }

        addItemBtn = view.findViewById(R.id.add_item_btn)
        addItemBtn.setOnClickListener{
            SaveItemIncomeToDB()
        }
        closeBtn = view.findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener {
            CloseCurrentFragment()
        }
    }
    private fun SaveItemIncomeToDB(){
        iznos = iznosInput.text.toString().toDoubleOrNull()
        izvor = izvorInput.text.toString()
        if(iznos == null)
            iznosLayout.error = "Vrednost mora biti uneta u polje Iznos."
        else if(izvor == "Izvor")
            izvorLayout.error = "Morate odabrati neku od ponudjenih opcija."
        else
            SaveToDB()
    }
    private fun SaveToDB(){
        authorization = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Income")
            .child(authorization.currentUser?.uid.toString())

        val incomeItem = IncomeItem(izvor, iznos)
        val dataMap = hashMapOf(
            "incomeItem" to incomeItem,
            "username" to korisnik!!.korisnickoIme
        )

        databaseRef.push().setValue(dataMap).addOnCompleteListener {
            Toast.makeText(requireContext(), "Uspesno dodat izvor.", Toast.LENGTH_SHORT).show()
            CloseCurrentFragment()
        }.addOnFailureListener{ err ->
            Toast.makeText(requireContext(), "Addition of Spending Item Failed. Error ${err.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun CloseCurrentFragment(){
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
    }
    private fun PopulateDropDown() {
        val kategorije = resources.getStringArray(R.array.izvor_kategorije)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spending_fragment_add_dd_item, kategorije)
        izvorInput.setAdapter(arrayAdapter)
    }

    private fun ResetErrorIznos(editable: Editable?){
        if(editable!!.count()>0)
            iznosLayout.error = null
    }
    private fun ResetErrorIzvor(editable: Editable?){
        if(editable!!.count()>0)
            izvorLayout.error = null
    }
}