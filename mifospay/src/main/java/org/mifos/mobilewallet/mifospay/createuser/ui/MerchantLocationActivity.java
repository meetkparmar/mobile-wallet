package org.mifos.mobilewallet.mifospay.createuser.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.createuser.adapter.MerchantAdapter;
import org.mifos.mobilewallet.mifospay.passcode.ui.PassCodeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MerchantLocationActivity extends BaseActivity {

    @Inject
    MerchantAdapter adapter;

    @BindView(R.id.et_search_location)
    EditText etSearchLocation;

    @BindView(R.id.btn_locate)
    Button btnLocate;

    private int buttonState = 1;
    private List<String> addressList = new ArrayList<>();
    private GoogleMap mMap;
    private LatLng latLng;
    private LatLng latLng2;
    private Marker marker;
    private Geocoder geocoder;
    private int REQUEST_CODE = 1111;
    private Address address;
    private List<Address> addresses = new ArrayList<>();
    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_merchant_location);
        ButterKnife.bind(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        etSearchLocation.addTextChangedListener(textWatcher);

        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonState == 1){
                    searchLocation(v);
                    buttonState = 2;
                    btnLocate.setText(R.string.confirm);
                } else {
                    Intent intent = new Intent(MerchantLocationActivity.this, PassCodeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setUpMap() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address = addresses.get(0);
                if (address != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    addAddresses(address);
                }
                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker"));

                latLng2 = new LatLng(latLng.latitude+0.002, latLng.longitude);
                marker = mMap.addMarker(new MarkerOptions().position(latLng2));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    if (location != null) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                mMap = googleMap;
                                setUpMap();
                                if (latLng == null) {
                                    latLng = new LatLng(40.78733671598963, -73.97452078562641);
                                }
                                if (marker != null) {
                                    marker.remove();
                                }
                                marker = mMap.addMarker(new MarkerOptions().position(latLng));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                                latLng2 = new LatLng(latLng.latitude+0.002, latLng.longitude);
                                marker = mMap.addMarker(new MarkerOptions().position(latLng2));
                            }
                        });
                    } else {
                        Toast.makeText(MerchantLocationActivity.this, "Turn on your device location", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }


    public void searchLocation(View view) {
        String location = etSearchLocation.getText().toString();
        List<Address> addressList = new ArrayList<>();

        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }if (addressList.isEmpty()) {
            Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
            return;
        } else {
            address = addressList.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            getLocation();
            addAddresses(address);
        }
    }

    private void addAddresses(Address address) {
        try {
            addresses = geocoder.getFromLocation(address.getLatitude(), address.getLongitude(), 1);
            String addressLine = addresses.get(0).getAddressLine(0);

            List<Address> addresses2 = geocoder.getFromLocation(latLng2.latitude, latLng2.longitude, 1);
            String addressLine2 = addresses2.get(0).getAddressLine(0);

            addressList.clear();
            addressList.add(addressLine);
            addressList.add(addressLine2);

            showBottomSheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!etSearchLocation.getText().toString().equals("")) {
                btnLocate.setEnabled(true);
                btnLocate.setClickable(true);
            } else {
                btnLocate.setEnabled(false);
                btnLocate.setClickable(false);
            }
        }
    };

    private void showBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_merchant_list, null);
        dialog.setContentView(view);
        dialog.show();

        RecyclerView recyclerView = dialog.findViewById(R.id.rv_merchant);
        if (recyclerView != null) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(llm);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            adapter.setData(addressList);
        }
    }
}
