package TPackage;

public class BankAccount {
    private int balance = 10000;

    public void deposit(int amount) {
        balance += amount;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }

    public static void transfer(BankAccount acc1, BankAccount acc2, int amount) {
        acc1.withdraw(amount);
        acc2.deposit(amount);
    }
}
