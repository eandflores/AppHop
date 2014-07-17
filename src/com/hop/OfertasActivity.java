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

import BO.Local;
import BO.Oferta;
import BO.Producto;
import BO.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OfertasActivity extends Activity {
	
	private ListView lv;
	
	private User usuario;
	private Local local;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas);
        iniciarComponentes();
        iniciarEventos();
        
        Thread tr = new Thread() {
			@Override
			public void run() {
				final String ofertas = getOfertas();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						cargarOfertas(ofertas);
					}
				});
			}
		};

		tr.start();
    }
    
    private void iniciarComponentes() {
    	lv = (ListView) findViewById(R.id.lvOfertas);
    	
    	Intent i = getIntent();
		local = (Local) i.getSerializableExtra("Local");
		usuario = (User) i.getSerializableExtra("Usuario");
    }
    
    private void iniciarEventos() {
    	
    }
    
    public String getOfertas() {
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/Ofertas/ofertas");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(1);
			params.add(new BasicNameValuePair("local_id", ""+local.getId()));

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}
    
    private void cargarOfertas(String response){
   	 	
    	ArrayList<Oferta> listadoOfertas = new ArrayList<Oferta>();

		try {
			JSONArray json = new JSONArray(response);

			for (int i = 0; i < json.length(); i++) {
				Oferta oferta = new Oferta(json.getJSONObject(i).getInt("id"), 
					json.getJSONObject(i).getString("precio"), json.getJSONObject(i).getString("descripcion"), 
					json.getJSONObject(i).getInt("user_id"), json.getJSONObject(i).getInt("producto_id"), 
					json.getJSONObject(i).getInt("local_id"),json.getJSONObject(i).getString("created"), 
					json.getJSONObject(i).getString("modified"),json.getJSONObject(i).getString("producto_nombre"));

				listadoOfertas.add(oferta);
			}

			ArrayAdapter<Oferta> ofertasAdapter = new ArrayAdapter<Oferta>(
					this, android.R.layout.select_dialog_item, listadoOfertas);
			lv.setAdapter(ofertasAdapter);

		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ofertas, menu);
        return true;
    }
    
}