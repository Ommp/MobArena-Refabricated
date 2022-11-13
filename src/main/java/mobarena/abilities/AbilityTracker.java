package mobarena.abilities;

import java.util.Random;

public class AbilityTracker {

    private int abilityCount = 0;
    private int abilityLimit = 0;

    public void incrementAbilityCount() {
        abilityCount++;
    }

    public void setAbilityLimit() {
        if (abilityLimit == 0) {
            abilityLimit = new Random().nextInt((25 - 10) + 1) + 10;
        }
    }

    public boolean limitReached() {
        return abilityCount>=abilityLimit;
    }

    public Ability selectRandomAbililty() {
        int random = new Random().nextInt(Abilities.abilities.size());
        abilityLimit = 0;
        abilityCount = 0;
        return Abilities.abilities.get(random);
    }
}
