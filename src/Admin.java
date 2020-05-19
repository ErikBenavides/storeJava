import java.net.*;
import java.util.Scanner;

import javax.swing.JFileChooser;

import java.io.*;
public class Admin {
	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private String ip;
	private int port;

	public Admin(String ip, int port){
		this.ip = ip;
		this.port = port;
	}

    private void startConnection() {
		try{
        	clientSocket = new Socket(ip, port);

			//System.out.println(this.sendMessage("hi server"));
			//this.menu();
			//in.close();
        	//out.close();
        	//clientSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void menu(){
		int res = 0;
		do{
			System.out.println("------  Administrador de Store -----");
			System.out.println("Selecciona una opcion");
			System.out.println("1) Ver productos");
			System.out.println("2) Crear producto");
			System.out.println("3) Eliminar producto");
			System.out.println("4) Editar producto");
			System.out.println("6) Salir");

			BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
			try{
				res = Integer.parseInt(consoleReader.readLine());

				//Comprobar opcion
				if(res == 1){

				} else if(res == 2){
					this.createProduct();
				} else if(res == 3){

				} else if(res == 4){

				} else if(res == 5){

				} else if(res == 6){
					System.out.println("Cerrando ...");
				} else {
					System.out.println("Introduce un numero valido");
				}

			} catch(Exception e){
				e.printStackTrace();
			}
		}while(res != 6);
	}

	private void createProduct(){
		try{
			Product product = new Product();
			Scanner in = new Scanner(System.in);

			//Capturar datos del producto
			System.out.println("------- Crear producto ------");

			System.out.println("Introduce el nombre");
			product.setName(in.nextLine());

			System.out.println("Introduce la descripcion");
			product.setDescription(in.nextLine());

			System.out.println("Introduce el stock");
			product.setStock(in.nextInt());

			System.out.println("Introduce el precio");
			product.setPrice(in.nextFloat());

			//seleccionar imagen
			JFileChooser jf= new JFileChooser();
			int r = jf.showOpenDialog(null);

			if (r==JFileChooser.APPROVE_OPTION){
				//Imagen
				File file = jf.getSelectedFile();
				String fileName = file.getName();
				String filePath = file.getAbsolutePath();

				//Datos del objeto
				String dataPath = "./data.txt";
				File data = new File(dataPath);

				//String dataPath = data.getAbsolutePath();
				//long dataSize= data.length();
				product.setImage(System.currentTimeMillis() + "-" + fileName.replaceAll("\\s", ""));

				System.out.println(product.toString());

				//Serializacion

				FileOutputStream fileOS = new FileOutputStream(dataPath);
				ObjectOutputStream out = new ObjectOutputStream(fileOS);

				out.writeObject(product);
				out.close();
				fileOS.close();
				//Manda el archivo con el objeto serializado
				this.sendFile(data, dataPath, "createData", dataPath);

				//Manda la imagen
				this.sendFile(file, filePath, "createImage", product.getImage());

			} else {
				System.out.println("Debes seleccionar un archivo");
			}

			//Deserializar
			/*
			FileInputStream fileIS = new FileInputStream(fileName);
			ObjectInputStream inObject = new ObjectInputStream(fileIS);

			Product product2 = (Product) inObject.readObject();
			System.out.printf(product2.toString());

			inObject.close();
			*/

			//System.out.println("Introduce la imagen");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void sendFile(File file, String filePath, String action, String name){
		try{
			this.startConnection();
			long fileSize = file.length();


			//Escribir datos del tipo primitivo de una forma portable
			out = new DataOutputStream(clientSocket.getOutputStream());
			//Leer datos del tipo primitivo de una forma portable.
			DataInputStream dis= new DataInputStream(new FileInputStream(filePath));
			//Escribir datos con codificación UTF por defecto es utf-8
			out.writeUTF(action);
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
				System.out.print("\nEnviado: "+porcentaje+"%\r");
			}//While
			System.out.print("\n\nArchivoenviado\n");
			out.close();
			dis.close();
			//this.stopConnection();
			clientSocket.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
        Admin admin = new Admin("localhost", 3000);
		admin.menu();
    }


}