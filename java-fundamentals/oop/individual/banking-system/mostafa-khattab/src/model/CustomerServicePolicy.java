
package model;

public interface CustomerServicePolicy {

    double getTransferFee(double amount);

    double getDailyTransferLimit();
}
