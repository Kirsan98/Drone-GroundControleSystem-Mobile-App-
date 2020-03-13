package fr.b.dronegcs_m;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng homePosition = new LatLng(44.804, -0.606);
        LatLng dronePosition = new LatLng(44.8037, -0.6057);



        MarkerOptions moHOME = new MarkerOptions();
        moHOME.position(homePosition).title("Home");
        mMap.addMarker(moHOME);

        MarkerOptions moDRONE = new MarkerOptions();
        moDRONE.position(dronePosition).title("Drone");
        moDRONE.icon(BitmapDescriptorFactory.fromResource(R.drawable.drone));
        mMap.addMarker(moDRONE);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(homePosition));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(homePosition, 18.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
}
