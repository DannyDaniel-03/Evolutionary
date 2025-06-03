package genetics;

import SAT.Statement;
import genetics.selection.Selection;
import genetics.selection.Tournament;

import java.util.concurrent.ThreadLocalRandom;

public class Simulation {
    private static final int POP_SIZE = 300;
    private static final int EXTINCTION = 1000;
    private static final double CROSS_OVER_CHANCE = 0.2;
    private final int variableCount;
    private final Statement statement;

    public Simulation(int variableCount, Statement statement) {
        this.variableCount = variableCount;
        this.statement = statement;
    }

    public void simulate() {
        int t = 0;
        //generate the starting population
        Population population = new Population(POP_SIZE, variableCount, true);

        //evaluate P(t)
        Fitness[] populationFitness = new Fitness[POP_SIZE];
        Fitness bestFitness = new Fitness(statement, population.at(1));
        Chromosome bestChromosome = population.at(1);
        populationFitness[0] = bestFitness;
        for (int i = 1; i < POP_SIZE; i++) {
            Fitness chromosomeFitness = new Fitness(statement, population.at(i + 1));
            populationFitness[i] =  chromosomeFitness;

            if (chromosomeFitness.getFitness() > bestFitness.getFitness()) {
                bestFitness = chromosomeFitness;
                bestChromosome = population.at(i + 1);
            }
        }

        System.out.println("Run: " + t + ". Satisfied: " + statement.getTrueClauses(bestChromosome) + ". To satisfy: " + statement.getLength() + ".");
        //stopping condition
        while (t < EXTINCTION && !statement.isSatisfied(bestChromosome)) {
            t++;

            //select P(t) from P(t-1)
            Selection selection = new Tournament(population, populationFitness);
            population = selection.getNextPopulation();

            //mutate P(T)
            for (int i = 1; i <= POP_SIZE; i++) {
                Mutation.mutate(population.at(i));
            }

            //cross-over P(T)
            int totalCross = (int) Math.round(POP_SIZE * CROSS_OVER_CHANCE);
            for (int x = 0; x < totalCross; x++) {
                int parentA = ThreadLocalRandom.current().nextInt(1, POP_SIZE + 1);
                int parentB;
                do {
                    parentB = ThreadLocalRandom.current().nextInt(1, POP_SIZE + 1);
                } while (parentB == parentA);
                population.crossover(parentA, parentB, 1, true);
            }

            //evaluate P(T)
            for (int i = 1; i <= POP_SIZE; i++) {
                Fitness chromosomeFitness = new Fitness(statement, population.at(i));
                populationFitness[i - 1] = chromosomeFitness;

                if (chromosomeFitness.getFitness() > bestFitness.getFitness()) {
                    bestFitness = chromosomeFitness;
                }
            }

            //incrementing the weight of each clause
            for (int i = 1; i <= statement.getLength(); i++) {
                if (!statement.getClause(i).isSatisfied(bestChromosome)) {
                    statement.incWeight(i);
                }
            }

            //decaying the weight
            if (t % 100 == 0) {
                double maxWeight = 0;
                for (int i = 1; i <= statement.getLength(); i++) {
                    double w = statement.getClauseWeight(i);
                    if (w > maxWeight) {
                        maxWeight = w;
                    }                    
                }                        

                if (maxWeight > 100) {
                    for (int i = 1; i <= statement.getLength(); i++) {
                        int initial = statement.getClause(i).getSize();
                        double reduced = statement.getClauseWeight(i) / 3;
                        if (reduced < initial) {
                            reduced = initial;
                        }
                        statement.setClauseWeight(i, reduced);
                    }                    
                }                        
            }                            

            if (t % 100 == 0 && t != 1000) {
                System.out.println("Run: " + t + ". Satisfied: " + statement.getTrueClauses(bestChromosome) + ". To satisfy: " + statement.getLength() + ".");
            }
        }

        System.out.println("Run: " + t + ". Satisfied: " + statement.getTrueClauses(bestChromosome) + ". To satisfy: " + statement.getLength() + ".");
    }
}
