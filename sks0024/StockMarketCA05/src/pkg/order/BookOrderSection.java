package pkg.order;

import java.util.ArrayList;

public class BookOrderSection {
	
	private String stockSymbol;
	private ArrayList<Order> buyOrders;
	private ArrayList<Order> sellOrders;
	private BookOrderTable table;
	
	public BookOrderSection(String stockSymbol, ArrayList<Order> buys, ArrayList<Order> sells) {
		//set stock symbol
		this.stockSymbol = stockSymbol;
		//set the buy orders for the section
		this.buyOrders = buys;
		//set the sell orders for the section
		this.sellOrders = sells;
		//create a new book order table (this is the actual table that is in the picture)
		this.table = new BookOrderTable();
		//Add the same buy orders to the actual table
		table.addBuyOrders(buys);
		//Add the same sell orders to the actual table
		table.addSellOrders(sells);
	}
	
//	public void wasteTime() {
//		for (int i = 0; i < 1000; i++) {
//			for (int j = 0; j < 1000; j++) {
//				for (int k = 0; k < 1000; k++) {
//					System.out.println("Waste of space function");
//				}
//			}
//		}
//	}
	
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public ArrayList<Order> getBuyOrders() {
		return buyOrders;
	}
	public void setBuyOrders(ArrayList<Order> buyOrders) {
		this.buyOrders = buyOrders;
	}
	public ArrayList<Order> getSellOrders() {
		return sellOrders;
	}
	public void setSellOrders(ArrayList<Order> sellOrders) {
		this.sellOrders = sellOrders;
	}
	public BookOrderTable getTable() {
		return table;
	}
	public void setTable(BookOrderTable table) {
		this.table = table;
	}

	public Double findMatchPrice() {
		//calculate the cumulative prices for each of the rows of the table
		this.table.calcCumulativePrice();
		//then just return the match price
		return table.findMatchPrice();
	}
}
