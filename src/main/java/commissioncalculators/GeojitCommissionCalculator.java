package commissioncalculators;
import core.Stock;
import core.Trade;


public class GeojitCommissionCalculator extends SEBICommissionCalculator implements CommissionCalculator{

	private void setCommission(Trade trade,double transactionAmount,double stockPrice,double quantity){
		
		double commission = transactionAmount * .3/100;
		if(commission <= 20){
			commission = 20;
		}
		double _sebiMaxCommission = getMaxCommission(stockPrice,quantity);
		if(commission > _sebiMaxCommission){
			commission = _sebiMaxCommission;
		}
		
		double transactionTax = trade.getQuantity() * trade.getGrossrate()*.1/100;
		transactionTax = Math.ceil(transactionTax);
		trade.setTransactionTax(transactionTax);
	}
	
	@Override
	public void calculateCommission(Trade trade) {
		if(trade.getTradeType().equals("R")){
			trade.setCommission(0);
			return;
		}
		double transactionAmount = trade.getQuantity() * trade.getGrossrate();
		setCommission(trade,transactionAmount,trade.getGrossrate(),trade.getQuantity());
	}

	@Override
	public void adjustCurrentPrice(Stock stock) {
		if(stock.getTotalQuantity() == 0){
			return;
		}
		double transactionAmount = stock.getTotalQuantity() * stock.getCurrentPrice();
		double commission = 0;
		transactionAmount -= commission;
		stock.setCurrentPrice(transactionAmount/stock.getTotalQuantity());
	}
}
