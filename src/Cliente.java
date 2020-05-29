import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
public class Cliente {
	private ArrayList<Product> products;
	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private String ip;
	private int port;
	private String path;

	public Cliente(String ip, int port){
		this.path = "./";
		this.ip = ip;
		this.port = port;
	}

    private void startConnection() {
		try{
        	clientSocket = new Socket(ip, port);

		}catch(Exception e){
			e.printStackTrace();
		}

	}
    
    public Socket getClientSocket() {
    	return clientSocket;
    }

	public ArrayList<Product> getProducts(){
		return this.products;
	}

	public void stopConnection() {
		try{
			in.close();
			out.close();
			clientSocket.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadProducts(){
		try{
            FileInputStream fis = new FileInputStream("./products.data");
            ObjectInputStream ois = new ObjectInputStream(fis);

			products = (ArrayList<Product>) ois.readObject();

            ois.close();
			fis.close();

			System.out.println("\n\nDeserializado archivo");
			for (Product product : products) {
				System.out.println(product);
			}
        } catch(Exception e){
			e.printStackTrace();
		}

	}

	public void downloadImages(){
		//descargar cada una de las imagenes
		for(Product product : products){
			getProductsImage(product.getImage());
		}
	}
	public void getProductsImage(String name){
		try{
			startConnection();
			out = new DataOutputStream(clientSocket.getOutputStream());
			//Leer datos del tipo primitivo de una forma portable.
			in = new DataInputStream(clientSocket.getInputStream());
			out.writeUTF("getProductsImage");
			//solicita la imagen
			out.writeUTF(name);
			
			//recibe la imagen 
			reciveFile("./client/", name);
		}catch(Exception e){
			e.printStackTrace();
		}


	}

	public void getProductsData(){
		try{
			startConnection();
			//Escribir datos del tipo primitivo de una forma portable
			out = new DataOutputStream(clientSocket.getOutputStream());
			//Leer datos del tipo primitivo de una forma portable.
			in = new DataInputStream(clientSocket.getInputStream());

			out.writeUTF("getProductsData");
			String name = in.readUTF();

			//Recibe el archivo con los datos de los productos
			reciveFile(path, name);
			//Deserializa y lo guarda en products
			loadProducts();
			out.close();
			in.close();

		} catch(Exception e){
			e.printStackTrace();
		}


	}

	private void reciveFile(String path, String name){
		try{
			//startConnection();
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
			}
			out.flush();

			System.out.print("\n\nArchivo recibido\n");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Escribe la direccion");
		String ip = in.nextLine();
		System.out.println("Escribe el puerto");
		int port = in.nextInt();
		in.close();
		System.out.println(ip + ":" + port);

		//String ip = "localhost";
		//int port = 3000;
		Cliente cliente = new Cliente(ip, port);
		cliente.getProductsData();
		cliente.downloadImages();


		//System.out.println(cliente.getProducts().get(0).getName());
		Window window = new Window(cliente.getProducts(), ip, port);
		//window.setProducts();


    }


}