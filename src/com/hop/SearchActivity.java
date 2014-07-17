package com.hop;

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

import BO.Local;
import BO.Producto;
import BO.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

public class SearchActivity extends Activity {

	private ImageButton btn1;
	private AutoCompleteTextView tvBusqueda;
	private User usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

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
		usuario = (User) i.getSerializableExtra("Usuario");
		
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
				SearchActivity.this,
				PositionActivity.class);
		
		intent.putExtra("nombreProducto",tvBusqueda.getText().toString());
		intent.putExtra("Usuario",usuario);
		startActivity(intent);
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
