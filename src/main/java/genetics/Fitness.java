package genetics;

import SAT.Clause;
import SAT.Statement;

public class Fitness {
    private final double fitness;
    private final Chromosome chromosome;
    private final Statement statement;

    public Fitness(Statement statement, Chromosome chromosome) {
        this.chromosome = chromosome;
        this.statement = statement;

        double weightedFitness = 0;
        for (int i = 1; i <= statement.getLength(); i++) {
            Clause clause = statement.getClause(i);
            double weight = statement.getClauseWeight(i);
            weightedFitness += clause.isSatisfied(chromosome) ? weight : -weight;
        }

        fitness = weightedFitness;
    }



    public double getFitness() {
        return fitness;
    }

}
