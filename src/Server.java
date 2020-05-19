import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Server implements Serializable{

	private static final long serialVersionUID = 1L;
	private ArrayList<Product> products;
	private ServerSocket serverSocket;
	private Socket client;
	private DataInputStream in;
	private DataOutputStream out;
	private String path;

	private DataInputStream dis;

	public Server(int port){
		try{
			//Crear servidor en el puerto 3000
			path = "./server/";
			serverSocket = new ServerSocket(3000);
			products = new ArrayList<Product>();
			defaultProducts();
			saveProducts(products);
			loadProducts();

		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void listen(){
		try{
			for(;;){
				//Aceptar de petición de conexión
				System.out.println("\nEsperando conexion");
				client = serverSocket.accept();
				System.out.println("Conexion establecida desde "+client.getInetAddress()+":"+client.getPort());

				//Configuración para la transmisión de datos
				//Leer datos del tipo primitivo de una forma portable.
				in= new DataInputStream(client.getInputStream());
				out = new DataOutputStream(client.getOutputStream());

				//Leer datos con codificación UTF por defecto es utf-8
				String action = in.readUTF();
				String name = "";

				if(action.equals("createData")){
					//Recibe datos - Objeto
					name = in.readUTF();
					this.reciveFile(path, name);

					//Deserializa y lo agrega al arreglo
					products.add( deserializeProduct(path, name) );
				} else if(action.equals("createImage")){
					//Recibe imagen
					name = in.readUTF();
					this.reciveFile(path, name);
				} else if(action.equals("getProductsData")){
					//Enviar datos de los productos
					this.getProducts();

				} else if(action.equals("getProductsImage")){
					name = in.readUTF();
					System.out.println(name);
					sendImage(name);
				}

				saveProducts(products);
			}
		}catch(Exception e){
			e.printStackTrace();
		}//catch
	}

	private void sendImage(String name){
		try{

			//Regresa las imagenes de los productos al cliente

			File file = new File(path + name);

			dis = new DataInputStream(new FileInputStream(path + name));
			long fileSize = file.length();
			System.out.println(name);
			out.flush();
			out.writeLong(fileSize);
			out.flush();
			byte[] b = new byte[1024];
			long enviados = 0;
			int porcentaje, n;
			//Código para mostrar mensaje mientras se transmienten los datos
			while(enviados < fileSize){
				n = dis.read(b);
				out.write(b,0,n);
				out.flush();
				enviados = enviados+n;
				porcentaje = (int)(enviados*100/fileSize);
				System.out.print("Enviado: "+porcentaje+"%\r");
			}//While
			System.out.print("\n\nArchivo enviado\n");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private void getProducts(){
		try{

			//Regresa los productos al cliente
			//Datos del objeto
			saveProducts(products);
			String name = "products.data";
			File data = new File(path + name);

			sendFile(data, path, name);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private void sendFile(File file, String path, String name){
		try{
			//this.startConnection();

			//Escribir datos del tipo primitivo de una forma portable
			//DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
			//Leer datos del tipo primitivo de una forma portable.
			dis = new DataInputStream(new FileInputStream(path + name));
			long fileSize = file.length();
			System.out.println(name);
			//Escribir datos con codificación UTF por defecto es utf-8
			out.flush();
			out.writeUTF(name);
			out.flush();
			out.writeLong(fileSize);
			out.flush();
			byte[] b = new byte[1024];
			long enviados = 0;
			int porcentaje, n;
			//Código para mostrar mensaje mientras se transmienten los datos
			while(enviados < fileSize){
				n = dis.read(b);
				out.write(b,0,n);
				out.flush();
				enviados = enviados+n;
				porcentaje = (int)(enviados*100/fileSize);
				System.out.print("Enviado: "+porcentaje+"%\r");
			}//While
			System.out.print("\n\nArchivo enviado\n");

		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadProducts(){
		try{
            FileInputStream fis = new FileInputStream("./server/products.data");
            ObjectInputStream ois = new ObjectInputStream(fis);

			products = (ArrayList<Product>) ois.readObject();

            ois.close();
			fis.close();

        } catch(Exception e){
			e.printStackTrace();
		}

	}

	private void defaultProducts(){
		Product product = new Product();
			product.setName("Xbox series X");
			product.setDescription("Consola de videojuegos");
			product.setStock(5);
			product.setPrice(7000);
			product.setImage("1589221304679-xbox.jpg");

			products.add(product);

			Product product2 = new Product();
			product2.setName("Iphone 11");
			product2.setDescription("Smarthphone de apple");
			product2.setStock(10);
			product2.setPrice(17500);
			product2.setImage("1589221304679-iphone11.jpg");

			products.add(product2);

			Product product3 = new Product();
			product3.setName("Xiaomi redminote 9");
			product3.setDescription("Smartphone de xiaomi");
			product3.setStock(16);
			product3.setPrice(5400);
			product3.setImage("1589221304679-xiaomi.jpg");

			products.add(product3);
	}

	private void saveProducts(Object object){
		//almacena en un archivo serializado
		try{
            FileOutputStream fos = new FileOutputStream("./server/products.data");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	private Product deserializeProduct(String path, String name){
		// Reading the object from a file
		try{
			FileInputStream fileIS = new FileInputStream(path + name);
			ObjectInputStream in = new ObjectInputStream(fileIS);

			System.out.println("\n\nObjeto deserializado\n");
			Product product = (Product) in.readObject();
			System.out.printf(product.toString());
			in.close();
			return product;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;

	}

	private void reciveFile(String path, String name){
		try{

			long tam= in.readLong();
			byte[] b = new byte[1024];
			//Escribir datos del tipo primitivo de una forma portable
			out = new DataOutputStream(new FileOutputStream(path + name));
			long recibidos=0;
			int n, porcentaje;

			//Código para mostrar mensaje mientras se transmienten los datos
			while(recibidos < tam){
				n = in.read(b);
				out.write(b,0,n);
				out.flush();
				recibidos = recibidos + n;
				porcentaje = (int)(recibidos*100/tam);
				System.out.print("\nRecibiendo archivo: " + porcentaje);
			}//While
			out.close();
			in.close();
			client.close();
			System.out.print("\n\nArchivo recibido\n");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		/* Scanner in = new Scanner(System.in);
		System.out.println("Escribe el puerto");
		int port = in.nextInt();
		in.close();
		System.out.println(port); */
		Server server = new Server(3000);
		server.listen();



	}
}