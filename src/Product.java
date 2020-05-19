public class Product implements java.io.Serializable{
	private String name;
	private String description;
	private int stock;
	private float price;
	private String image;

	public String toString(){
		return "Nombre: " + name + "\n" +
				"Descripcion: " + description + "\n" +
				"Stock: " + stock + "\n" +
				"Price: " + price + "\n" +
				"Image: " + image + "\n";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}