public class FundTransferService implements Transferable {
    @Override
    public boolean transfer(Account source, Account destination, double amount) {
        if (source == null || destination == null) {
            return false;
        }
        if (source.equals(destination)) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }

        if (source.withdraw(amount)) {
            destination.deposit(amount);
            return true;
        }
        return false;
    }
}