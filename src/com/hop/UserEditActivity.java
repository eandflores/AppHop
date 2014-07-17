package com.hop;

import BO.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class UserEditActivity extends Activity {
	
	private LinearLayout layoutImagen;
	private LinearLayout layoutInformacion;
	private LinearLayout layoutEmail;
	private LinearLayout layoutContrasena;
	private LinearLayout layoutNombres;
	private LinearLayout layoutTelefonos;
	private LinearLayout layoutDireccion;
	
	private User usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit);
		iniciarComponentes();
		iniciarEventos();
	}
	
	private void iniciarComponentes() {
		// TODO Auto-generated method stub
		layoutInformacion = (LinearLayout) findViewById(R.id.LayoutInformacion);
		layoutImagen = (LinearLayout) findViewById(R.id.LayoutImagen);
		layoutEmail = (LinearLayout) findViewById(R.id.LayoutEmail);
		layoutContrasena = (LinearLayout) findViewById(R.id.LayoutContrasena);
		layoutNombres = (LinearLayout) findViewById(R.id.LayoutNombres);
		layoutTelefonos = (LinearLayout) findViewById(R.id.LayoutTelefonos);
		layoutDireccion = (LinearLayout) findViewById(R.id.LayoutDireccion);
		
		Intent i = getIntent();
		usuario = (User) i.getSerializableExtra("Usuario");
		
		layoutImagen.setVisibility(View.INVISIBLE);
	}
	
	private void iniciarEventos() {
		// TODO Auto-generated method stub
		
		layoutInformacion.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserEditActivity.this, UserViewActivity.class);
				i.putExtra("Usuario", usuario);
				startActivity(i);
			}
		});
		
		/*
		layoutImagen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserEditActivity.this, UserViewActivity.class);
				i.putExtra("Usuario", usuario);
				startActivity(i);
			}
		});*/
		
		layoutEmail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserEditActivity.this, UserEditEmailActivity.class);
				i.putExtra("Usuario", usuario);
				startActivity(i);
			}
		});
		
		layoutContrasena.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserEditActivity.this, UserEditPasswordActivity.class);
				i.putExtra("Usuario", usuario);
				startActivity(i);
			}
		});
		
		layoutNombres.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserEditActivity.this, UserEditNombresActivity.class);
				i.putExtra("Usuario", usuario);
				startActivity(i);
			}
		});
		
		layoutTelefonos.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserEditActivity.this, UserEditTelefonosActivity.class);
				i.putExtra("Usuario", usuario);
				startActivity(i);
			}
		});
		
		layoutDireccion.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserEditActivity.this, UserEditDireccionActivity.class);
				i.putExtra("Usuario", usuario);
				startActivity(i);
			}
		});
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_edit, menu);
		return true;
	}

}
