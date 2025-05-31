package genetics.selection;

import genetics.Fitness;
import genetics.Population;

public abstract class Selection {
    Population currentPopulation;
    Fitness[] fitness;

    public Selection(Population currentPopulation, Fitness[] fitness) {
        if (currentPopulation.getSize() != fitness.length) {
            throw new IllegalArgumentException("Mismatch: Population size is "
                    + currentPopulation.getSize() + ", but fitness array length is " + fitness.length);
        }

        this.currentPopulation = currentPopulation;
        this.fitness = fitness;
    }

    public abstract Population getNextPopulation();
}
