# Actividad 3 - Servicios Rest


## Video pruebas

[https://www.youtube.com/watch?v=g1UXsCYCMkA](https://www.youtube.com/watch?v=g1UXsCYCMkA)

## Repositorio

[GitHub - guiller91/Servidor_Rest: Servidor con springboot para la actividad 3 de programación de procesos y servicios](https://github.com/guiller91/Servidor_Rest)

## Objetivos

- [x]  Servicio Rest que controle una serie de videojuegos.
- [x]  Cliente que trabaje con este servicio.
- [x]  El servicio tendrá que realizar las siguientes tareas
    - [x]  Dar de alta un videojuego
    - [x]  Dar de baja un videojuego por id
    - [x]  Modificar un videojuego por id
    - [x]  Obtener un videojuego por id
    - [x]  Listar todos los videojuegos
- [x]  No podrá haber dos videojuegos con nombre o id repetidos
- [x]  El cliente tendrá que mostrar el menú apropiado para trabajar con el servicio.

## Explicación de puntos clave

### Servidor

---

Para cumplir con la premisa de que ningún videojuego creado contenga el mismo nombre o id, se realiza el siguiente cribado.

```java
public String addJuego(Videojuego v) {

		for (Videojuego p : listaJuegos) {
			if (p.getNombre().equalsIgnoreCase(v.getNombre())) {
				return null;
			}
		}
		v.setId(id++);
		listaJuegos.add(v);
		return "Añadido";
	}
```

Mediante el bucle comprobamos el listado de juegos y en el momento que coincida el nombre, retornamos un `null` . Si no encuentra el nombre, el programa seguirá y asignara un ID al videojuego que añadió el cliente y lo agregará. De este modo, nos aseguramos que el id sea único quitando la posibilidad al cliente de dar el valor del ID.

En el siguiente método recorremos el listado en busca del ID que nos proporciona el cliente y de encontrarlo le devolvemos el objeto. Si no lo encuentra, le devolvemos un `null` .

```java
public Videojuego get(int id) {
		for(Videojuego p:  listaJuegos) {
			if(p.getId()==id) {
				return p;
			}
		}return null;
	}
```

Para borrar el objeto sobre la ID del videojuego que nos de el cliente, tendremos que sobrescribir los métodos `hashCode()` y `equals(Object obj)` en la clase `Videojuego.class` . De este modo podremos recorrer el `ListadoJuegos`  y hacer una búsqueda `indexOf()` pasándole como atributo el `Videojuego` que queramos borrar. Nos dará la posición en la que esta, si es que existe, y se la pasaremos a `listaJuegos.remove()` .

```java
public Videojuego delete(int id) {
		try {
			Videojuego j = get(id);
			int n = listaJuegos.indexOf(j); 
			return listaJuegos.remove(n);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("delete -> Videojuego fuera de rango");
			return null;
		}
	}
```

Para modificar un `Videojuego`, vamos a pedir en la parte de controlador que nos indiquen la `ID` del objeto a modificar y el cuerpo del mensaje será el resto de atributos, ósea, `nombre, compañía y nota`. Con todos esos parámetros creamos un Objeto `Videojuego` que pasaremos al siguiente método. Y mediante el método `get()` accederemos al objeto a modificar y le pasamos los nuevos atributos.

```java
public Videojuego update(Videojuego v) {
		try {
			Videojuego vAux = get(v.getId());
			vAux.setNombre(v.getNombre());
			vAux.setCompañia(v.getCompañia());
			vAux.setNota(v.getNota());
			
			return vAux;
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("update -> Persona fuera de rango");
			return null;
		}
	}
```

Para listar todos los videojuegos solo tendremos que crear el siguiente metodo.

```java
public List<Videojuego> list() {
		return listaJuegos;
	}
```

En la parte del `ControladorVideojuego.class`, sigue un esquema muy parecido. Para configurar que estamos recibiendo, si estamos recibiendo un verbo GET o un POST por ejemplo, tendremos que seguir el siguiente patrón, modificándolo para cada verbo.

Como por ejemplo, el verbo POST.

```java
@PostMapping(path="videojuegos",consumes=MediaType.APPLICATION_JSON_VALUE,
														    produces=MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Videojuego> altaPersona(@RequestBody Videojuego v) {
		System.out.println("Alta videojuego: objeto videojuego: " + v);
		String prueba=daoVideojuego.addJuego(v);
		System.out.println(prueba);
		if(prueba != null) {
			return new ResponseEntity<Videojuego>(v,HttpStatus.CREATED);//201 created
		}else {
			
			return new ResponseEntity<Videojuego>(HttpStatus.CONFLICT);//409 conflict
		}
	}
```

Con la anotación `@PostMapping` , le estamos diciendo que actuara sobre el verbo POST. Con el `path` , le indicamos la ruta o URL que escucha esta petición. En este caso seria algo parecido a lo siguiente : [`http://localhost:8080/videojuegos/`](http://localhost:8080/videojuegos/) . Con `consumes`  le indicamos en que formato va a leer los datos que recibe y con `produces` el formato de dato que va a generar el método.

El método le estamos diciendo que es obligatorio que nos pasen un objeto `Videojuego` con `@RequestBody`.

Con este formato tendremos que hacerlo para todas las posibles peticiones que requiere el servidor, dar de Baja a un `Videojuego`, buscarlo por ID, etc. Cada uno con su anotación oportuna.

### Cliente

---

En la parte de cliente, tendremos que adaptar la aplicación para que mande la información como lo hemos configurado el servidor.

Por ejemplo, en `ServicioProxyJuego.class` es donde vamos a configurar el verbo que le mandamos al servidor y su estructura. Vamos a ver el código de a`lta(Videojuego v)`.

```java
public Videojuego alta(Videojuego v){
		try {		
			ResponseEntity<Videojuego> re = restTemplate.postForEntity(URL, v, Videojuego.class);
			System.out.println("alta -> Codigo de respuesta " + re.getStatusCode());
			return re.getBody();
		} catch (HttpClientErrorException e) {
			System.out.println("alta -> El videojuego no se ha dado de alta: " + v);
		    System.out.println("alta -> Codigo de respuesta: " + e.getStatusCode());
		    return null;
		}
	}
```

Con `restTemplate.postForEntity(URL, v, Videojuego.class)` , le estamos diciendo que va a ser el verbo POST y le estamos pasando la URL donde el servidor tiene configurado esta acción y el Videojuego que queremos dar de alta.

Retornamos el cuerpo del mensaje que nos envía el servidor (q que en este caso será un objeto) y con `re.getStatusCode()` que en caso de que sea correcto será un código 201 CREATED y en caso de que no se haya dado de alta será un 409 CONFLICT.

Al igual que en el servidor, cada acción que deseamos llevara su método con su verbo. A los que daremos contenido con el siguiente código, que tenemos alojado en la clase `ClienteRestApplication.class` y que está configurado que lo haga todo en el menú. 

```java
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
```
