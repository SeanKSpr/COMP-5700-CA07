package pkg.trader;

import static org.junit.Assert.*;

import org.junit.Test;

import pkg.exception.StockMarketExpection;
import pkg.market.Market;
import pkg.order.OrderType;
import pkg.order.SellOrder;
import pkg.stock.Stock;

public class TraderTest {

	@Test
	public void buyFromBankInsufficientFunds() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 0);
		Market m = getNASDAQ();
		try {
			trader.buyFromBank(m, "SBUX", 9999999);
		} catch(StockMarketExpection e) {
			return;
		}
		fail("Test should've failed due to insufficient funds");
	}
	
	@Test
	public void buyFromBankNominal() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 15000);
		Market m = getNASDAQ();
		try {
			trader.buyFromBank(m, "SBUX", 2);
		} catch(StockMarketExpection e) {
			fail("Test failed due to insufficient funds");
		}
		assertTrue(true);
	}
	
	@Test
	public void placeNewOrderNominal() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 15000);
		Market m = getNASDAQ();
		try {
			trader.placeNewOrder(m, "SBUX", 2, 100, OrderType.BUY);
		} catch(StockMarketExpection e) {
			fail("Test failed because expection");
		}
		assertTrue(true);
	}
	
	@Test
	public void placeNewOrderInsufficientFunds() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 0);
		Market m = getNASDAQ();
		try {
			trader.placeNewOrder(m, "SBUX", 2, 100, OrderType.BUY);
		} catch(StockMarketExpection e) {
			assertTrue(true);
			return;
		}
		fail("Should've failed due to insufficient funds");
	}
	
	@Test
	public void placeNewOrderDontOwnStock() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 0);
		Market m = getNASDAQ();
		try {
			trader.placeNewOrder(m, "SBUX", 2, 100, OrderType.SELL);
		} catch(StockMarketExpection e) {
			assertTrue(true);
			return;
		}
		fail("Should've failed because no stock was owned by the trader");
	}
	
	@Test
	public void placeNewOrderSellMoreThanOwn() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 0);
		Market m = getNASDAQ();
		SellOrder sellOrder = new SellOrder("SBUX", 1, 100.0, trader);
		trader.position.add(sellOrder);
		try {
			trader.placeNewOrder(m, "SBUX", 2, 100, OrderType.SELL);
		} catch(StockMarketExpection e) {
			assertTrue(true);
			return;
		}
		fail("Should've failed because not enough stock was owned by the trader");
	}
	
	@Test
	public void placeNewOrderDuplicateOrder() {
		Trader trader = new Trader("Sean", 0);
		Market m = null;
		try {
			m = getNASDAQ();
		} catch (StockMarketExpection e1) {
			fail();
			e1.printStackTrace();
		}
		try {
			trader.placeNewOrder(m, "SBUX", 2, 100, OrderType.BUY);
			trader.placeNewOrder(m, "SBUX", 2, 100, OrderType.BUY);
		} catch(StockMarketExpection e) {
			assertTrue(true);
			return;
		}
		fail("Should've failed because duplicate order");
	}
	
	@Test
	public void placeNewMarketOrderNominal() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 15000);
		Market m = getNASDAQ();
		try {
			trader.placeNewMarketOrder(m, "SBUX", 2, 100, OrderType.BUY);
		} catch(StockMarketExpection e) {
			fail("Test failed because expection");
		}
		assertTrue(true);
	}
	
	@Test
	public void placeNewMarketOrderInsufficientFunds() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 0);
		Market m = getNASDAQ();
		try {
			trader.placeNewMarketOrder(m, "SBUX", 2, 100, OrderType.BUY);
		} catch(StockMarketExpection e) {
			assertTrue(true);
			return;
		}
		fail("Should've failed due to insufficient funds");
	}
	
	@Test
	public void placeNewMarketOrderDontOwnStock() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 0);
		Market m = getNASDAQ();
		try {
			trader.placeNewMarketOrder(m, "SBUX", 2, 100, OrderType.SELL);
		} catch(StockMarketExpection e) {
			assertTrue(true);
			return;
		}
		fail("Should've failed because no stock was owned by the trader");
	}
	
	@Test
	public void placeNewMarketOrderSellMoreThanOwn() throws StockMarketExpection {
		Trader trader = new Trader("Sean", 0);
		Market m = getNASDAQ();
		SellOrder sellOrder = new SellOrder("SBUX", 1, 100.0, trader);
		trader.position.add(sellOrder);
		try {
			trader.placeNewMarketOrder(m, "SBUX", 2, 100, OrderType.SELL);
		} catch(StockMarketExpection e) {
			assertTrue(true);
			return;
		}
		fail("Should've failed because not enough stock was owned by the trader");
	}
	
	@Test
	public void placeNewMarketOrderDuplicateOrder() {
		Trader trader = new Trader("Sean", 0);
		Market m = null;
		try {
			m = getNASDAQ();
		} catch (StockMarketExpection e1) {
			fail();
			e1.printStackTrace();
		}
		try {
			trader.placeNewMarketOrder(m, "SBUX", 2, 100, OrderType.BUY);
			trader.placeNewMarketOrder(m, "SBUX", 2, 100, OrderType.BUY);
		} catch(StockMarketExpection e) {
			assertTrue(true);
			return;
		}
		fail("Should've failed because duplicate order");
	}
	
	private Market getNASDAQ() throws StockMarketExpection {
		Market m = new Market("NASDAQ");
		Stock stock = new Stock("SBUX", "Starbux", 98.5);
		m.addStock(stock);
		return m;
	}
	
	
}
