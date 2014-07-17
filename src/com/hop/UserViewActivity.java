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
import BO.Rol;
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
import android.widget.TextView;
import android.widget.Toast;

public class UserViewActivity extends Activity {
	
	private TextView username;
	private TextView rut;
	private TextView nombres;
	private TextView apellidos;
	private TextView rol;
	private TextView estado;
	private TextView email;
	private TextView telefono_fijo;
	private TextView telefono_movil;
	private TextView poblacion;
	private TextView calle;
	private TextView numero;
	private TextView comuna;
	
	private User usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_view);
		iniciarComponentes();
	}
	
	private void iniciarComponentes() {
		// TODO Auto-generated method stub
		username = (TextView) findViewById(R.id.textViewUsername);
		rut = (TextView) findViewById(R.id.textViewRut);
		nombres = (TextView) findViewById(R.id.textViewNombres);
		apellidos = (TextView) findViewById(R.id.textViewApellidos);
		rol = (TextView) findViewById(R.id.textViewRol);
		estado = (TextView) findViewById(R.id.textViewEstado);
		email = (TextView) findViewById(R.id.textViewEmail);
		telefono_fijo = (TextView) findViewById(R.id.textViewTelefonoFijo);
		telefono_movil = (TextView) findViewById(R.id.textViewTelefonoMovil);
		poblacion = (TextView) findViewById(R.id.textViewPoblacion);
		calle = (TextView) findViewById(R.id.textViewCalle);
		numero = (TextView) findViewById(R.id.textViewNumero);
		comuna = (TextView) findViewById(R.id.textViewComuna);
		
		Intent i = getIntent();
		usuario = (User) i.getSerializableExtra("Usuario");
		
		username.setText(usuario.getUsername());
		rut.setText(usuario.getRut());
		nombres.setText(usuario.getNombre());
		apellidos.setText(usuario.getApellidos());
		email.setText(usuario.getEmail());
		telefono_fijo.setText(usuario.getTelefonoFijo());
		telefono_movil.setText(usuario.getTelefonoMovil());
		poblacion.setText(usuario.getPoblacion());
		calle.setText(usuario.getCalle());
		numero.setText(usuario.getNumero());
		
		if(usuario.getEstado() == true){
			estado.setText("Habilitado");
		}
		else{
			estado.setText("Deshabilitado");
		}
		
		
		Thread hilo = new Thread() {
			@Override
			public void run() {
				try {
					final String res;
					
					res = getDatos();
					
					final JSONObject json = new JSONObject(res);
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								comuna.setText(json.getString("comunaNombre").toString());
								rol.setText(json.getString("rolNombre").toString());	
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
	
	public String getDatos() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/users/getDatos");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			
			params.add(new BasicNameValuePair("rol", usuario.getRol()+""));
			params.add(new BasicNameValuePair("comuna", usuario.getComuna()+""));
			
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultado;
	}	

}
