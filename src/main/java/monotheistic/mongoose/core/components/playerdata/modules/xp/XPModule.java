package monotheistic.mongoose.core.components.playerdata.modules.xp;

public class XPModule {
    private int currentXp;
    private int increase;
    private int level;
    private int currentThreshold;

    public XPModule(int currentXp, int increase, int level, int currentThreshold) {
        this.currentXp = currentXp;
        this.increase = increase;
        this.level = level;
        this.currentThreshold = currentThreshold;
    }

    public XPModule() {
        this(0, 0, 0, 0);
    }

    private void setCurrentXp(int amt) {
        this.currentXp = amt;
    }

    int currentXp() {
        return currentXp;
    }

    int threshold() {
        return this.currentThreshold;
    }

    private void setNextThreshold() {
        this.currentThreshold += increase;
    }

    private int previousThreshold() {
        return this.currentThreshold - increase;
    }

    private void setPreviousThreshold() {
        this.currentThreshold -= increase;
    }

    void giveXp(int amt) {
        setCurrentXp(currentXp() + amt);
        while (currentXp() >= threshold()) {
            setCurrentXp(currentXp() - threshold());
            setNextThreshold();
            this.level++;
        }
    }

    void takeXp(int amt) {
        int margin = currentXp() - previousThreshold();
        if (margin > amt) {
            setCurrentXp(currentXp() - amt);
            return;
        }
        setCurrentXp(currentXp() - margin);
        amt -= margin;
        while (amt > previousThreshold() && previousThreshold() != threshold()) {
            amt -= previousThreshold();
            setCurrentXp(previousThreshold());
            setPreviousThreshold();
            this.level--;
        }
        setCurrentXp(currentXp() - amt >= 0 ? currentXp() - amt : 0);

    }

    int getCurrentXp() {
        return currentXp;
    }

    int getIncrease() {
        return increase;
    }

    int getLevel() {
        return level;
    }

    int getCurrentThreshold() {
        return currentThreshold;
    }
}
