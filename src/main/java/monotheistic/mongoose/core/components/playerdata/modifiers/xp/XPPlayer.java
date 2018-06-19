package monotheistic.mongoose.core.components.playerdata.modifiers.xp;


import monotheistic.mongoose.core.components.playerdata.modifiers.PlayerDataModifier;

public interface XPPlayer extends PlayerDataModifier {
    int currentXp();

    void setCurrentXp(int amt);

    int currentLevel();

    void setCurrentLevel(int amt);

    int threshold();

    int previousThreshold();

    void setNextThreshold();

    void setPreviousThreshold();

    default void giveXp(int amt) {
        setCurrentXp(currentXp() + amt);
        while (currentXp() >= threshold()) {
            setCurrentXp(currentXp() - threshold());
            setNextThreshold();
            levelUp();
        }
    }

    default void takeXp(int amt) {
        int margin = currentXp() - previousThreshold();
        if (margin > amt) {
            setCurrentXp(currentXp() - amt);
            return;
        }
        setCurrentXp(currentXp() - margin);
        amt -= margin;
        int counter = 0;
        while (amt > previousThreshold() && previousThreshold() != threshold()) {
            amt -= previousThreshold();
            setCurrentXp(previousThreshold());
            setPreviousThreshold();
            levelDown();
        }
        setCurrentXp(currentXp() - amt >= 0 ? currentXp() - amt : 0);

    }

    default void levelUp() {
        setCurrentLevel(currentLevel() + 1);
    }

    default void levelDown() {
        setCurrentLevel(currentLevel() - 1);
    }


}
