package com.hop;

import BO.User;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class UserActivity extends TabActivity {

	private TabHost th;

	private void inicarComponentes() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		User usuario = (User) i.getSerializableExtra("Usuario");

		if (usuario != null) {
			if (usuario.getRol() == 1) {
				th = getTabHost();
				Resources r = getResources();

				TabSpec ts1 = th.newTabSpec("tab 1");
				ts1.setContent(new Intent(UserActivity.this,
						SearchActivity.class));
				ts1.setIndicator("Buscar", r.getDrawable(R.drawable.search));
				th.addTab(ts1);

				TabSpec ts2 = th.newTabSpec("tab 2");
				ts2.setContent(new Intent(UserActivity.this,
						SolicitudActivity.class));
				ts2.setIndicator("Solicitudes",
						r.getDrawable(R.drawable.solicitud));
				th.addTab(ts2);

				TabSpec ts3 = th.newTabSpec("tab 3");
				ts3.setContent(new Intent(UserActivity.this,
						SearchActivity.class));
				ts3.setIndicator("Notificaciones",
						r.getDrawable(R.drawable.notification));
				th.addTab(ts3);

				TabSpec ts4 = th.newTabSpec("tab 4");
				ts4.setContent(new Intent(UserActivity.this,
						SearchActivity.class));
				ts4.setIndicator("Cuenta", r.getDrawable(R.drawable.user));
				th.addTab(ts4);
			}
		} else {
			Intent intent = getIntent();
			boolean visitante = intent.getBooleanExtra("visitante", true);

			if (visitante == true) {
				th = getTabHost();
				Resources r = getResources();

				TabSpec ts1 = th.newTabSpec("tab 1");
				ts1.setContent(new Intent(UserActivity.this,
						SearchActivity.class));
				ts1.setIndicator("Buscar", r.getDrawable(R.drawable.search));

				TabSpec ts2 = th.newTabSpec("tab 2");
				ts2.setContent(new Intent(UserActivity.this,
						VisitanteAddActivity.class));
				ts2.setIndicator("Registrarse",
						r.getDrawable(R.drawable.search));

				th.addTab(ts1);
				th.addTab(ts2);
			} else {
				th = getTabHost();
				Resources r = getResources();

				TabSpec ts1 = th.newTabSpec("tab 2");
				ts1.setContent(new Intent(UserActivity.this,
						VisitanteAddActivity.class));
				ts1.setIndicator("Registrarse",
						r.getDrawable(R.drawable.search));

				th.addTab(ts1);
			}
		}
	}

	private void iniciarEventos() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_administrador);
		inicarComponentes();
		iniciarEventos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.administrador, menu);
		return true;
	}

}
