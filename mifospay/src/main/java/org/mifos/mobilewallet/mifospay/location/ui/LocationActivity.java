package org.mifos.mobilewallet.mifospay.location.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import org.mifos.mobilewallet.core.domain.model.uspf.AddAddressResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.ClientAddress;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.location.contract.LocationContract;
import org.mifos.mobilewallet.mifospay.location.presenter.LocationPresenter;
import org.mifos.mobilewallet.mifospay.utils.Constants;
import org.mifos.mobilewallet.mifospay.utils.Toaster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends BaseActivity implements LocationContract.LocationView{

    @Inject
    LocationPresenter mPresenter;

    LocationContract.locationPresenter mLocationPresenter;

    @BindView(R.id.cv_address)
    CardView cvAddress;

    @BindView(R.id.et_search_location)
    EditText editText;

    @BindView(R.id.tv_address_line1)
    TextView tvAddressLine1;

    @BindView(R.id.tv_address_line2)
    TextView tvAddressLine2;

    @BindView(R.id.btn_locate)
    Button btnLocate;

    private int buttonState = 1;
    private ProgressDialog progressDialog;
    private GoogleMap mMap;
    private LatLng latLng;
    private Marker marker;
    Geocoder geocoder;
    int REQUEST_CODE = 1111;
    Address address;
    List<Address> addresses = new ArrayList<>();
    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        progressDialog = new ProgressDialog(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        editText.addTextChangedListener(textWatcher);

        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonState == 1) {
                    searchLocation(v);
                    buttonState = 2;
                    btnLocate.setText(R.string.confirm);
                } else {
                    // addressTypeId is hardcode
                    ClientAddress address = new ClientAddress(
                            "17",
                            addresses.get(0).getAddressLine(0).split(",")[0],
                            addresses.get(0).getAddressLine(0).split(",")[1],
                            addresses.get(0).getAddressLine(0).split(",")[2],
                            addresses.get(0).getLocality(),
                            addresses.get(0).getPostalCode());
                    showLoadingDialog("Loading...");
                    mLocationPresenter.addAddress(address, 0, 17);
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
                    showAddress(address);
                }
                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker"));
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
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
                            }
                        });
                    } else {
                        Toast.makeText(LocationActivity.this, "Turn on your device location", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    public void searchLocation(View view) {
        String location = editText.getText().toString();
        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null) {
            address = addressList.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            getLocation();
            showAddress(address);
        }
    }

    private void showAddress(Address address) {
        try {
            addresses = geocoder.getFromLocation(address.getLatitude(), address.getLongitude(), 1);
            String addressLine = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            cvAddress.setVisibility(View.VISIBLE);
            tvAddressLine1.setText(addressLine.split(",")[0]);
            tvAddressLine2.setText(city + ", " + state + ", " + country);

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
            if (!editText.getText().toString().equals("")) {
                btnLocate.setEnabled(true);
                btnLocate.setClickable(true);
            } else {
                btnLocate.setEnabled(false);
                btnLocate.setClickable(false);
            }
        }
    };

    @Override
    public void showToast(String message) {
        hideLoadingDialog();
        Toaster.showToast(this, message);
    }

    @Override
    public void showAddAddressResult(AddAddressResponseBody addAddressResponseBody) {
        showToast(Constants.ADDRESS_ADDED_SUCCESSFULLY);
        finish();
    }

    @Override
    public void setPresenter(LocationContract.locationPresenter presenter) {
        mLocationPresenter = presenter;
    }

    private void showLoadingDialog(String message) {
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    private void hideLoadingDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
}
