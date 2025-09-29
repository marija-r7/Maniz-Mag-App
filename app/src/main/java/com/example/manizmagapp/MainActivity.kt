package com.example.manizmagapp

import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.manizmagapp.databinding.MainActivityBinding

class MainActivity : AppCompatActivity()
{
    private var korisnik: Korisnik? = null
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        GetKorisnik()

        ReplaceFragment(HomepageFrag())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.pocetna -> ReplaceFragment(HomepageFrag())
                R.id.potrosnja -> {
                    val spendingFrag = SpendingFrag.newInstance(korisnik)
                    ReplaceFragment(spendingFrag)
                }
                R.id.prihodi -> {
                    val incomeFrag = IncomeFrag.newInstance(korisnik)
                    ReplaceFragment(incomeFrag)
                }
                R.id.mojnalog -> {
                    val myAccountFrag = MyAccountFrag.newInstance(korisnik)
                    ReplaceFragment(myAccountFrag)
                }

                else ->{}
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        return true
    }

    fun GetKorisnik()
    {
        korisnik = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra("korisnik", Korisnik::class.java)
        else
            intent.getParcelableExtra<Korisnik>("korisnik")
    }

    fun ReplaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun GetData(): Korisnik? {
        return korisnik
    }
}