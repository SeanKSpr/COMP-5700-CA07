package pkg.order;

import static org.junit.Assert.*;

import org.junit.Test;

public class BookOrderTupleTest {

	@Test
	public void minOfOrderTest() {
		BookOrderTuple tuple = new BookOrderTuple();
		tuple.setBuyOrderAmount(400);
		tuple.setBuyOrderLeastFavorablePrice(1600);
		tuple.setPrice(100.0);
		tuple.setSellOrderAmount(500);
		tuple.setSellOrderLeastFavorablePrice(4200);
		assertEquals(tuple.minOfOrder().intValue(), 1600);
	}
	
	@Test
	public void minOfOrderTestSameBuyAndSellValues() {
		BookOrderTuple tuple = new BookOrderTuple();
		tuple.setBuyOrderAmount(400);
		tuple.setBuyOrderLeastFavorablePrice(1600);
		tuple.setPrice(100.0);
		tuple.setSellOrderAmount(500);
		tuple.setSellOrderLeastFavorablePrice(1600);
		assertEquals(tuple.getBuyOrderLeastFavorablePrice(), tuple.getSellOrderLeastFavorablePrice());
		assertEquals(tuple.minOfOrder().intValue(), 1600);
	}
}
