package pkg.order;


public class BookOrderTuple {
	private Double price;
	private Integer buyOrderCumulativePerPrice;
	private Integer sellOrderCumulativePerPrice;
	private Integer buyOrderLeastFavorablePrice;
	private Integer sellOrderLeastFavorablePrice;
	

	BookOrderTuple() {
		buyOrderCumulativePerPrice = 0;
		sellOrderCumulativePerPrice = 0;
		buyOrderLeastFavorablePrice = 0;
		sellOrderLeastFavorablePrice = 0;
	}
	public Integer minOfOrder() {
		if (this.buyOrderLeastFavorablePrice < this.sellOrderLeastFavorablePrice) {
			return this.buyOrderLeastFavorablePrice;
		}
		else
			return this.sellOrderLeastFavorablePrice;
	}
	
	public void addSellOrderLeastFavorablePrice(Integer amount) {
		this.sellOrderLeastFavorablePrice += amount;
	}
	
	public void addBuyOrderLeastFavorablePrice(Integer amount) {
		this.buyOrderLeastFavorablePrice += amount;
	}
	
	public Integer getBuyOrderLeastFavorablePrice() {
		return buyOrderLeastFavorablePrice;
	}
	public void setBuyOrderLeastFavorablePrice(Integer buyOrderLeastFavorablePrice) {
		this.buyOrderLeastFavorablePrice = buyOrderLeastFavorablePrice;
	}
	public Integer getSellOrderLeastFavorablePrice() {
		return sellOrderLeastFavorablePrice;
	}
	public void setSellOrderLeastFavorablePrice(Integer sellOrderLeastFavorablePrice) {
		this.sellOrderLeastFavorablePrice = sellOrderLeastFavorablePrice;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getBuyOrderAmount() {
		return buyOrderCumulativePerPrice;
	}
	public void setBuyOrderAmount(Integer buyOrderAmount) {
		this.buyOrderCumulativePerPrice = buyOrderAmount;
	}
	public void addBuyOrderAmount(Integer amount) {
		this.buyOrderCumulativePerPrice += amount;
	}
	public Integer getSellOrderAmount() {
		return sellOrderCumulativePerPrice;
	}
	public void setSellOrderAmount(Integer sellOrderAmount) {
		this.sellOrderCumulativePerPrice = sellOrderAmount;
	}
	public void addSellOrderAmount(Integer amount) {
		this.sellOrderCumulativePerPrice += amount;
	}
	
}
