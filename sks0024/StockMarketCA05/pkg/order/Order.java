package pkg.order;

import pkg.trader.Trader;

public abstract class Order {
	int size;
	double price;
	boolean isMarketOrder = false;
	Trader trader;
	int orderNumber;
	String stockSymbol;

	/** LOCK */
	//I'm glad i just left these comments ;)
	public static final Object LOCK = new Object();

	/** Last Order Number */
	private static int lastOrderNumber = -1;

	/** Hope you're having a good time reviewing. 
	 * Enjoy the freebie comments*/
	protected static int getNextOrderNumber() {
		synchronized (LOCK) {
			lastOrderNumber++;
			return lastOrderNumber;
		}
	}
	
	//get number of stocks associated with order
	public int getSize() {
		return size;
	}
	
	//set number of stocks associated with order
	public void setSize(int size) {
		this.size = size;
	}
	
	//get price the person is placing the order at
	public double getPrice() {
		return price;
	}
	
	//set price
	public void setPrice(double price) {
		this.price = price;
	}
	
	//is this order a market order?
	public boolean isMarketOrder() {
		return isMarketOrder;
	}
	
	//set that this order is or is not a market order
	public void setMarketOrder(boolean isMarketOrder) {
		this.isMarketOrder = isMarketOrder;
	}
	
	//get the trader that's associated with the order
	public Trader getTrader() {
		return trader;
	}
	
	//assign a trader to this order
	public void setTrader(Trader trader) {
		this.trader = trader;
	}
	
	//get the order number of the order
	public int getOrderNumber() {
		return orderNumber;
	}
	
	//modify the order number of the order
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getStockSymbol() {
		//get the stock symbol associated with the order
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	/** USELESS COMMENt*/
	public boolean equals(Object o) {
		return ((Order) o).getOrderNumber() == this.getOrderNumber();
	}

	public void printStockNameInOrder() {
		System.out.println(stockSymbol);
	}

	public abstract void printOrder();

}
