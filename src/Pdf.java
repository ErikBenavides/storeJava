import java.io.FileOutputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

import javax.swing.JTextField;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Pdf {
	private Document document;
	private Font font;
	private Chunk chunk;
	private ArrayList<Product> cart;
	private ArrayList<JTextField> fields;
	
	
	public Pdf() {}
	
	public Pdf(ArrayList<Product> cart, ArrayList<JTextField> fields, int total) {
		try {
			//cart = new ArrayList<Product>();
			//fields = new ArrayList<JTextField>();
			//this.cart = cart;
			//this.fields = fields;
			document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream("Recibo.pdf"));			 
			document.open();
			
			addChunk("Store - Recibo", 24);
			addPara("Store.inc", 15);
			addPara("México, México, Ecatepec de Morelos, Vallarta #12", 12);
			addPara("Nº de recibo:", 15);
			addChunk("FO" + System.currentTimeMillis(), 18);
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date date = new Date(); 
			
			addPara("Fecha y hora: " + formatter.format(date), 15);
			addPara(" ", 12);
			addPara(" ", 12);
			
			
			PdfPTable table = new PdfPTable(4);
			addTableHeader(table);
			
			
			//addRows(table);
			float total1 = 0.0f;			
			
			//Agregar datos del carro a la tabla
			for(int i = 0; i < cart.size(); i++) {
				total1 = cart.get(i).getPrice() * Integer.parseInt(fields.get(i).getText());
				addRows(table, fields.get(i).getText(), cart.get(i).getName() , String.valueOf(cart.get(i).getPrice()), String.valueOf(total1) );
			}
			addRows(table, " ", " ", "Subtotal", String.valueOf(total * 0.84));
			addRows(table, " ", " ", "IVA 16.0%", String.valueOf(total * 0.16));
			addRows(table, " ", " ", "Total", String.valueOf(total));
			
			
			document.add(table);
			
			document.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void addTableHeader(PdfPTable table) {
	    Stream.of("Cantidad", "Nombre", "Precio", "Importe")
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(2);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}	
	
	private void addRows(PdfPTable table, String amount, String name, String price, String total) {
	    table.addCell(amount);
	    table.addCell(name);
	    table.addCell(price);
	    table.addCell(total);
	}
	
	private void addChunk(String text, int size) {
		try {			
			font = FontFactory.getFont(FontFactory.COURIER, size, BaseColor.BLACK);
			chunk = new Chunk(text, font);		 
			document.add(chunk);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addPara(String text, int size) {
		try {			
			font = FontFactory.getFont(FontFactory.COURIER, size, BaseColor.BLACK);			
			document.add(new Paragraph(text, font));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//public static void main(String[] args) {
		// TODO Auto-generated method stub
	//	Pdf pdf = new Pdf();
	//}

}
