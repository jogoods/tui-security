package tuisolutions.tuisecurity.ui;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;
public class LocationOnMap{
//public class LocationOnMap extends MapActivity {
//    
//    public static final String LATITUE = "latitue";
//    public static final String LONGTITUE = "longtitue";
//    private MapView mapView;
//    private MapController mapControler;
//    private GeoPoint point;
//    private double lat = 0.0;
//    private double lng = 0.0;
//    
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.show_in_maps);
//        
//        /**
//         * Set coordinates
//         */
//        
//        lat = getIntent().getExtras().getDouble(LONGTITUE);
//        lng = getIntent().getExtras().getDouble(LATITUE);
//        if (lat != 0.0 && lng != 0.0) {
//            Toast.makeText(getApplicationContext(), "Lat = " + String.valueOf(lat) + ", long = " + String.valueOf(lng), Toast.LENGTH_LONG).show();
//            
//            setCoordinates(lat, lng);
//            
//            mapView = (MapView) findViewById(R.id.mapview);
//            mapView.setBuiltInZoomControls(true);
//            mapView.setSatellite(true);
//            mapView.setTraffic(true);
//            
//            mapControler = mapView.getController();
//            
//            point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
//            
//            mapControler.animateTo(point);
//            mapControler.setZoom(14);
//            
//            // -- add a location marker
//            MapOverlay mapOverlay = new MapOverlay();
//            List<Overlay> listOverlays = mapView.getOverlays();
//            listOverlays.clear();
//            listOverlays.add(mapOverlay);
//            
//            mapView.invalidate();
//        } else {
//            Toast.makeText(getApplicationContext(), "Can't get locaton. Please try again.", Toast.LENGTH_SHORT).show();
//        }
//    }
//    
//    public void setCoordinates(double latitude, double longitude) {
//        this.lat = latitude;
//        this.lng = longitude;
//    }
//    
//    @Override
//    protected boolean isRouteDisplayed() {
//        return false;
//    }
//    
//    private class MapOverlay extends com.google.android.maps.Overlay {
//        
//        @Override
//        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
//            super.draw(canvas, mapView, shadow);
//            
//            // --translate the GeoPoint to screen pixels
//            Point screenPts = new Point();
//            mapView.getProjection().toPixels(point, screenPts);
//            
//            // --add the marker
//            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.flag_green);
//            canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 30, null);
//            return true;
//            
//        }
//        
//    }
//    
}
