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
import BO.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	private Spinner comuna_id;
	private EditText poblacion;
	private EditText calle;
	private EditText numero;
	private EditText telefono_fijo;
	private EditText telefono_movil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visitante_add);

		Thread tr = new Thread() {
			@Override
			public void run() {
				final String comunas = getComunas();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						cargarComunas(comunas);
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
		comuna_id = (Spinner) findViewById(R.id.spinnerComuna);
		telefono_fijo = (EditText) findViewById(R.id.editTextTelefonoFijo);
		telefono_movil = (EditText) findViewById(R.id.editTextTelefonoMovil);

		email.setText("abc@abc.abc");
		username.setText("Andres");
		password.setText("666");

	}

	private void iniciarEventos() {
		// TODO Auto-generated method stub
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(username.getText().toString().trim().equals("")){
					Toast.makeText(VisitanteAddActivity.this,
							"El username no puede estar en vacío.",
							Toast.LENGTH_LONG).show();
				} else if(password.getText().toString().trim().equals("")){
					Toast.makeText(VisitanteAddActivity.this,
							"El password no puede estar en vacío.",
							Toast.LENGTH_LONG).show();
				} else if(email.getText().toString() .trim().equals("")){
					Toast.makeText(VisitanteAddActivity.this,
							"El email no puede estar en vacío.",
							Toast.LENGTH_LONG).show();
				} else{
					EnviarOnClick(v);
				}
			}
		});
	}
	
	public void EnviarOnClick(View v) {
		Thread hilo = new Thread() {
			@Override
			public void run() {
				try {
					String res;
					int region = 8;
					Comuna comuna = (Comuna) comuna_id.getSelectedItem();

					res = enviarPost(rut.getText().toString(), nombre.getText()
							.toString(), apellido_paterno.getText().toString(),
							apellido_materno.getText().toString(),
							fecha_nacimiento.getText().toString(), email
									.getText().toString(), username.getText()
									.toString(), password.getText().toString(),
							region + "", comuna.getId() + "", poblacion
									.getText().toString(), calle.getText()
									.toString(), numero.getText().toString(), 
							telefono_fijo.getText().toString(), 
							telefono_movil.getText().toString());

					final JSONObject json = new JSONObject(res);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								if(!json.getString("mensaje").equals("EXITO")){
									Toast.makeText(VisitanteAddActivity.this,
											json.getString("mensaje"),
											Toast.LENGTH_LONG).show();
								}
								else{
									JSONObject usuarioJSON = json
											.getJSONObject("usuario");

									Toast.makeText(VisitanteAddActivity.this,
											"El registro se ha completado exitosamente.",
											Toast.LENGTH_LONG).show();
									
									Intent i = new Intent(VisitanteAddActivity.this,
											UserActivity.class);

									User usuario = new User(
										usuarioJSON.getInt("id"),usuarioJSON.getString("rut"),
										usuarioJSON.getString("nombre"),usuarioJSON.getString("apellido_paterno"),
										usuarioJSON.getString("apellido_materno"),usuarioJSON.getString("fecha_nacimiento"),
										usuarioJSON.getString("email"),usuarioJSON.getString("username"),
										usuarioJSON.getString("password"),usuarioJSON.getString("telefono_fijo"),
										usuarioJSON.getString("telefono_movil"),usuarioJSON.getString("poblacion"),
										usuarioJSON.getString("calle"),usuarioJSON.getString("numero"),
										usuarioJSON.getBoolean("estado"),usuarioJSON.getString("img"),
										usuarioJSON.getInt("rol_id"),
										usuarioJSON.getInt("comuna_id"),
										usuarioJSON.getString("created"),usuarioJSON.getString("modified"));
									
									i.putExtra("Usuario", usuario);
									startActivity(i);
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

	public String enviarPost(String rut, String nombre,String apellido_paterno, 
			String apellido_materno,String fecha_nacimiento, String email, 
			String username,String password, String region_id, String comuna_id,
			String poblacion, String calle, String numero,String telefono_fijo,
			String telefono_movil) {
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/users/guardar");
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
			params.add(new BasicNameValuePair("telefono_fijo", telefono_fijo));
			params.add(new BasicNameValuePair("telefono_movil", telefono_movil));

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
				"http://"+getString(R.string.IP)+"/Hop/comunas/comunas");
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
}