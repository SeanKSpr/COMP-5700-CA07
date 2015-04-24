package pkg.trader;

import java.util.ArrayList;

import pkg.exception.StockMarketExpection;
import pkg.market.Market;
import pkg.order.BuyOrder;
import pkg.order.Order;
import pkg.order.OrderType;
import pkg.order.SellOrder;
import pkg.stock.Stock;
import pkg.util.OrderUtility;

public class Trader {
	// Name of the trader
	String name;
	// Cash left in the trader's hand
	double cashInHand;
	// Stocks owned by the trader
	ArrayList<Order> position;
	// Orders placed by the trader
	ArrayList<Order> ordersPlaced;

	public Trader(String name, double cashInHand) {
		super();
		this.name = name;
		this.cashInHand = cashInHand;
		this.position = new ArrayList<Order>();
		this.ordersPlaced = new ArrayList<Order>();
	}

	public void buyFromBank(Market m, String symbol, int volume)
			throws StockMarketExpection {
		// Buy stock straight from the bank
		// Need not place the stock in the order list
		// Add it straight to the user's position
		// If the stock's price is larger than the cash possessed, then an
		// exception is thrown
		// Adjust cash possessed since the trader spent money to purchase a
		// stock.
		Stock stockBeingPurchased = m.getStockForSymbol(symbol);
		if (stockBeingPurchased.getPrice() * volume > this.cashInHand) {
			throw new StockMarketExpection("Trader.buyFromBank: Not enough cash on hand to make purchase. Trader: " + this.name);
		}
		cashInHand = (cashInHand - stockBeingPurchased.getPrice() * volume);
		BuyOrder order = new BuyOrder(symbol, volume, stockBeingPurchased.getPrice(), this);
		addToPosition(order);
	}

	public void placeNewOrder(Market m, String symbol, int volume,
			double price, OrderType orderType) throws StockMarketExpection {
		// Place a new order and add to the orderlist
		// Also enter the order into the orderbook of the market.
		// Note that no trade has been made yet. The order is in suspension
		// until a trade is triggered.
		//
		// If the stock's price is larger than the cash possessed, then an
		// exception is thrown
		// A trader cannot place two orders for the same stock, throw an
		// exception if there are multiple orders for the same stock.
		// Also a person cannot place a sell order for a stock that he does not
		// own. Or he cannot sell more stocks than he possesses. Throw an
		// exception in these cases.
		Order order;
		if (orderType == OrderType.BUY) {
			if (price * volume > this.cashInHand) {
				throw new StockMarketExpection("Trader.placeNewOrder: Not enough cash on hand to make purchase.  Trader: " + this.name);
			}
			order = new BuyOrder(symbol, volume, price, this);
		}
		else {
			if (!OrderUtility.owns(position, symbol)) {
				throw new StockMarketExpection("Trader.placeNewOrder: Attempting to place sell order on stock that isn't owned.  Trader: " + this.name);
			}
			else if (OrderUtility.ownedQuantity(position, symbol) < volume) {
				throw new StockMarketExpection("Trader.placeNewOrder: Attempting to sell more orders than owned.  Trader: " + this.name);
			}	
			order = new SellOrder(symbol, volume, price, this);
		}
		if (OrderUtility.isAlreadyPresent(ordersPlaced, order)) {
			throw new StockMarketExpection("Trader.placeNewOrder: This order has already been placed.  Trader: " + this.name);
		}
		
		ordersPlaced.add(order);
		m.addOrder(order);
	}

