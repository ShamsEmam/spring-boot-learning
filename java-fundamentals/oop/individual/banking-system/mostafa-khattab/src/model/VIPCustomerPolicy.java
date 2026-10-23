
package model;

public class VIPCustomerPolicy implements CustomerServicePolicy {

    @Override
    public double getTransferFee(double amount) {
        return amount * 0.002;
    }

    @Override
    public double getDailyTransferLimit() {
        return 100_000.0;
    }
}
