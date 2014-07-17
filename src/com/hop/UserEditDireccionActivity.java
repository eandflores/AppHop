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
import BO.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UserEditDireccionActivity extends Activity {
	
	private EditText poblacion;
	private EditText calle;
	private EditText numero;
	private Spinner comuna_id;
	
	private Button btn1;
	
	
	private User usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit_direccion);
		
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
		poblacion = (EditText) findViewById(R.id.editText1);
		calle = (EditText) findViewById(R.id.editText2);
		numero = (EditText) findViewById(R.id.editText3);
		comuna_id = (Spinner) findViewById(R.id.spinnerComuna);
		
		btn1 = (Button) findViewById(R.id.button1);
		
		Intent i = getIntent();
		usuario = (User) i.getSerializableExtra("Usuario");
		
		poblacion.setText(usuario.getPoblacion());
		calle.setText(usuario.getCalle());
		numero.setText(usuario.getNumero());
		
		if(usuario.getNumero().equals("null")){
			numero.setText("");
		}
		if(usuario.getCalle().equals("null")){
			calle.setText("");
		}
		if(usuario.getPoblacion().equals("null")){
			poblacion.setText("");
		}
	}
	
	private void iniciarEventos() {
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EnviarOnClick(v);
			}
		});
	}
	
	public void EnviarOnClick(View v) {
		Thread hilo = new Thread() {
			@Override
			public void run() {
				try {
					String res;
					
					res = enviarPost(usuario);
					
					final JSONObject json = new JSONObject(res);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								if(!json.getString("mensaje").equals("EXITO")){
									Toast.makeText(UserEditDireccionActivity.this,
											json.getString("mensaje"),
											Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(UserEditDireccionActivity.this,
											"Dirección actualizada exitosamente.",
											Toast.LENGTH_LONG).show();
									
									Intent intent = new Intent(UserEditDireccionActivity.this,
											UserActivity.class);
									usuario.setPoblacion(poblacion.getText().toString());
									usuario.setCalle(calle.getText().toString());
									usuario.setNumero(numero.getText().toString());
									
									Comuna comuna = (Comuna) comuna_id.getSelectedItem();
									usuario.setComuna(comuna.getId());
									
									intent.putExtra("Usuario",usuario);
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
	
	public String enviarPost(User u) {
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/users/actualizarDireccion");
		HttpResponse response = null;
		String resultado = null;

		try {
			int region = 8;
			Comuna comuna = (Comuna) comuna_id.getSelectedItem();
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(12);
			params.add(new BasicNameValuePair("id", u.getId()+""));
			params.add(new BasicNameValuePair("poblacion", poblacion.getText().toString()));
			params.add(new BasicNameValuePair("calle", calle.getText().toString()));
			params.add(new BasicNameValuePair("numero", numero.getText().toString()));
			params.add(new BasicNameValuePair("comuna_id", comuna.getId()+""));

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
			
			int position = 0;
			
			for (int j=0;j < listadoComunas.size();j++){
				   
			    if (listadoComunas.get(j).getId() == usuario.getComuna()){
				    position = j;
			    }
			}
			
			comuna_id.setSelection(position);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_edit_direccion, menu);
		return true;
	}

}
