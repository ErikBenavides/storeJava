
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;



public class Window extends JFrame implements ActionListener{

	private int index;
	private ArrayList<Product> products;
	private ArrayList<Product> cart;
	private ArrayList<Integer> productIndex;
	private JLabel image;
	private DataOutputStream out;
	private DataInputStream in;
	private Socket clientSocket;
	private String ip;
	private int port;	
	private JLabel lb1;
	private JLabel name;
	private JLabel description;
	private JLabel stock;
	private JLabel price;
	private JPanel controls;
	private JPanel container;

	public Window(){

	}

	public Window(ArrayList<Product> products, String ip, int port){
		super();
		this.index = 0;
		this.products = new ArrayList<>();
		this.cart = new ArrayList<>();
		this.productIndex = new ArrayList<>();
		this.products = products;
		this.ip = ip;
		this.port = port;
		this.lb1 = new JLabel();
		this.name = new JLabel();
		this.description = new JLabel();
		this.stock = new JLabel();
		this.price = new JLabel();
		this.controls = new JPanel();
		this.container = new JPanel();

		this.windowConfig();

		this.components();
		//JLabel image = new JLabel(new ImageIcon("./client/1589221304679-iphone11.jpg"));
		//this.add(image);


		//this.pack();
		this.setVisible(true);
	}

	private void windowConfig(){
		this.setTitle("Store");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		//this.setLayout(null);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setProducts(ArrayList<Product> products){
		this.products = products;
		System.out.println("Products");
		for(Product product : products){
			System.out.println(product.getName());
		}
		System.out.println("Index " + String.valueOf(index));
		System.out.println(products.get(index).getName());
	}

	private void components(){
		//JButton image = new JButton(new ImageIcon("./client/1589221304679-iphone11.jpg"));
		System.out.println(products.get(index).getImage());
		image = new JLabel(new ImageIcon("./client/" + products.get(index).getImage()));

		JButton exit = new JButton("Salir");
		JButton next = new JButton(">");
		JButton previous = new JButton("<");
		JButton buy = new JButton("Comprar");
		JButton cartBtn = new JButton("Carrito");

		//JPanel container = new JPanel();
		container.setLayout(new FlowLayout());
		container.add(createCard());
		container.add(buy);
		container.add(cartBtn);
		container.add(exit);

		//JPanel controls = new JPanel();
		controls.setLayout(new BorderLayout());
		controls.add(previous, BorderLayout.WEST);
		controls.add(image, BorderLayout.CENTER);
		controls.add(next, BorderLayout.EAST);

		this.add(controls, BorderLayout.CENTER);
		this.add(container, BorderLayout.SOUTH);

		next.addActionListener(this);
		previous.addActionListener(this);
		cartBtn.addActionListener(this);
		exit.addActionListener(this);
		buy.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
    {
		String button = e.getActionCommand();
		if(button.equals(">")){
			if (index < products.size() - 1){
				index++;
				updateValues();
			}
		} else if(button.equals("<")){
			if(index > 0)
				index--;
				updateValues();
		} else if(button.equals("Comprar")){
			//Comprobar existencias
			int actualStock = Integer.parseInt( validateStock());
			System.out.println(actualStock);
			if (actualStock == 0) {
				JOptionPane.showMessageDialog(this, "No se ha podido agregar al carrito\nPorque no hay stock",
                        "Estatus",
                        JOptionPane.INFORMATION_MESSAGE);
			} else {
				cart.add(products.get(index));
				productIndex.add(index);
				JOptionPane.showMessageDialog(this, "Agregado al carrito correctamente",
                        "Estatus",
                        JOptionPane.INFORMATION_MESSAGE);
			}			
		} else if(button.equals("Carrito")){
			CartWindow cartWindow = new CartWindow(cart, productIndex, ip, port);
		} else if(button.equals("Salir")) {
			System.exit(0);
		}

	}
	
	private String validateStock() {
		try {			
			
	        clientSocket = new Socket(ip, port);			
			//Escribir datos del tipo primitivo de una forma portable
			out = new DataOutputStream(clientSocket.getOutputStream());
			//Leer datos del tipo primitivo de una forma portable.
			in = new DataInputStream(clientSocket.getInputStream());
			
			
			out.writeUTF("validateStock");
			out.flush();
			out.writeUTF(String.valueOf(index));
			out.flush();
			return in.readUTF();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void updateValues(){
		lb1.setText("Producto: " + String.valueOf(index));
		name.setText("Nombre: " + products.get(index).getName());
		description.setText("Descripcion: " + products.get(index).getDescription());
		stock.setText("Stock: " + String.valueOf(products.get(index).getStock()));
		price.setText("Price: " + String.valueOf(products.get(index).getPrice()));

		image.setIcon(new ImageIcon("./client/" + products.get(index).getImage()));
	}


	private JPanel createCard(){
		System.out.println(products.get(index).toString());
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));


		lb1.setText("Producto: " + String.valueOf(index));
		name.setText("Nombre: " + products.get(index).getName());
		description.setText("Descripcion: " + products.get(index).getDescription());
		stock.setText("Stock: " + String.valueOf(products.get(index).getStock()));
		price.setText("Price: " + String.valueOf(products.get(index).getPrice()));


		card.add(name);
		card.add(description);
		card.add(stock);
		card.add(price);

		card.add(lb1);

		return card;
	}

	/* public static void main(String[] args) {
		Window window = new Window();
	} */
}
