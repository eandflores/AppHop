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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

public class SearchActivity extends Activity {

	private ImageButton btn1;
	private AutoCompleteTextView tvBusqueda;

	public void cargarProductos(String response) {

		ArrayList<String> listadoProductos = new ArrayList<String>();

		try {
			JSONArray json = new JSONArray(response);

			for (int i = 0; i < json.length(); i++) {
				Producto producto = new Producto(json.getJSONObject(i).getInt(
						"id"), json.getJSONObject(i).getString("nombre"), json
						.getJSONObject(i).getInt("categoria_producto_id"), json
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

	public void EnviarOnClick(View v) {
		Thread hilo = new Thread() {
			@Override
			public void run() {
				try {

					String res;

					res = enviarPost(tvBusqueda.getText().toString());

					final JSONObject json = new JSONObject(res);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								if (!json.getString("mensaje").equals("EXITO")) {
									Toast.makeText(SearchActivity.this,
											json.getString("mensaje"),
											Toast.LENGTH_LONG).show();
								} else {
									JSONArray localesJSON = json
											.getJSONArray("locales");
									ArrayList<Local> listadoLocales = new ArrayList<Local>();

									Intent intent = new Intent(
											SearchActivity.this,
											PositionActivity.class);

									for (int i = 0; i < localesJSON.length(); i++) {
										Local local = new Local(
												localesJSON.getJSONObject(i)
														.getInt("id"),
												localesJSON.getJSONObject(i)
														.getString("nombre"),
												localesJSON.getJSONObject(i)
														.getString("calle"),
												localesJSON.getJSONObject(i)
														.getInt("numero"),
												localesJSON
														.getJSONObject(i)
														.getString(
																"telefono_fijo"),
												localesJSON
														.getJSONObject(i)
														.getString(
																"telefono_movil"),
												localesJSON.getJSONObject(i)
														.getString("email"),
												localesJSON.getJSONObject(i)
														.getString("sitio_web"),
												localesJSON.getJSONObject(i)
														.getBoolean("estado"),
												localesJSON.getJSONObject(i)
														.getString("img"),
												localesJSON
														.getJSONObject(i)
														.getInt("categoria_local_id"),
												localesJSON.getJSONObject(i)
														.getInt("user_id"),
												localesJSON.getJSONObject(i)
														.getInt("region_id"),
												localesJSON.getJSONObject(i)
														.getInt("comuna_id"),
												localesJSON.getJSONObject(i)
														.getString("created"),
												localesJSON.getJSONObject(i)
														.getString("modified"));

										listadoLocales.add(local);
									}

									intent.putExtra("Locales", listadoLocales);
									startActivity(intent);
								}
							} catch (JSONException e) {
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

	public String enviarPost(String nombre) {
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://192.168.1.126/Hop/Locals/locales");
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

	public String getProductos() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://192.168.1.126/Hop/Productos/productos");
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

	private void iniciarComponentes() {
		// TODO Auto-generated method stub

		btn1 = (ImageButton) findViewById(R.id.imageButton1);
		tvBusqueda = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
