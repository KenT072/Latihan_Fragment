package com.example.latihan_fragment

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private val historyTambah = arrayListOf<String>()
    private val historyKurang = arrayListOf<String>()
    private val historyKali = arrayListOf<String>()
    private val historyBagi = arrayListOf<String>()

    private var hasilNegatifTerakhir = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnKali = findViewById<Button>(R.id.btnKali)
        val btnBagi = findViewById<Button>(R.id.btnBagi)
        val btnTambah = findViewById<Button>(R.id.btnTambah)
        val btnKurang = findViewById<Button>(R.id.btnKurang)

        if (savedInstanceState != null) {
            historyTambah.addAll(
                savedInstanceState.getStringArrayList("tambah")
                    ?: arrayListOf()
            )

            historyKurang.addAll(
                savedInstanceState.getStringArrayList("kurang")
                    ?: arrayListOf()
            )

            historyKali.addAll(
                savedInstanceState.getStringArrayList("kali")
                    ?: arrayListOf()
            )

            historyBagi.addAll(
                savedInstanceState.getStringArrayList("bagi")
                    ?: arrayListOf()
            )

            hasilNegatifTerakhir =
                savedInstanceState.getBoolean("negatifTerakhir", false)

            ubahBackground(hasilNegatifTerakhir)
        }

        btnKali.setOnClickListener {
            hitung("*")
        }

        btnBagi.setOnClickListener {
            hitung("/")
        }

        btnTambah.setOnClickListener {
            hitung("+")
        }

        btnKurang.setOnClickListener {
            hitung("-")
        }

        if (savedInstanceState == null) {
            tampilkanFragment("+", false)
        }
    }

    private fun hitung(operasi: String) {

        val edtAngka1 = findViewById<EditText>(R.id.edtAngka1)
        val edtAngka2 = findViewById<EditText>(R.id.edtAngka2)

        val angka1 = edtAngka1.text.toString().toDoubleOrNull()
        val angka2 = edtAngka2.text.toString().toDoubleOrNull()

        if (angka1 == null || angka2 == null) {
            Toast.makeText(
                this,
                "Input kedua nomer terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (operasi == "/" && angka2 == 0.0) {
            Toast.makeText(
                this,
                "ga bisa dibagi dengan nol",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val hasil = when (operasi) {
            "*" -> angka1 * angka2
            "/" -> angka1 / angka2
            "+" -> angka1 + angka2
            "-" -> angka1 - angka2
            else -> return
        }

        if (!hasil.isFinite()) {
            Toast.makeText(
                this,
                "Hasilnya unvalid",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val catatan = "${formatAngka(angka1)} $operasi " +
                "${formatAngka(angka2)} = ${formatAngka(hasil)}"

        when (operasi) {
            "*" -> historyKali.add(catatan)
            "/" -> historyBagi.add(catatan)
            "+" -> historyTambah.add(catatan)
            "-" -> historyKurang.add(catatan)
        }

        hasilNegatifTerakhir = hasil < 0

        tampilkanFragment(operasi, hasilNegatifTerakhir)
    }

    private fun tampilkanFragment(
        operasi: String,
        negatif: Boolean
    ) {

        val fragment: Fragment = when (operasi) {
            "*" -> KaliFragment()
            "/" -> BagiFragment()
            "-" -> KurangFragment()
            else -> TambahFragment()
        }

        val history = when (operasi) {
            "*" -> historyKali
            "/" -> historyBagi
            "-" -> historyKurang
            else -> historyTambah
        }

        val bundle = Bundle()
        bundle.putStringArrayList("history", ArrayList(history))
        bundle.putBoolean("negatif", negatif)

        fragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.fragment_container,
            fragment
        )

        fragmentTransaction.commit()

        ubahBackground(negatif)
    }

    private fun ubahBackground(negatif: Boolean) {

        val mainLayout =
            findViewById<LinearLayout>(R.id.mainLayout)

        if (negatif) {
            mainLayout.setBackgroundColor(Color.RED)
        } else {
            mainLayout.setBackgroundColor(Color.WHITE)
        }
    }

    private fun formatAngka(angka: Double): String {
        return angka.toString().removeSuffix(".0")
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putStringArrayList("tambah", historyTambah)
        outState.putStringArrayList("kurang", historyKurang)
        outState.putStringArrayList("kali", historyKali)
        outState.putStringArrayList("bagi", historyBagi)

        outState.putBoolean(
            "negatifTerakhir",
            hasilNegatifTerakhir
        )

        super.onSaveInstanceState(outState)
    }
}
