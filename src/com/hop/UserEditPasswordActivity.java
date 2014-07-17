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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserEditPasswordActivity extends Activity {

	private EditText password;
	private EditText password2;
	private EditText password3;
	
	private Button btn1;
	
	private User usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit_password);
		iniciarComponentes();
		iniciarEventos();
	}
	
	private void iniciarComponentes() {
		// TODO Auto-generated method stub
		password = (EditText) findViewById(R.id.editText1);
		password2 = (EditText) findViewById(R.id.editText2);
		password3 = (EditText) findViewById(R.id.editText3);
		
		btn1 = (Button) findViewById(R.id.button1);
		
		Intent i = getIntent();
		usuario = (User) i.getSerializableExtra("Usuario");
	
	}
	
	private void iniciarEventos() {
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(password.getText().toString().trim().equals("")){
					Toast.makeText(UserEditPasswordActivity.this,
							"El password actual no puede estar vacío.",
							Toast.LENGTH_LONG).show();
				} else if(password3.getText().toString().trim().equals("")){
					Toast.makeText(UserEditPasswordActivity.this,
							"El password nuevo no puede estar vacío.",
							Toast.LENGTH_LONG).show();
				} else if(password.getText().toString().equals(password2.getText().toString())){
					EnviarOnClick(v);
				} else{
					Toast.makeText(UserEditPasswordActivity.this,
							"Los passwords actuales no coinciden.",
							Toast.LENGTH_LONG).show();
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

					res = enviarPost(usuario);
					
					final JSONObject json = new JSONObject(res);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								if(!json.getString("mensaje").equals("EXITO")){
									Toast.makeText(UserEditPasswordActivity.this,
											json.getString("mensaje"),
											Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(UserEditPasswordActivity.this,
											"Password actualizado exitosamente.",
											Toast.LENGTH_LONG).show();
									
									Intent intent = new Intent(UserEditPasswordActivity.this,
											UserActivity.class);
									usuario.setPassword(json.getString("password").toString());
									
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
				"http://"+getString(R.string.IP)+"/Hop/users/actualizarPassword");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(12);
			params.add(new BasicNameValuePair("id", u.getId()+""));
			params.add(new BasicNameValuePair("passwordAntiguo", password.getText().toString()));
			params.add(new BasicNameValuePair("passwordNuevo", password3.getText().toString()));

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
		getMenuInflater().inflate(R.menu.user_edit_password, menu);
		return true;
	}

}