	public void placeNewMarketOrder(Market m, String symbol, int volume,
			double price, OrderType orderType) throws StockMarketExpection {
		// Similar to the other method, except the order is a market order
		Order order;
		if (orderType == OrderType.BUY) {
			if (price * volume > this.cashInHand) {
				throw new StockMarketExpection("Trader.placeNewOrder: Not enough cash on hand to make purchase.  Trader: " + this.name);
			}
			order = new BuyOrder(symbol, volume, true, this);
		}
		else {
			if (!OrderUtility.owns(position, symbol)) {
				throw new StockMarketExpection("Trader.placeNewOrder: Attempting to place sell order on stock that isn't owned.  Trader: " + this.name);
			}
			else if (OrderUtility.ownedQuantity(position, symbol) < volume) {
				throw new StockMarketExpection("Trader.placeNewOrder: Attempting to sell more orders than owned.  Trader: " + this.name);
			}
			order = new SellOrder(symbol, volume, true, this);
		}
		if (OrderUtility.isAlreadyPresent(ordersPlaced, order)) {
			throw new StockMarketExpection("Trader.placeNewOrder: This order has already been placed.  Trader: " + this.name);
		}
		
		ordersPlaced.add(order);
		m.addOrder(order);
	}

	public void tradePerformed(Order o, double matchPrice)
			throws StockMarketExpection {
		// Notification received that a trade has been made, the parameters are
		// the order corresponding to the trade, and the match price calculated
		// in the order book. Note than an order can sell some of the stocks he
		// bought, etc. Or add more stocks of a kind to his position. Handle
		// these situations.

		// Update the trader's orderPlaced, position, and cashInHand members
		// based on the notification.
		if (o instanceof BuyOrder) {
			if (matchPrice * o.getSize() > this.cashInHand) {
				throw new StockMarketExpection("Trader.placeNewOrder: Not enough cash on hand to make purchase.  Trader: " + this.name);
			}
			doBuyStock(o, matchPrice);
		}
		else if (o instanceof SellOrder) {
			if (!OrderUtility.owns(position, o.getStockSymbol())) {
				throw new StockMarketExpection("Trader.placeNewOrder: Attempting to place sell order on stock that isn't owned.  Trader: " + this.name);
			}
			else if (OrderUtility.ownedQuantity(position, o.getStockSymbol()) < o.getSize()) {
				throw new StockMarketExpection("Trader.placeNewOrder: Attempting to sell more orders than owned.  Trader: " + this.name);	
			}
			doSellStock(o, matchPrice);
		}
	}
	
	//TODO:rename
	private void doSellStock(Order orderBeingSold, double matchPrice) {
		OrderUtility.findAndExtractOrder(ordersPlaced, orderBeingSold.getStockSymbol());
		OrderUtility.findAndExtractOrder(position, orderBeingSold.getStockSymbol());
		cashInHand += orderBeingSold.getSize() * matchPrice;
	}
	
	//TODO:rename
	private void doBuyStock(Order orderBeingPurchased, double matchPrice) {
		OrderUtility.findAndExtractOrder(ordersPlaced, orderBeingPurchased.getStockSymbol());
		addToPosition(orderBeingPurchased);
		cashInHand -= orderBeingPurchased.getSize() * matchPrice;
		
	}

	private void addToPosition(Order orderBeingPurchased) {
		if (OrderUtility.isAlreadyPresent(position, orderBeingPurchased)) {
			Order orderExtractedFromPosition = OrderUtility.findAndExtractOrder(position, orderBeingPurchased.getStockSymbol());
			orderExtractedFromPosition.setSize(orderExtractedFromPosition.getSize() + orderBeingPurchased.getSize());
			position.add(orderExtractedFromPosition);
		}
		else {
			position.add(orderBeingPurchased);
		}
	}

	public void printTrader() {
		System.out.println("Trader Name: " + name);
		System.out.println("=====================");
		System.out.println("Cash: " + cashInHand);
		System.out.println("Stocks Owned: ");
		for (Order o : position) {
			o.printStockNameInOrder();
		}
		System.out.println("Stocks Desired: ");
		for (Order o : ordersPlaced) {
			o.printOrder();
		}
		System.out.println("+++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++");
	}
	
	/*
	 * public String getFunnyString() {
	 * 		return "[insert joke here]"
	 * }
	 */
}
