package com.hop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import BO.Local;
import BO.Producto;
import BO.User;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class PositionActivity extends FragmentActivity {

	private GoogleMap mapa;
    private int vista = 0;
    ArrayList<Local> locales = new ArrayList<Local>();
    
    private float latitud = (float) -36.818905;
    private float longitud = (float) -73.051551;
    
    private ImageButton btn1;
	private AutoCompleteTextView tvBusqueda;
	
	private User usuario;
	String nombreProducto = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position);
		
		Thread tr = new Thread() {
			@Override
			public void run() {
				final String locales = getProductos();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						cargarProductos(locales);
					}
				});
			}
		};

		tr.start();
		
		iniciarComponentes();
		iniciarEventos();
	}
	
	private void iniciarComponentes() {
		// TODO Auto-generated method stub
		btn1 = (ImageButton) findViewById(R.id.imageButton1);
		tvBusqueda = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

		Intent i = getIntent();
		nombreProducto = i.getStringExtra("nombreProducto");
		usuario = (User) i.getSerializableExtra("Usuario");
		
		cargarLocales(nombreProducto);
	}
	
	private void iniciarEventos() {
		// TODO Auto-generated method stub
		
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EnviarOnClick(v);
			}
		});

	}
	
	public void EnviarOnClick(View v) {
		Intent intent = new Intent(
				PositionActivity.this,
				PositionActivity.class);
		
		intent.putExtra("nombreProducto",tvBusqueda.getText().toString());
		startActivity(intent);
		
	}
	
    private void setUpMarker(Local local) throws IOException {
    	
    	Geocoder geocoder = new Geocoder(new Activity()); 
    	List<Address> localAddress =  geocoder.getFromLocationName(local.getDireccion()+" Concepción, Concepción, Biobío, Chile", 1);

        LatLng localCordenanda = new LatLng(localAddress.get(0).getLatitude(),localAddress.get(0).getLongitude());
        
        Log.e("LATI",localAddress.get(0).getLatitude()+""+localAddress.get(0).getLongitude());
        Log.e("LON", localAddress.get(0).getLocality()+" "+localAddress.get(0).getCountryName());
        
        mapa.addMarker(new MarkerOptions()
        	.position(localCordenanda)
        	.title(local.getNombre())
        );
    }
    
    protected void onResume() {
        super.onResume();
        
        mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        mapa.setMyLocationEnabled(true);
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
        LatLng localCordenanda = new LatLng(latitud, longitud);
        
        CameraPosition camPos = new CameraPosition.Builder().target(localCordenanda).
        		zoom(14).bearing(0).tilt(0).build();

        CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd);
        
        mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
		    
			public boolean onMarkerClick(Marker marker) {
		        
		    	Intent intent = new Intent(
						PositionActivity.this,
						LocalViewActivity.class);
				
		    	intent.putExtra("nombreProducto",nombreProducto);
				intent.putExtra("nombreLocal",marker.getTitle());
				intent.putExtra("Usuario",usuario);
				startActivity(intent);
				
				return false;
		    }
		});
    }
    
    public String getLocales(String nombre) {
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/Locals/locales");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(12);
			params.add(new BasicNameValuePair("nombre", nombre));

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}
    
    public void cargarLocales(final String nombreProducto){
    	Thread hilo = new Thread() {
			@Override
			public void run() {
				try {

					String res;

					res = getLocales(nombreProducto);
					
					final JSONObject json = new JSONObject(res);
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								
								if (!json.getString("mensaje").equals("EXITO")) {
									Toast.makeText(PositionActivity.this,
											json.getString("mensaje"),
											Toast.LENGTH_LONG).show();
								} else {
									JSONArray localesJSON = json.getJSONArray("locales");
									ArrayList<Local> listadoLocales = new ArrayList<Local>();

									for (int i = 0; i < localesJSON.length(); i++) {
										Local local = new Local(
											localesJSON.getJSONObject(i).getInt("id"),
											localesJSON.getJSONObject(i).getString("nombre"),
											localesJSON.getJSONObject(i).getString("calle"),
											localesJSON.getJSONObject(i).getInt("numero"),
											localesJSON.getJSONObject(i).getString("telefono_fijo"),
											localesJSON.getJSONObject(i).getString("telefono_movil"),
											localesJSON.getJSONObject(i).getString("email"),
											localesJSON.getJSONObject(i).getString("sitio_web"),
											localesJSON.getJSONObject(i).getBoolean("estado"),
											localesJSON.getJSONObject(i).getString("img"),
											localesJSON.getJSONObject(i).getInt("categoria_local_id"),
											localesJSON.getJSONObject(i).getInt("user_id"),
											localesJSON.getJSONObject(i).getInt("comuna_id"),
											localesJSON.getJSONObject(i).getString("created"),
											localesJSON.getJSONObject(i).getString("modified"));
										
										setUpMarker(local);
										listadoLocales.add(local);
									}			
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		hilo.start();
    }
    
    public String getProductos() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/Productos/productos");
		HttpResponse response = null;
		String resultado = null;

		try {
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}
    
    public void cargarProductos(String response) {

		ArrayList<String> listadoProductos = new ArrayList<String>();

		try {
			JSONArray json = new JSONArray(response);

			for (int i = 0; i < json.length(); i++) {
				Producto producto = new Producto(json.getJSONObject(i).getInt(
						"id"), json.getJSONObject(i).getString("nombre"), json
						.getJSONObject(i).getInt("subcategoria_producto_id"), json
						.getJSONObject(i).getInt("user_id"), json
						.getJSONObject(i).getString("created"), json
						.getJSONObject(i).getString("modified"));

				listadoProductos.add(producto.toString());
			}

			ArrayAdapter<String> productosAdapter = new ArrayAdapter<String>(
					this, android.R.layout.select_dialog_item, listadoProductos);
			tvBusqueda.setAdapter(productosAdapter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.position, menu);
		return true;
	}
	
	private void alternarVista() {
        vista = (vista + 1) % 4;

        switch(vista){
            case 0: mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL); break;
            case 1: mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID); break;
            case 2: mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE); break;
            case 3: mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN); break;
        }
    }
	
	 @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        
        if (itemId == R.id.menu_vista) {
            alternarVista();
        } 
        else if (itemId == R.id.menu_mover) {
            CameraUpdate camUpd = CameraUpdateFactory.newLatLng(new LatLng(latitud, longitud));
            mapa.moveCamera(camUpd);
        } 
        else if (itemId == R.id.menu_3d) {
            LatLng restaurant = new LatLng(latitud, longitud);
            CameraPosition camPos = new CameraPosition.Builder().target(restaurant).
            		zoom(18).bearing(45).tilt(70).build();
            CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
            mapa.animateCamera(camUpd);
        } 
        else if (itemId == R.id.menu_posicion) {
            CameraPosition camPos = mapa.getCameraPosition();
            LatLng pos = camPos.target;
            Toast.makeText(PositionActivity.this,"Lat: " + pos.latitude + " - Lng: " + pos.longitude,
                    Toast.LENGTH_LONG).show();
        } 
        else if (itemId == R.id.menu_como_llegar) {
            String uri = "http://maps.google.com/maps?saddr="+latitud+","+longitud+"&daddr="+latitud +","+longitud;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}