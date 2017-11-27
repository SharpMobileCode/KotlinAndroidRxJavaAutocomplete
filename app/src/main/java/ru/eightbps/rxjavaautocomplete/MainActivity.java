package ru.eightbps.rxjavaautocomplete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jakewharton.rxbinding.widget.AdapterViewItemClickEvent;
import com.jakewharton.rxbinding.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.eightbps.rxjavaautocomplete.data.model.Location;
import ru.eightbps.rxjavaautocomplete.data.model.PlaceAutocompleteResult;
import ru.eightbps.rxjavaautocomplete.data.model.PlaceDetailsResult;
import ru.eightbps.rxjavaautocomplete.data.model.Prediction;
import ru.eightbps.rxjavaautocomplete.data.RestClient;
import ru.eightbps.rxjavaautocomplete.utils.KeyboardHelper;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity  {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RxCustomAutoComplete autoCompleteTextView = findViewById(R.id.autocomplete_text);
        addOnAutoCompleteTextViewTextChangedObserver(autoCompleteTextView);
    }

    // TODO: 1. Put the Observable<PlaceAutocompleteResult> in the custom control
    // 2. Put the observer into the control
    // the only thing the main activity needs is compositeSubscription subscribe and unsubscribe.. maybe

    private void addOnAutoCompleteTextViewTextChangedObserver(final RxCustomAutoComplete autoCompleteTextView) {

        compositeSubscription.add(autoCompleteTextView.getSubscription());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }


}
