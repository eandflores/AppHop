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

import BO.Comuna;
import BO.Region;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class VisitanteAddActivity extends Activity {

	private Button btn1;
	private EditText rut;
	private EditText nombre;
	private EditText apellido_paterno;
	private EditText apellido_materno;
	private EditText fecha_nacimiento;
	private EditText email;
	private EditText username;
	private EditText password;
	private Spinner region_id;
	private Spinner comuna_id;
	private EditText poblacion;
	private EditText calle;
	private EditText numero;

	public void cargarComunas(String response) {
		ArrayList<Comuna> listadoComunas = new ArrayList<Comuna>();

		try {
			JSONArray json = new JSONArray(response);

			for (int i = 0; i < json.length(); i++) {
				Comuna comuna = new Comuna(json.getJSONObject(i).getInt("id"),
						json.getJSONObject(i).getString("nombre").toString(),
						json.getJSONObject(i).getInt("region_id"));
				listadoComunas.add(comuna);
			}

			ArrayAdapter<Comuna> comunasAdapter = new ArrayAdapter<Comuna>(
					this, android.R.layout.simple_spinner_dropdown_item,
					listadoComunas);
			comuna_id.setAdapter(comunasAdapter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cargarRegiones(String response) {
		ArrayList<Region> listadoRegiones = new ArrayList<Region>();

		try {
			JSONArray json = new JSONArray(response);

			for (int i = 0; i < json.length(); i++) {
				Region region = new Region(json.getJSONObject(i).getInt("id"),
						json.getJSONObject(i).getString("numero").toString(),
						json.getJSONObject(i).getString("nombre").toString());
				listadoRegiones.add(region);
			}

			ArrayAdapter<Region> regionesAdapter = new ArrayAdapter<Region>(
					this, android.R.layout.simple_spinner_dropdown_item,
					listadoRegiones);
			region_id.setAdapter(regionesAdapter);

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
					Region region = (Region) region_id.getSelectedItem();
					Comuna comuna = (Comuna) comuna_id.getSelectedItem();

					res = enviarPost(rut.getText().toString(), nombre.getText()
							.toString(), apellido_paterno.getText().toString(),
							apellido_materno.getText().toString(),
							fecha_nacimiento.getText().toString(), email
									.getText().toString(), username.getText()
									.toString(), password.getText().toString(),
							region.getId() + "", comuna.getId() + "", poblacion
									.getText().toString(), calle.getText()
									.toString(), numero.getText().toString());

					final JSONObject json = new JSONObject(res);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								Toast.makeText(VisitanteAddActivity.this,
										json.getString("mensaje"),
										Toast.LENGTH_LONG).show();
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

	public String enviarPost(String rut, String nombre,
			String apellido_paterno, String apellido_materno,
			String fecha_nacimiento, String email, String username,
			String password, String region_id, String comuna_id,
			String poblacion, String calle, String numero) {
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://192.168.1.126/Hop/users/guardar");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(12);
			params.add(new BasicNameValuePair("rut", rut));
			params.add(new BasicNameValuePair("nombre", nombre));
			params.add(new BasicNameValuePair("apellido_paterno",
					apellido_paterno));
			params.add(new BasicNameValuePair("apellido_materno",
					apellido_materno));
			params.add(new BasicNameValuePair("fecha_nacimiento",
					fecha_nacimiento));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("poblacion", poblacion));
			params.add(new BasicNameValuePair("calle", calle));
			params.add(new BasicNameValuePair("numero", numero));
			params.add(new BasicNameValuePair("region_id", region_id));
			params.add(new BasicNameValuePair("comuna_id", comuna_id));

			params.add(new BasicNameValuePair("rol_id", "2"));

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}

	public String getComunas() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://192.168.1.126/Hop/comunas/comunas");
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

	public String getRegiones() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://192.168.1.126/Hop/regions/regiones");
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

		btn1 = (Button) findViewById(R.id.button1);
		rut = (EditText) findViewById(R.id.editTextRut);
		nombre = (EditText) findViewById(R.id.editTextNombre);
		apellido_paterno = (EditText) findViewById(R.id.editTextApellidoPaterno);
		apellido_materno = (EditText) findViewById(R.id.editTextApellidoMaterno);
		fecha_nacimiento = (EditText) findViewById(R.id.editTextFechaNacimiento);
		email = (EditText) findViewById(R.id.editTextEmail);
		username = (EditText) findViewById(R.id.editTextUsername);
		password = (EditText) findViewById(R.id.editTextPassword);
		poblacion = (EditText) findViewById(R.id.editTextPoblacion);
		calle = (EditText) findViewById(R.id.editTextCalle);
		numero = (EditText) findViewById(R.id.editTextNumero);
		region_id = (Spinner) findViewById(R.id.spinnerRegion);
		comuna_id = (Spinner) findViewById(R.id.spinnerComuna);

		rut.setText("16.666.666-6");
		nombre.setText("Andres");
		apellido_paterno.setText("Flores");
		apellido_materno.setText("Osben");
		fecha_nacimiento.setText("2013-08-11");
		email.setText("abc@abc.abc");
		username.setText("Andres");
		password.setText("666");
		poblacion.setText("ajjajajaj");
		calle.setText("ajajja");
		numero.setText("123");

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
		setContentView(R.layout.activity_visitante_add);

		Thread tr = new Thread() {
			@Override
			public void run() {
				final String regiones = getRegiones();
				final String comunas = getComunas();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						cargarRegiones(regiones);
						cargarComunas(comunas);
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