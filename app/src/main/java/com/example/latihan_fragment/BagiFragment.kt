package com.example.latihan_fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class BagiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.activity_bagi_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val tvJudul = view.findViewById<TextView>(R.id.tvJudul)
        val tvHistory = view.findViewById<TextView>(R.id.tvHistory)
        val fragmentRoot = view.findViewById<LinearLayout>(R.id.fragmentRoot)

        val history = arguments?.getStringArrayList("history")
            ?: arrayListOf()

        val negatif = arguments?.getBoolean("negatif", false)
            ?: false

        if (history.isEmpty()) {
            tvHistory.text = "Belum ada history."
        } else {
            var isiHistory = ""

            for (i in 0 until history.size) {
                isiHistory += "${i + 1}. ${history[i]}\n"
            }

            tvHistory.text = isiHistory
        }

        if (negatif) {
            fragmentRoot.setBackgroundColor(Color.RED)
            tvJudul.setTextColor(Color.WHITE)
            tvHistory.setTextColor(Color.WHITE)
        } else {
            fragmentRoot.setBackgroundColor(Color.WHITE)
            tvJudul.setTextColor(Color.BLACK)
            tvHistory.setTextColor(Color.BLACK)
        }
    }
}
