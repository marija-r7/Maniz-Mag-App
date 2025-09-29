package com.example.manizmagapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SpendingFrag : Fragment() {
    private lateinit var spendingItems: ArrayList<SpendingItem>
    private lateinit var spendingItemAdapter: SpendingItemAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var addSpendingItemBtn: FloatingActionButton
    private lateinit var mainActivity: MainActivity
    private lateinit var authorization: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private var korisnik: Korisnik? = null

    companion object{
        private const val ARG_KORISNIK = "arg_korisnik"

        fun newInstance(korisnik: Korisnik?): SpendingFrag {
            val fragment = SpendingFrag()
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
        return inflater.inflate(R.layout.spending_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        spendingItems = ArrayList()
        spendingItemAdapter = SpendingItemAdapter(spendingItems)
        recyclerView.adapter = spendingItemAdapter

        addSpendingItemBtn = view.findViewById(R.id.add_spending_item_btn)

        GetSpendingDataFromDB()

        addSpendingItemBtn.setOnClickListener{
            ReplaceFragment()
        }
    }

    private fun GetSpendingDataFromDB() {
        authorization = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Spending")
            .child(authorization.currentUser!!.uid)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                spendingItems.clear()
                for(itemSnapshot in snapshot.children){
                    val item = itemSnapshot.key?.let {
                        SpendingItem(
                            itemSnapshot.child("spendingItem/heading").value.toString(),
                            itemSnapshot.child("spendingItem/amount").value.toString().toDoubleOrNull()
                        )
                    }
                    if(item!=null)
                        spendingItems.add(item)
                    spendingItemAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun ReplaceFragment()
    {
        val spendingFragAddItem = SpendingFragAddItem.newInstance(korisnik)
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, spendingFragAddItem)
            .addToBackStack(null)
        fragmentTransaction.commit()
    }
}