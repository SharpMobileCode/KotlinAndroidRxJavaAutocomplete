package ru.eightbps.rxjavaautocomplete

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.AttributeSet
import android.webkit.PermissionRequest
import android.widget.AutoCompleteTextView
import android.widget.Filterable
import android.widget.ListAdapter

/**
 * Created by neilwarner on 11/27/17.
 */
class RxCustomAutoComplete @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.autoCompleteTextViewStyle) : AutoCompleteTextView(context, attrs, defStyleAttr) {


    var defaultLocation: String = "Dallas, TX"
    private var locationLatLong: String? = null

    init {

        //checkForGpsFunction()
        // pass it resource, etc.
        //super.setAdapter(CustomLocationAutoCompleteAdapter(context))

    }


    override fun <T> setAdapter(adapter: T) where T : ListAdapter?, T : Filterable? {
        //throw Exception("Don't set an adapter; this control includes its own adapter; please set listener instead.")
        super.setAdapter(adapter)
    }


}