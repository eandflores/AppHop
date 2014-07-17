package com.hop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import BO.Local;
import BO.Rol;
import BO.User;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocalViewActivity extends Activity {
	
	private ImageView img;
	private TextView nombre;
	private TextView categoria;
	private TextView sitio_web;
	private TextView telefono_fijo;
	private TextView telefono_movil;
	private TextView calle;
	private TextView numero;
	private TextView comuna;
	private TextView votos_positivos;
	private TextView votos_negativos;
	
	private Button btnLike;
	private Button btnDislike;
	private Button btnOfertas;
	
	private User usuario;
	private Local local;
	
	String local_nombre = "";
	String producto_nombre = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_view);
		iniciarComponentes();
		iniciarEventos();
	}
	
	private void iniciarComponentes() {
		// TODO Auto-generated method stub
		img = (ImageView) findViewById(R.id.imageView1);
		nombre = (TextView) findViewById(R.id.textViewNombre);
		categoria = (TextView) findViewById(R.id.textViewCategoria);
		sitio_web = (TextView) findViewById(R.id.textViewSitioWeb);
		telefono_fijo = (TextView) findViewById(R.id.textViewTelefonoFijo);
		telefono_movil = (TextView) findViewById(R.id.textViewTelefonoMovil);
		calle = (TextView) findViewById(R.id.textViewCalle);
		numero = (TextView) findViewById(R.id.textViewNumero);
		comuna = (TextView) findViewById(R.id.textViewComuna);
		votos_positivos = (TextView) findViewById(R.id.textViewVotosPositivos);
		votos_negativos = (TextView) findViewById(R.id.textViewVotosNegativos);
		
		btnOfertas = (Button) findViewById(R.id.button1);
		btnLike = (Button) findViewById(R.id.button2);
		btnDislike = (Button) findViewById(R.id.button3);
		
		Intent i = getIntent();
		usuario = (User) i.getSerializableExtra("Usuario");
		local_nombre = i.getStringExtra("nombreLocal");
		producto_nombre = i.getStringExtra("nombreProducto");
		
		if(usuario == null){
			btnLike.setVisibility(View.INVISIBLE);
			btnDislike.setVisibility(View.INVISIBLE);
		}
		
		Thread hilo = new Thread() {
			@Override
			public void run() {
				try {
					final String res;
					
					res = getLocal(local_nombre);
					
					final JSONObject json = new JSONObject(res);
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								local = new Local(
									json.getInt("id"),json.getString("nombre"),
									json.getString("calle"),json.getInt("numero"),
									json.getString("telefono_fijo"),json.getString("telefono_movil"),
									json.getString("email"),json.getString("sitio_web"),
									json.getBoolean("estado"),json.getString("img"),
									json.getInt("categoria_local_id"),json.getInt("user_id"),
									json.getInt("comuna_id"),
									json.getString("created"),json.getString("modified")
								);
								
								StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
								StrictMode.setThreadPolicy(policy); 
								
								img.setImageBitmap(getBitmapFromURL("http://"+getString(R.string.IP)+local.getImg()));
								
								nombre.setText(local.getNombre());
								sitio_web.setText(local.getSitioWeb());
								telefono_fijo.setText(local.getTelefonoFijo());
								telefono_movil.setText(local.getTelefonoMovil());
								calle.setText(local.getCalle());
								numero.setText(""+local.getNumero());
								
								Thread hilo2 = new Thread() {
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
														categoria.setText(json.getString("categoriaLocalNombre").toString());
														votos_positivos.setText(json.getString("votosPositivos").toString());
														votos_negativos.setText(json.getString("votosNegativos").toString());
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

								hilo2.start();
									
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
	
	private void iniciarEventos() {
		// TODO Auto-generated method stub
		
		btnLike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EnviarOnClick("positivo");
			}
		});
		
		btnDislike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EnviarOnClick("negativo");
			}
		});
		
		btnOfertas.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						LocalViewActivity.this,
						OfertasActivity.class);
				
				intent.putExtra("Local",local);
				intent.putExtra("Usuario",usuario);
				startActivity(intent);
			}
		});

	}
	
	public void EnviarOnClick(final String tipo) {
		
		Thread hilo = new Thread() {
			@Override
			public void run() {
				try {

					String res;

					res = enviarVoto(tipo);
					
					final JSONObject json = new JSONObject(res);
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								
								if (!json.getString("mensaje").equals("EXITO")) {
									Toast.makeText(LocalViewActivity.this,
											json.getString("mensaje"),
											Toast.LENGTH_LONG).show();
								} else {
									Thread hilo2 = new Thread() {
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
															votos_positivos.setText(json.getString("votosPositivos").toString());
															votos_negativos.setText(json.getString("votosNegativos").toString());
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

									hilo2.start();
									
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
	
	public String getLocal(String nombre) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/Locals/getLocal");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(1);
			
			params.add(new BasicNameValuePair("nombre", nombre));
			
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultado;
	}	
	
	public String getDatos() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/Locals/getDatos");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(3);
			
			params.add(new BasicNameValuePair("id", local.getId()+""));
			params.add(new BasicNameValuePair("categoria_local", local.getCategoria()+""));
			params.add(new BasicNameValuePair("comuna", local.getComuna()+""));
			
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultado;
	}	
	
	public String enviarVoto(String tipo) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(
				"http://"+getString(R.string.IP)+"/Hop/VotosLocals/addAndroid");
		HttpResponse response = null;
		String resultado = null;

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>(3);
			
			params.add(new BasicNameValuePair("local_id", ""+local.getId()));
			params.add(new BasicNameValuePair("user_id", ""+usuario.getId()));
			params.add(new BasicNameValuePair("tipo", tipo));
			
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost, localContext);
			HttpEntity entity = response.getEntity();
			resultado = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultado;
	}	
	
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        Log.e("src",src);
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        Log.e("Bitmap","returned");
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("Exception",e.getMessage());
	        return null;
	    }
	}

}
