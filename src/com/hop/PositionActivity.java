package com.hop;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PositionActivity extends FragmentActivity {

	private ListView lv;
	private ArrayList locales;
	private ArrayAdapter adaptador;

	private void cargarDatos() {
		try {
			locales.add("Quillota");
			locales.add("Don Pedro");
			locales.add("Ignacio");
			locales.add("Libreria Sofia");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(PositionActivity.this, e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		adaptador = new ArrayAdapter<String>(PositionActivity.this,
				android.R.layout.simple_list_item_1, locales);
		lv.setAdapter(adaptador);
	}

	private void iniciarComponentes() {
		// TODO Auto-generated method stub

	}

	private void iniciarEventos() {
		// TODO Auto-generated method stub

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String elemento = (String) lv.getItemAtPosition(arg2);
				Intent i = new Intent(PositionActivity.this, MainActivity.class);
				i.putExtra("local", elemento);
				startActivity(i);
			}
		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position);
		//iniciarComponentes();
		//iniciarEventos();
		//cargarDatos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.position, menu);
		return true;
	}
}