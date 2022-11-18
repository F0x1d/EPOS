package com.f0x1d.epos.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.f0x1d.epos.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class InfoBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(title: String, text: String): InfoBottomSheet {
            val bundle = Bundle().apply {
                putString("title", title)
                putString("text", text)
            }
            return InfoBottomSheet().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.sheet_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.title).text = requireArguments().getString("title")
        view.findViewById<TextView>(R.id.text).text = requireArguments().getString("text")
        view.findViewById<MaterialButton>(R.id.ok_btn).setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        BottomSheetBehavior.from(requireView().parent as View).state = BottomSheetBehavior.STATE_EXPANDED
    }
}