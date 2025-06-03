package genetics;

import SAT.Statement;
import genetics.selection.Selection;
import genetics.selection.Tournament;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Simulation {
    private static final int POP_SIZE = 200;
    private static final int EXTINCTION = 1000;
    private static final double CROSS_OVER_CHANCE = 0.3;
    private static final double SCRAMBLE_CHANCE = 0.2;
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

        final Population popSnapLS0 = population;
        IntStream.range(0, POP_SIZE).parallel().forEach(i -> LocalSearch.greedyImprove(statement, popSnapLS0.at(i + 1)));

        //evaluate P(t)
        Fitness[] populationFitness = new Fitness[POP_SIZE];
        final Population popSnaphot0 = population;
        Arrays.parallelSetAll(populationFitness, i ->
                new Fitness(statement, popSnaphot0.at(i + 1))
        );

        Fitness bestFitness = populationFitness[0];
        Chromosome bestChromosome = popSnaphot0.at(1);
        for (int i = 1; i < POP_SIZE; i++) {
            if (populationFitness[i].getFitness() > bestFitness.getFitness()) {
                bestFitness = populationFitness[i];
                bestChromosome = popSnaphot0.at(i+1);
            }
        }

        System.out.println("Run: " + t + ". Current fitness: " + bestFitness.getFitness() + ". Max possible fitness: " + bestFitness.max() + ".");
        //stopping condition
        while (t < EXTINCTION && bestFitness.getFitness() < bestFitness.max()) {
            t++;

            //select P(t) from P(t-1)
            Selection selection = new Tournament(population, populationFitness, statement);
            population = selection.getNextPopulation();

            //cross-over P(T)
            int totalCross = (int) Math.round(POP_SIZE * CROSS_OVER_CHANCE);
            for (int x = 0; x < totalCross; x++) {
                int parentA = ThreadLocalRandom.current().nextInt(1, POP_SIZE + 1);
                int parentB;
                do {
                    parentB = ThreadLocalRandom.current().nextInt(1, POP_SIZE + 1);
                } while (parentB == parentA);
                population.crossover(parentA, parentB, 1, false);
            }

            //mutate P(T)
            for (int i = 1; i <= POP_SIZE; i++) {
                Mutation.mutate(population.at(i), 0.02);
            }

            if (ThreadLocalRandom.current().nextDouble() < SCRAMBLE_CHANCE) {
                int scrambleIdx = ThreadLocalRandom.current().nextInt(1, POP_SIZE + 1);
                Chromosome chr = population.at(scrambleIdx);
                Mutation.mutate(chr, 0.33);
            }

            final Population popSnapLS = population;
            IntStream.range(0, POP_SIZE).parallel().forEach(i -> LocalSearch.greedyImprove(statement, popSnapLS.at(i + 1)));

            //evaluate P(T)
            final Population popSnapshot = population;
            Arrays.parallelSetAll(populationFitness, i ->
                    new Fitness(statement, popSnapshot.at(i + 1))
            );

            for (int i = 0; i < POP_SIZE; i++) {
                Fitness chromosomeFitness = populationFitness[i];
                if (chromosomeFitness.getFitness() > bestFitness.getFitness()) {
                    bestFitness = chromosomeFitness;
                    bestChromosome = popSnapLS.at(i + 1);
                }
            }

            System.out.println("Run: " + t + ". Current fitness: " + bestFitness.getFitness() + ". Max possible fitness: " + bestFitness.max() + ".");
        }

        System.out.println("Best fitness: " + bestFitness.getFitness() + ". Max possible fitness: " + bestFitness.max() + ".");

        // write assignment to file
        try (PrintWriter writer = new PrintWriter(new FileWriter("assignment.txt"))) {
            for (int i = 1; i <= bestChromosome.getGeneCount(); i++) {
                writer.println("X" + i + ": " + bestChromosome.getGene(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
