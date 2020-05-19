
import javax.swing.*;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartWindow extends JFrame implements ActionListener{
	private ArrayList<Product> cart;
	private int deleteIndex;
	private ArrayList<JTextField> fields;
	private int total;

	public CartWindow(){}

	public CartWindow(ArrayList<Product> cart){
		super();
		this.cart = new ArrayList<>();
		this.cart = cart;
		this.deleteIndex = 0;
		this.total = 0;
		this.fields = new ArrayList<JTextField>();
		windowConfig();
		this.components();
		this.setVisible(true);
	}



	private void windowConfig(){
		this.setTitle("Store - Carrito");
		this.setSize(400, 600);
		this.setLocationRelativeTo(null);
		//this.setLayout(null);
		//this.setLayout(new BorderLayout());
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void components() {
		for(Product product : cart){
			this.add(productCard(product));
		}
		JButton buy = new JButton("Finalizar compra");
		this.add(buy);
		buy.addActionListener(this);

	}

	private JPanel productCard(Product product){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		//JPanel close = new JPanel();
		JButton button = new JButton("Borrar" + deleteIndex++);
		//close.setLayout(new FlowLayout());
		//close.add(new JLabel("Nombre: " + product.getName()));
		//close.add(button);

		//panel.add(close);
		button.addActionListener(this);

		panel.add(button);
		panel.add(new JLabel("Nombre: " + product.getName()));
		panel.add(new JLabel("Precio: " + product.getPrice()));

		JPanel field = new JPanel();
		field.setLayout(new FlowLayout());
		field.add(new JLabel("Cantidad:"));
		JTextField textField = new JTextField("1", 10);
		fields.add(textField);
		field.add(textField);
		panel.add(new JLabel("Stock:" + product.getStock()));

		panel.add(field);

		return panel;
	}

	private void getTotal() {
		for(int i = 0; i < cart.size(); i++){
			total += cart.get(i).getPrice() * Integer.parseInt( fields.get(i).getText());
		}
		System.out.println(total);
		JOptionPane.showMessageDialog(this, "Gracias por comprar\nEl Recibo se ha generado con exito\nTotal: " + String.valueOf(total),
                                          "Compra finalizada",
                                          JOptionPane.INFORMATION_MESSAGE);
		
		Pdf pdf = new Pdf(cart, fields, total);
	}

	public void actionPerformed(ActionEvent e){
		String button = e.getActionCommand();

		if(button.equals("Finalizar compra")){
			getTotal();
		} else {


			int index = Integer.parseInt(button.substring(button.length() - 1));
			cart.remove(index);
			getContentPane().removeAll();

			deleteIndex = 0;
			total = 0;
			components();

			this.validate();
			this.repaint();
		}


	}
}