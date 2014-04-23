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
import org.json.JSONException;
import org.json.JSONObject;

import BO.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btn1;
	private Button btn2;
	private Button btn3;
	private EditText username;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iniciarComponentes();
		iniciarEventos();
	}

	private void iniciarComponentes() {
		// TODO Auto-generated method stub
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		btn3 = (Button) findViewById(R.id.button3);
		username = (EditText) findViewById(R.id.et_username);
		password = (EditText) findViewById(R.id.et_password);

		username.setText("edgar");
		password.setText("kkk");
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

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, UserActivity.class);
				i.putExtra("visitante", false);
				startActivity(i);
			}
		});

		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, UserActivity.class);
				i.putExtra("orden_menu", true);
				startActivity(i);
			}
		});
	}

	public void EnviarOnClick(View v) {
		Thread hilo = new Thread() {
			@Override
			public void run() {
				try {
					String res;

					res = enviarPost(username.getText().toString(), password
							.getText().toString());
					final JSONObject json = new JSONObject(res);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								if (!json.getString("mensaje").equals("EXITO")) {
									Toast.makeText(MainActivity.this,
											json.getString("mensaje"),
											Toast.LENGTH_LONG).show();
								} else {
									JSONObject usuarioJSON = json
											.getJSONObject("usuario");
									Intent i = new Intent(MainActivity.this,
											UserActivity.class);

									User usuario = new User(
											usuarioJSON.getInt("id"),
											usuarioJSON.getString("rut"),
											usuarioJSON.getString("nombre"),
											usuarioJSON
													.getString("apellido_paterno"),
											usuarioJSON
													.getString("apellido_materno"),
											usuarioJSON
													.getString("fecha_nacimiento"),
											usuarioJSON.getString("email"),
											usuarioJSON.getString("username"),
											usuarioJSON.getString("password"),
											usuarioJSON
													.getString("telefono_fijo"),
											usuarioJSON
													.getString("telefono_movil"),
											usuarioJSON.getString("poblacion"),
											usuarioJSON.getString("calle"),
											usuarioJSON.getInt("numero"),
											usuarioJSON
													.getInt("cant_votos_positivos"),
											usuarioJSON
													.getInt("cant_votos_negativos"),
											usuarioJSON.getBoolean("estado"),
											usuarioJSON.getString("img"),
											usuarioJSON.getInt("rol_id"),
											usuarioJSON.getInt("region_id"),
											usuarioJSON.getInt("comuna_id"),
											usuarioJSON.getString("local_id"),
											usuarioJSON.getString("created"),
											usuarioJSON.getString("modified"));

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

	public String enviarPost(String username, String password) {
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://192.168.1.126/Hop/users/loginAndroid");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("data[User][username]", username));
			params.add(new BasicNameValuePair("data[User][password]", password));

			httpPost.setEntity(new UrlEncodedFormEntity(params));
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
