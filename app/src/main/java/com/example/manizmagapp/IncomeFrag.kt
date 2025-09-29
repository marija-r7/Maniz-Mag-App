package com.example.manizmagapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IncomeFrag : Fragment() {
    private var korisnik: Korisnik? = null
    private lateinit var recyclerViewIncome: RecyclerView
    private lateinit var incomeItems: ArrayList<IncomeItem>
    private lateinit var incomeItemAdapter: IncomeItemAdapter
    private lateinit var addIncomeItemBtn: FloatingActionButton
    private lateinit var authorization: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    companion object{
        private const val ARG_KORISNIK = "arg_korisnik"

        fun newInstance(korisnik: Korisnik?): IncomeFrag {
            val fragment = IncomeFrag()
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.income_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewIncome = view.findViewById(R.id.recyclerViewIncome)
        recyclerViewIncome.setHasFixedSize(true)
        recyclerViewIncome.layoutManager = LinearLayoutManager(context)

        incomeItems = ArrayList()
        incomeItemAdapter = IncomeItemAdapter(incomeItems)
        recyclerViewIncome.adapter = incomeItemAdapter

        addIncomeItemBtn = view.findViewById(R.id.add_income_btn)

        GetIncomeDataFromDB()

        addIncomeItemBtn.setOnClickListener{
            ReplaceFragment()
        }
    }

    private fun GetIncomeDataFromDB() {
        authorization = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Income")
            .child(authorization.currentUser!!.uid)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                incomeItems.clear()
                for(itemSnapshot in snapshot.children){
                    val item = itemSnapshot.key?.let {
                        IncomeItem(
                            itemSnapshot.child("incomeItem/source").value.toString(),
                            itemSnapshot.child("incomeItem/amount").value.toString().toDoubleOrNull()
                        )
                    }
                    if(item!=null)
                        incomeItems.add(item)
                    incomeItemAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun ReplaceFragment()
    {
        val incomeFragAddItem = IncomeFragAddItem.newInstance(korisnik)
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, incomeFragAddItem)
            .addToBackStack(null)
        fragmentTransaction.commit()
    }
}