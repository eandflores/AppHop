package BO;

import java.io.Serializable;

public class Solicitud implements Serializable {

	int id;
	String estado;
	String sql;
	String accion;
	String tabla;
	String campos;
	int user_id;
	String admin_id;  //id
	String local_id;  //id
	String created;
	String modified;

	public Solicitud(int id, String estado, String sql,String accion,String tabla, String campos, 
			int user_id,String admin_id,String local_id,String created,String modified) {
		
		this.id = id;
		this.estado = estado;
		this.sql = sql;
		this.accion = accion;
		this.tabla = tabla;
		this.campos = campos;
		this.user_id = user_id;
		this.admin_id = admin_id;
		this.local_id = local_id;
		this.created = created;
		this.modified = modified;
	}
	
	@Override
	public String toString() {
		return this.accion + " " + this.tabla;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getAccion() {
		return this.accion;
	}
	
	public String getTabla() {
		return this.tabla;
	}
	
	public int getUser() {
		return this.user_id;
	}
	
	public String getLocal() {
		return this.local_id;
	}
	
	public String getCampos() {
		return this.campos;
	}
}