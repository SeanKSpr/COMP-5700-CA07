package pkg.order;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import pkg.trader.Trader;

public class BookOrderTableTest {

	@Test
	public void addBuyOrdersTest() {
		ArrayList<Order> buyOrderList = getNewBuyOrderList();
		BookOrderTable table = new BookOrderTable();
		table.addBuyOrders(buyOrderList);
		assertEquals(buyOrderList.size(), table.getTable().size());
	}

	private ArrayList<Order> getNewBuyOrderList() {
		BuyOrder order;
		ArrayList<Order> buyOrderList = new ArrayList<Order>();
		for (int i = 0; i < 10; i++) {
			order = new BuyOrder("SBUX", 500 + i, 100.0 + i, new Trader("Sean " + i, 10000.0 + i));
			buyOrderList.add(order);
		}
		return buyOrderList;
	}
	
	@Test
	public void addBuyOrderTest() {
		BuyOrder order = new BuyOrder("SBUX", 500, 100.0, new Trader("Sean", 10000.0));
		BookOrderTuple tuple = new BookOrderTuple();
		tuple.setPrice(100.0);
		tuple.setBuyOrderAmount(500);
		BookOrderTable table = new BookOrderTable();
		table.addBuyOrder(order);
		assertEquals(table.getTable().get(0).getBuyOrderAmount(), tuple.getBuyOrderAmount());
		assertEquals(table.getTable().get(0).getPrice(), tuple.getPrice());
		
		int oldBuyOrderAmount = table.getTable().get(0).getBuyOrderAmount().intValue();
		table.addBuyOrder(order);
		
		assertEquals(oldBuyOrderAmount * 2, table.getTable().get(0).getBuyOrderAmount().intValue());
		assertEquals(table.getTable().get(0).getPrice(), tuple.getPrice());
	}
	
	
}
