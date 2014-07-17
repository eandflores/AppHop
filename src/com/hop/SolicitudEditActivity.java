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
import BO.Solicitud;
import BO.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SolicitudEditActivity extends Activity {
	
	private TextView accion;
	private TextView tabla;
	private TextView usuariotv;
	private TextView campos;
	
	private Button btn1;
	private Button btn2;
	
	private Solicitud solicitud;
	private User user;
	private User admin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solicitud_edit);

		iniciarComponentes();
		iniciarEventos();
	}
	
	private void iniciarComponentes() {
		// TODO Auto-generated method stub

		Intent i = getIntent();
		solicitud = (Solicitud) i.getSerializableExtra("solicitud");
		admin = (User) i.getSerializableExtra("Usuario");
		
		accion = (TextView) findViewById(R.id.textView1);
		tabla = (TextView) findViewById(R.id.textView2);
		usuariotv = (TextView) findViewById(R.id.textView3);
		campos = (TextView) findViewById(R.id.textView5);
		
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		
		accion.setText(solicitud.getAccion());
		tabla.setText(solicitud.getTabla());
		campos.setText(solicitud.getCampos());
		
		final int user_id = solicitud.getUser();
		
		Thread tr = new Thread() {
			@Override
			public void run() {
				final String usuario = getUsuario(user_id);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						cargarUsuario(usuario);
						usuariotv.setText(user.getUsername());
					}
				});
			}
		};

		tr.start();

	}

	private void iniciarEventos() {
		// TODO Auto-generated method stub
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EnviarOnClick(v,1);
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EnviarOnClick(v,2);
			}
		});
	}
	
	public void EnviarOnClick(View v,final int accion) {
		Thread hilo = new Thread() {
			@Override
			public void run() {
				try {
					String res;

					res = enviarPost(accion);
					final JSONObject json = new JSONObject(res);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								if (!json.getString("mensaje").equals("EXITO")) {
									Toast.makeText(SolicitudEditActivity.this,
											json.getString("mensaje"),
											Toast.LENGTH_LONG).show();
								} else {
									Intent i =new Intent();
									i.putExtra("Usuario", admin);
									
									if(accion == 1){
										Toast.makeText(SolicitudEditActivity.this,
												"Solicitud validada exitosamente.",
												Toast.LENGTH_LONG).show();
									}
									else{
										Toast.makeText(SolicitudEditActivity.this,
												"Solicitud rechazada exitosamente.",
												Toast.LENGTH_LONG).show();
									}
									
									setResult(RESULT_OK, i);
									finish();
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
	
	public String enviarPost(int accion) {
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/Solicituds/actualizarSolicitud");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("id", solicitud.getId()+""));
			params.add(new BasicNameValuePair("admin_id", admin.getId()+""));
			params.add(new BasicNameValuePair("accion", accion+""));

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}
	
	public String getUsuario(int id) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/Users/getUsuario");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(1);
			params.add(new BasicNameValuePair("id", id+""));

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}	
	
	public void cargarUsuario(String response) {
		try {
			JSONObject JSON = new JSONObject(response);
			JSONObject usuarioJSON = JSON.getJSONObject("usuario");
			
			user = new User(
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.solicitud_edit, menu);
		return true;
	}

}
