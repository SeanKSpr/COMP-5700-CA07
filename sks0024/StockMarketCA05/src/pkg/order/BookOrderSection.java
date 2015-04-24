package pkg.order;

import java.util.ArrayList;

public class BookOrderSection {
	
	private String stockSymbol;
	private ArrayList<Order> buyOrders;
	private ArrayList<Order> sellOrders;
	private BookOrderTable table;
	
	public BookOrderSection(String stockSymbol, ArrayList<Order> buys, ArrayList<Order> sells) {
		this.stockSymbol = stockSymbol;
		this.buyOrders = buys;
		this.sellOrders = sells;
		this.table = new BookOrderTable();
		table.addBuyOrders(buys);
		table.addSellOrders(sells);
		
	}
	
	
	
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
		this.table.calcCumulativePrice();
		return table.findMatchPrice();
	}
}
