package principal;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import model.Cliente;
import model.Detalle;
import model.Factura;
import model.Producto;
/**
 * Para JPA se necesita un objeto de la clase EntityManager, quien administra la BBDD 
 * @author bea_soria
 *
 */
public class Principal {
	private static Scanner lector;
	private static EntityManagerFactory factoria;
	private static EntityManager em;

	public static void main(String[] args) {
		factoria = Persistence.createEntityManagerFactory("ActividadUF3");
		em = factoria.createEntityManager();
		lector = new Scanner(System.in);
		String opcion;
		do {
			System.out.println();//Imprimimos por pantalla las opciones
			System.out.println();
			System.out.println("CONSULTAS FERRETERÍA");
			System.out.println("-----------------------------");
			System.out.println("1. Listado de cientes");
			System.out.println("2. Consultar facturas");
			System.out.println("3. Suma de los importes de todas las ventas");
			System.out.println("4. Descripcion de cada articulo junto al numero total de unidades vendidas");
			System.out.println("5. Terminar programa");

			System.out.println("¿Qué opción eliges?");
			opcion = lector.nextLine();
			switch (opcion) {  //Se crea switch para las diferentes opciones
			case "1":
				listadoClientes();
				break;
			case "2":
				consultarFactura();
				break;
			case "3":
				importeVentas();
				break;
			case "4":
				ventas();
				break;
			case "5":
				System.out.println("Hasta pronto");
			}
		} while (!opcion.equals("5"));
		lector.close();
	}
	
    /**
     * Metodo que consulta la tabla clientes e imprime por pantalla sus datos
     */
	private static void listadoClientes() {
		TypedQuery<Cliente> query = em.createNamedQuery("Cliente.findAll", Cliente.class);
		List<Cliente> cliente = query.getResultList();
		for (Cliente c : cliente) {
			System.out.println(c.getNif() + " - " + c.getNombre() + " - " + c.getTlf());
		}

	}

/**
 * Metodo que pide el Num de Factura y muestra sus detalles
 */
	private static void consultarFactura() {
		System.out.println("Imtroduce numero de factura: ");
		Integer numero = lector.nextInt();
		lector.nextLine();
		Factura factura = em.find(Factura.class, numero);
		if (factura == null) {
			System.out.println("No se ha encontrado la factura = " + numero);
		} else {
			System.out.println("Fecha: " + factura.getFecha());
			System.out.println("Pagado: " + factura.getPagado());
			System.out.println("Cliente: " + factura.getCliente().getNif() + " - " + factura.getCliente().getNombre());
			System.out.println();
			float total = 0;
			for (Detalle d : factura.getDetalles()) {
				total += d.getPrecio() * d.getUnidades();
				System.out.println(" " + d.getProducto().getDescripcion() + ", " + d.getUnidades() + " unidades a "
						+ d.getPrecio() + " €");
			}
			System.out.println();
			System.out.println(" IMPORTE TOTAL: " + total);

		}

	}

	private static void importeVentas() {
		TypedQuery<Detalle> query = em.createNamedQuery("Detalle.findAll", Detalle.class);
		List<Detalle> detalle = query.getResultList();
		float total = 0;
		for (Detalle d : detalle) {
			total += d.getPrecio() * d.getUnidades();
		}
		System.out.println("Importe total de las ventas: " + total);

	}

	private static void ventas() {
		TypedQuery<Detalle> query = em.createNamedQuery("Detalle.findAll", Detalle.class);
		List<Detalle> detalles = query.getResultList();

		HashMap<Producto, Integer> mapa = new HashMap<>();

		for (Detalle detalle : detalles) {
			Producto p = detalle.getProducto();
			int cantidad = detalle.getUnidades();
			// Veo si ya existe el producto en el mapa
			if (mapa.containsKey(p)) {
				// Como ya existe, tengo que actualizar la cantidad
				int cantidad_previa = mapa.get(p);
				int cantidad_actualizada = cantidad_previa + cantidad;
				mapa.replace(p, cantidad_actualizada);
			} else {
				mapa.put(p, cantidad);
			}
		}
		recorrerMapa(mapa);

	}

	private static void recorrerMapa(HashMap<Producto, Integer> mapa) {
		// Recorro el mapa mostrando los datos
		for (Map.Entry<Producto, Integer> entrada : mapa.entrySet()) {
			System.out.println(entrada.getKey().getDescripcion() + " - " + entrada.getValue() + " unidades vendidas");
		}
	}

}