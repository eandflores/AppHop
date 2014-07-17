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
import android.widget.Toast;

public class SolicitudActivity extends Activity {
	
	private ListView lvSolicitudes;
	private int CODIGO_EDITAR = 1;
	private User usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solicitud);
		
		Thread tr = new Thread() {
			@Override
			public void run() {
				final String solicitudes = getSolicitudes();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						cargarSolicitudes(solicitudes);
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
		
		Intent i = getIntent();
		usuario = (User) i.getSerializableExtra("Usuario");
		
		lvSolicitudes = (ListView) findViewById(R.id.lvSolicitudes);
		

	}

	private void iniciarEventos() {
		// TODO Auto-generated method stub
		lvSolicitudes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Solicitud s = (Solicitud) lvSolicitudes.getItemAtPosition(arg2);
				Intent i = new Intent(SolicitudActivity.this, SolicitudEditActivity.class);
				i.putExtra("Usuario", usuario);
				i.putExtra("solicitud", s);
				startActivityForResult(i, CODIGO_EDITAR);
			}	
		});
	}
	
	public String getSolicitudes() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/Solicituds/solicitudes");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("id", usuario.getId()+""));
			params.add(new BasicNameValuePair("rol", usuario.getRol()+""));
			
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}	
	
	public void cargarSolicitudes(String response) {
		ArrayList<Solicitud> listadoSolicitudes = new ArrayList<Solicitud>();

		try {
			JSONArray json = new JSONArray(response);
			
			for (int i = 0; i < json.length(); i++) {
				Solicitud solicitud = new Solicitud(
					json.getJSONObject(i).getInt("id"),json.getJSONObject(i).getString("estado"),
					json.getJSONObject(i).getString("sql"),json.getJSONObject(i).getString("accion"),
					json.getJSONObject(i).getString("tabla"),json.getJSONObject(i).getString("campos"),
					json.getJSONObject(i).getInt("user_id"),json.getJSONObject(i).getString("admin_id"),
					json.getJSONObject(i).getString("local_id"),json.getJSONObject(i).getString("created"),
					json.getJSONObject(i).getString("modified")
				);
				
				listadoSolicitudes.add(solicitud);
			}
			
			ArrayAdapter<Solicitud> solicitudesAdapter = new ArrayAdapter<Solicitud>(
					this, android.R.layout.simple_spinner_dropdown_item,
					listadoSolicitudes);
			
			lvSolicitudes.setAdapter(solicitudesAdapter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == CODIGO_EDITAR && resultCode == RESULT_OK) {
			usuario = (User) data.getSerializableExtra("Usuario");
			
			Thread tr = new Thread() {
				@Override
				public void run() {
					final String comunas = getSolicitudes();

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							cargarSolicitudes(comunas);
						}
					});
				}
			};

			tr.start();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.solicitud, menu);
		return true;
	}

}
