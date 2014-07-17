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
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserEditNombresActivity extends Activity {

	private EditText nombres;
	private EditText apellido_paterno;
	private EditText apellido_materno;
	private Button btn1;
	
	private User usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit_nombres);
		iniciarComponentes();
		iniciarEventos();
	}
	
	private void iniciarComponentes() {
		// TODO Auto-generated method stub
		nombres = (EditText) findViewById(R.id.editText1);
		apellido_paterno = (EditText) findViewById(R.id.editText2);
		apellido_materno = (EditText) findViewById(R.id.editText3);
		
		btn1 = (Button) findViewById(R.id.button1);
		
		Intent i = getIntent();
		usuario = (User) i.getSerializableExtra("Usuario");
		
		nombres.setText(usuario.getNombre());
		apellido_paterno.setText(usuario.getApellidoPaterno());
		apellido_materno.setText(usuario.getApellidoMaterno());
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
									Toast.makeText(UserEditNombresActivity.this,
											json.getString("mensaje"),
											Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(UserEditNombresActivity.this,
											"Nombre actualizado exitosamente.",
											Toast.LENGTH_LONG).show();
									
									Intent intent = new Intent(UserEditNombresActivity.this,
											UserActivity.class);
									usuario.setNombre(nombres.getText().toString());
									usuario.setApellidoPaterno(apellido_paterno.getText().toString());
									usuario.setApellidoMaterno(apellido_materno.getText().toString());
									
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
				"http://"+getString(R.string.IP)+"/Hop/users/actualizarNombre");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(12);
			params.add(new BasicNameValuePair("id", u.getId()+""));
			params.add(new BasicNameValuePair("nombre", nombres.getText().toString()));
			params.add(new BasicNameValuePair("apellido_paterno", apellido_paterno.getText().toString()));
			params.add(new BasicNameValuePair("apellido_materno", apellido_materno.getText().toString()));

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
