package com.example.vital.lab2

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_fragment.view.*

class MovieFragment: DialogFragment() {
    private var imdbID: String? = null
    private var title: String? = null
    private var year: String? = null
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString("title")
        year = arguments?.getString("year")
        type = arguments?.getString("type")
        imdbID = arguments?.getString("imdbID")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_fragment, container)

        rootView.title.text = title
        rootView.year.text = year
        rootView.type.text = type
        rootView.imdbID.text = imdbID

        rootView.buttonClose.setOnClickListener {
            dismiss()
        }

        return rootView
    }
}