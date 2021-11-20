package clienterest;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import clienterest.entidad.Videojuego;
import clienterest.servicio.ServicioProxyJuego;

@SpringBootApplication
public class ClienteRestApplication implements CommandLineRunner{
		@Autowired
		private ServicioProxyJuego sp;
	
		@Autowired
		private ApplicationContext context;
		
		@Bean
		public RestTemplate restTemplate(RestTemplateBuilder builder) {
			return builder.build();
		}
	
	public static void main(String[] args) {
		SpringApplication.run(ClienteRestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try (Scanner sc = new Scanner(System.in)) {

			String texto = "";
			boolean continuar = true;
			int num;
			int nota = 0;
			Videojuego videojuego, vRespuesta;
			boolean respues;

			boolean interruptor = false;

			do {
				System.out.println("\nElige una número del menu:\n" + "1. Dar de alta un videojuego\n"
						+ "2. Dar de baja un videojuego por ID\n" + "3. Modificar un videojuego por ID\n"
						+ "4. Obtener un videojuego por ID\n" + "5. Listar todos los videojuegos\n"
						+ "6. Salir de la aplicación");
				num = Integer.parseInt(sc.nextLine());

				switch (num) {
				case 1:
					videojuego = new Videojuego();
					System.out.println("Introduzca el Nombre");
					texto = sc.nextLine();
					videojuego.setNombre(texto);
					System.out.println("Introduzca la Compañia");
					texto = sc.nextLine();
					videojuego.setCompañia(texto);
					System.out.println("Introduzca la nota (min:0  max:10)");
					do {
						try {
							texto = sc.nextLine();
							
							if (Integer.parseInt(texto) >= 0 & Integer.parseInt(texto) < 11) {
								nota = Integer.parseInt(texto);
								interruptor = true;
							} else {
								System.out.println("Introduce un numero correcto");
								texto = sc.nextLine();
							}
						}catch(NumberFormatException e){
							System.out.println("Introduce numero valido(min:0  max:10)");
						}
					} while (!interruptor);
					videojuego.setNota(nota);
					System.out.println("enviando datos");
					vRespuesta = sp.alta(videojuego);
					System.out.println(vRespuesta);
					break;
				case 2:
					System.out.println("Introduzca el ID");
					texto = sc.nextLine();
					respues=sp.borrar(Integer.parseInt(texto));
					System.out.println("Se ha borrado ? " + respues);
					break;
				case 3:
					videojuego=new Videojuego();
					System.out.println("Introduzca el id del videojuego a Modificar");
					videojuego.setId(Integer.parseInt(sc.nextLine()));
					System.out.println("Introduzca el nuevo Nombre");
					videojuego.setNombre(sc.nextLine());
					System.out.println("Introduzca la nueva compañia");
					videojuego.setCompañia(sc.nextLine());
					System.out.println("Introduzca la nueva nota");
					videojuego.setNota(Integer.parseInt(sc.nextLine()));
					respues= sp.modificar(videojuego);
					System.out.println("Se ha podido modificar? " + respues);
					break;
				case 4:					
					System.out.println("Introduzca el id del videojuego a buscar");
					texto= sc.nextLine();
					videojuego=sp.obtener(Integer.parseInt(texto));
					if(videojuego!=null)
						System.out.println(videojuego);
					
					break;
				case 5:
					List<Videojuego> vJuegos;
					vJuegos=sp.listar();
					for(Videojuego v : vJuegos)
						System.out.println(v.toString());

					break;
				case 6:
					System.out.println("Saliendo de la aplicación....");

					continuar = false;
					break;
				default:
					System.out.println("Introduzca un numero del menu");
				}

			} while (continuar);

		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}

		System.out.println("CLIENTE: Fin del programa");
		pararAplicacion();
	}

	public void pararAplicacion() {

		SpringApplication.exit(context, () -> 0);

	}

}
