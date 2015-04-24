package pkg.order;

import java.util.ArrayList;

public class BookOrderTable {
	private ArrayList<BookOrderTuple> table;
	
	public BookOrderTable(){
		table = new ArrayList<BookOrderTuple>();
	}
	public ArrayList<BookOrderTuple> getTable() {
		return table;
	}

	public void setTable(ArrayList<BookOrderTuple> table) {
		this.table = table;
	}
	
	public void addBuyOrders(ArrayList<Order> buyOrders) {
		for (Order order : buyOrders) {
			this.addBuyOrder(order);
		}
	}
	
	public void addSellOrders(ArrayList<Order> sellOrders) {
		for (Order order : sellOrders) {
			this.addSellOrder(order);
		}
	}
	
	public void addBuyOrder(Order order) {
		int index = getMatchingIndex(order.getPrice());
		if (index >= 0) {
			//update existing buy order 
			table.get(index).addBuyOrderAmount(order.getSize());
		}
		else {
			//add new buy order
			BookOrderTuple tuple = new BookOrderTuple();
			tuple.setPrice(order.getPrice());
			tuple.setBuyOrderAmount(order.getSize());
			table.add(tuple);
		}
	}
	
	public void addSellOrder(Order order) {
		int index = getMatchingIndex(order.getPrice());
		if (index >= 0) {
			//update existing sell order
			table.get(index).addSellOrderAmount(order.getSize());
		}
		else {
			//add new sell order
			BookOrderTuple tuple = new BookOrderTuple();
			tuple.setPrice(order.getPrice());
			tuple.setSellOrderAmount(order.getSize());
			table.add(tuple);
		}
	}
	
	public void calcCumulativePrice() {
		for (BookOrderTuple tupleToAccumulate : table) {
			for (BookOrderTuple tupleToCompare : table) {
				if (tupleToAccumulate.getPrice() < tupleToCompare.getPrice()) {
					tupleToCompare.addSellOrderLeastFavorablePrice(tupleToAccumulate.getSellOrderAmount());
				}
				else if (tupleToAccumulate.getPrice() > tupleToCompare.getPrice()) {
					tupleToCompare.addBuyOrderLeastFavorablePrice(tupleToAccumulate.getBuyOrderAmount());
				}
				else {
					//This will happen if the two tuples are actually the same. 
					//Just adds the initial volume of orders to the cumulative orders (so like 0 + 700) 
					tupleToCompare.addBuyOrderLeastFavorablePrice(tupleToAccumulate.getBuyOrderAmount());
					tupleToCompare.addSellOrderLeastFavorablePrice(tupleToAccumulate.getSellOrderAmount());
				}
			}
		}
	}
	
	public Double findMatchPrice() {
		BookOrderTuple BiggestMin = new BookOrderTuple();
		//find largest minimum cumulative orders. That'll be the match price
		for (BookOrderTuple tuple : table) {
			Integer min = tuple.minOfOrder();
			if (min > BiggestMin.minOfOrder()) {
				BiggestMin = tuple;
			}
		}
		return BiggestMin.getPrice();
	}
	
	public int getMatchingIndex(double price) {
		for (int i = 0; i < table.size(); i++) {
			if (Double.compare(price, table.get(i).getPrice()) == 0) {
				return i;
			}
		}
		return -1;
	}
}
