package pkg.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pkg.exception.StockMarketExpection;
import pkg.market.Market;
import pkg.market.api.PriceSetter;

public class OrderBook {
	Market market;
	HashMap<String, ArrayList<Order>> buyOrders;
	HashMap<String, ArrayList<Order>> sellOrders;

	public OrderBook(Market market) {
		this.market = market;
		buyOrders = new HashMap<String, ArrayList<Order>>();
		sellOrders = new HashMap<String, ArrayList<Order>>();
	}

	public void addToOrderBook(Order order) {
		// Populate the buyOrders and sellOrders data structures, whichever
		// appropriate
		if (order instanceof BuyOrder) {
			ArrayList<Order> orderList = new ArrayList<Order>();
			if (buyOrders.containsKey(order.getStockSymbol())) {
				orderList =	buyOrders.get(order.getStockSymbol());
			}
			orderList.add(order);
			buyOrders.put(order.getStockSymbol(), orderList);
		}
		else if (order instanceof SellOrder) {
			ArrayList<Order> orderList = new ArrayList<Order>();
			if (sellOrders.containsKey(order.getStockSymbol())) {
				orderList =	sellOrders.get(order.getStockSymbol());
			}
			orderList.add(order);
			sellOrders.put(order.getStockSymbol(), orderList);
		}
	}
	
	/**
	 * trade goes through each Stock associated with the market and performs stock trading of buy and sell orders.
	 * After determining the largest number of trade transactions which can occur for a particular stock, the trade
	 * price which resulted in the greatest trade transaction shall be made the new stock price. Afterwards, those stocks
	 * which were traded will be updated with the Traders' order lists.
	 *		2. Find the matching price
	 *		3. Update the stocks price in the market using the PriceSetter.
	 *		4. Remove the traded orders from the orderbook
	 *		5. Delegate to trader that the trade has been made, so that the
	 *			trader's orders can be placed to his possession (a trader's position
	 *			is the stocks he owns)
	 */
	public void trade() {

		
		ArrayList<String> stockSymbols = new ArrayList<String>();
		getStockSymbols(stockSymbols);
		
		for (String stockSymbol : stockSymbols) {
			ArrayList<Order> buys = new ArrayList<Order>();
			ArrayList<Order> sells = new ArrayList<Order>();
			
			if (this.buyOrders.containsKey(stockSymbol)) {
				buys = this.buyOrders.get(stockSymbol);
			}
			if (this.sellOrders.containsKey(stockSymbol)) {
				sells = this.sellOrders.get(stockSymbol);
			}
			//create a section of the orderbook this would be like the "Starbucks" page in the order book
			BookOrderSection section = new BookOrderSection(stockSymbol,buys,sells);
			//That top statement will create the entire table and stuff associated with the stock
			
			double matchPrice = section.findMatchPrice();
			//Now we want to set the new price of the stock to the match price
			PriceSetter pc = new PriceSetter();
			this.market.getMarketHistory().setSubject(pc);
			pc.registerObserver(this.market.getMarketHistory());
			pc.setNewPrice(this.market, stockSymbol, matchPrice);
			//Get all the orders traded
			ArrayList<Order> buyOrdersTraded = findBuyOrdersTraded(matchPrice, buyOrders.get(stockSymbol));
			ArrayList<Order> sellOrdersTraded = findSellOrdersTraded(matchPrice, sellOrders.get(stockSymbol));
			//tell the traders to update their trade lists and stuff
			delegateTradesToTraders(buyOrdersTraded, sellOrdersTraded, matchPrice);
			//then remove the orders which were traded from the orders placed list
			removeTradedOrders(stockSymbol, buyOrdersTraded, sellOrdersTraded);
		}
		
	}

	private void delegateTradesToTraders(ArrayList<Order> buyOrdersTraded, ArrayList<Order> sellOrdersTraded, Double matchPrice) {
		for (Order order : buyOrdersTraded) {
			try {
				order.trader.tradePerformed(order, matchPrice);
			} catch (StockMarketExpection e) {
				
				e.printStackTrace();
			}
		}
		for (Order order : sellOrdersTraded) {
			try {
				order.trader.tradePerformed(order, matchPrice);
			} catch (StockMarketExpection e) {
				
				e.printStackTrace();
			}
		}
	}

	private void removeTradedOrders(String stockSymbol, ArrayList<Order> buyOrdersTraded, ArrayList<Order> sellOrdersTraded) {
		ArrayList<Order> buyOrderList = buyOrders.get(stockSymbol);
		ArrayList<Order> sellOrderList = sellOrders.get(stockSymbol);
		for (int i = 0; i < buyOrderList.size(); i++) {
			if (buyOrdersTraded.contains(buyOrderList.get(i))) {
				buyOrders.get(stockSymbol).remove(buyOrderList.get(i));
			}
		}
		
		for (int i = 0; i < sellOrderList.size(); i++) {
			if (sellOrdersTraded.contains(sellOrderList.get(i))) {
				sellOrders.get(stockSymbol).remove(sellOrderList.get(i));
			}
		}
	}

	private ArrayList<Order> findSellOrdersTraded(Double matchPrice,
			ArrayList<Order> sellList) {
		ArrayList<Order> sellOrdersTraded = new ArrayList<Order>();
		for (Order order : sellList) {
			if (order.getPrice() < matchPrice) {
				sellOrdersTraded.add(order);
			}
		}
		return sellOrdersTraded;
	}

	private ArrayList<Order> findBuyOrdersTraded(Double matchPrice,
			ArrayList<Order> buyList) {
		ArrayList<Order> buyOrdersTraded = new ArrayList<Order>();
		for (Order order : buyList) {
			if (order.getPrice() > matchPrice) {
				buyOrdersTraded.add(order);
			}
		}
		return buyOrdersTraded;
	}
	
	private void getStockSymbols(ArrayList<String> stockSymbols) {
		for (Map.Entry<String, ArrayList<Order>> entry : buyOrders.entrySet()) {
			String stockSymbol = entry.getKey();
			if (!stockSymbols.contains(stockSymbol)) {
				stockSymbols.add(stockSymbol);
			}
		}
		
		for (Map.Entry<String, ArrayList<Order>> entry : sellOrders.entrySet()) {
			String stockSymbol = entry.getKey();
			if (!stockSymbols.contains(stockSymbol)) {
				stockSymbols.add(stockSymbol);
			}
		}
	}

}
