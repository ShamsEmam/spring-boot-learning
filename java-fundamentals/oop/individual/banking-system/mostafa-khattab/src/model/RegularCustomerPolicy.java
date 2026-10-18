
package model;

public class RegularCustomerPolicy implements CustomerServicePolicy {

    @Override
    public double getTransferFee(double amount) {
        return amount * 0.01;
    }

    @Override
    public double getDailyTransferLimit() {
        return 10_000.0;
    }
}
