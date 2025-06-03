package genetics;

import SAT.Statement;

public class Fitness {
    private final int maxFitness;
    private final int fitness;

    public Fitness(Statement statement, Chromosome chromosome) {
        maxFitness = statement.getLength();
        fitness = statement.getTrueClauses(chromosome);
    }

    public int max() {
        return maxFitness;
    }

    public int getFitness() {
        return fitness;
    }

}
