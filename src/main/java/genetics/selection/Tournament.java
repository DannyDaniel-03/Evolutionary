package genetics.selection;

import genetics.Chromosome;
import genetics.Fitness;
import genetics.Population;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Tournament extends Selection {
    public static double ELITE_RATE = 0.05;

    private static final int TOURNAMENT_SIZE = 4;
    private static final int WINNER_COUNT = 2;

    public Tournament(Population current_population, Fitness[] fitness) {
        super(current_population, fitness);
    }

    @Override
    public Population getNextPopulation() {
        int popSize = fitness.length;
        int geneCount = currentPopulation.at(1).getGeneCount();
        Population newPopulation = new Population(popSize, geneCount, false);

        int filled = 1;
        while (filled <= popSize) {
            List<Integer> participants = new ArrayList<>(TOURNAMENT_SIZE);
            for (int k = 0; k < TOURNAMENT_SIZE; k++) {
                int idx;
                do {
                    idx = ThreadLocalRandom.current().nextInt(1, popSize + 1);
                } while (participants.contains(idx));
                participants.add(idx);
            }

            participants.sort((a, b) -> Double.compare(
                    fitness[b - 1].getFitness(),
                    fitness[a - 1].getFitness()));

            for (int w = 0; w < WINNER_COUNT && filled <= popSize; w++) {
                int winnerIdx = participants.get(w);
                Chromosome winner = currentPopulation.at(winnerIdx);
                newPopulation.setChromosome(filled, winner);
                filled++;
            }
        }


        return newPopulation;
    }
}
