package monotheistic.mongoose.core.components.playerdata.modules.economy;

public class EconomyModule {
    private double balance;

    public EconomyModule(double balance) {
        this.balance = balance;
    }

    public EconomyModule() {
        this(0);
    }

    double getBalance() {
        return balance;
    }

    double take(double amt) {
        final double difference = balance - amt;
        balance -= difference >= 0 ? amt : balance;
        return difference;
    }

    double give(double amt) {
        return balance += amt;
    }
}
