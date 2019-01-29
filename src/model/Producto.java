package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the producto database table.
 * 
 */
@Entity  //Anotacion que indica q se trata de 1 clase de entidad
//Anotación que define una consulta con nombre
@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id    //anotacion del atributo codigo que es clave princpal
	private String codigo;

	private String descripcion;

	private float minimo;

	private float precio;

	private float stock;

	//bi-directional many-to-one association to Detalle
	@OneToMany(mappedBy="producto")//se asocia de uno a muchos entre tabla producto y tabla detalle
	private List<Detalle> detalles;//Atributo detalles es coleccion de elementos de tipo Detalle

	public Producto() {
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public float getMinimo() {
		return this.minimo;
	}

	public void setMinimo(float minimo) {
		this.minimo = minimo;
	}

	public float getPrecio() {
		return this.precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public float getStock() {
		return this.stock;
	}

	public void setStock(float stock) {
		this.stock = stock;
	}

	public List<Detalle> getDetalles() {
		return this.detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}
    //Metodo que permite añadir un nuevo objeto Detalle
	public Detalle addDetalle(Detalle detalle) { 
		getDetalles().add(detalle);
		detalle.setProducto(this);

		return detalle;
	}
	//Metodo que permite eliminar una venta (Detalle)
	public Detalle removeDetalle(Detalle detalle) {
		getDetalles().remove(detalle);
		detalle.setProducto(null);

		return detalle;
	}

}