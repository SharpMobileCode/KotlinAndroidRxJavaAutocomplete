package ru.eightbps.rxjavaautocomplete

import android.Manifest
import android.R
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.AttributeSet
import android.util.Log
import android.webkit.PermissionRequest
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filterable
import android.widget.ListAdapter
import com.jakewharton.rxbinding.widget.RxTextView
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent
import ru.eightbps.rxjavaautocomplete.data.RestClient
import ru.eightbps.rxjavaautocomplete.data.model.PlaceAutocompleteResult
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * Created by neilwarner on 11/27/17.
 */
private const val DELAY_IN_MILLIS: Long = 500

class RxCustomAutoComplete @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.autoCompleteTextViewStyle) : AutoCompleteTextView(context, attrs, defStyleAttr) {


    var defaultLocation: String = "Dallas, TX"
    private var locationLatLong: String? = null

    init {

        //checkForGpsFunction()
        // pass it resource, etc.
        //super.setAdapter(CustomLocationAutoCompleteAdapter(context))

    }

    private val autocompleteResponseObservable = RxTextView.textChangeEvents(this)
            .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
            .map { textViewTextChangeEvent ->
                Log.d("NJW", "textViewChangeEvent")
                textViewTextChangeEvent.text().toString()
            }
            .filter { s ->
                Log.d("NJW", "ignoring those where lnegth < 2 characaters")

                s.length >= 2
            }
            .observeOn(Schedulers.io())
            .flatMap { s ->
                Log.d("NJW", "callling rest client for " + s)

                RestClient.INSTANCE.googlePlacesClient.autocomplete(s)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .retry()

    fun handleNetworkResult(list : List<String>) {
        val itemsAdapter = ArrayAdapter(this.context,
                R.layout.simple_list_item_1, list)
        this.setAdapter(itemsAdapter)
        val enteredText = this.getText().toString()
        if (list.size >= 1 && enteredText == list[0]) {
            this.dismissDropDown()
        }
        else {
            this.showDropDown()
        }
    }

    private val placeAutocompleteResultObserver = object : Observer<PlaceAutocompleteResult> {


        override fun onCompleted() {
            Log.i("NJW-network call", "onCompleted")
        }

        override fun onError(e: Throwable) {
            Log.e("NJW", "onError", e)
        }

        override fun onNext(placeAutocompleteResult: PlaceAutocompleteResult) {
            //Log.i("NJW", placeAutocompleteResult.toString());

            val list = ArrayList<String>()
            for (prediction in placeAutocompleteResult.predictions) {
                list.add(prediction.description)
            }

            this@RxCustomAutoComplete.handleNetworkResult(list)


        }
    }
    val subscription = autocompleteResponseObservable.subscribe(placeAutocompleteResultObserver)

    override fun <T> setAdapter(adapter: T) where T : ListAdapter?, T : Filterable? {
        //throw Exception("Don't set an adapter; this control includes its own adapter; please set listener instead.")
        super.setAdapter(adapter)
    }


}