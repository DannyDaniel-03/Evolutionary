package genetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Population {
    private final Chromosome[] chromosomes;
    private final int size;

    private void checkIndex(int index) {
        if (index < 1 || index > size) {
            throw new IndexOutOfBoundsException("Chromosome index " + index + " is out of bounds (1-" + (size) + ")");
        }
    }

    public Population(int size, int geneCount, boolean random) {
        this.size = size;
        chromosomes = new Chromosome[size];
        for (int i = 0; i < size; i++)
            chromosomes[i] = new Chromosome(geneCount, random);
    }

    public int getSize() {
        return size;
    }

    public Chromosome at(int index) {
        checkIndex(index--);
        return chromosomes[index];
    }

    public void setChromosome(int index, Chromosome chromosome) {
        checkIndex(index--);
        for (int i = 1; i <= chromosome.getGeneCount(); i++) {
            chromosomes[index].setGene(i, chromosome.getGene(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Population:\n");
        for (int i = 1; i <= size; i++) {
            sb.append("  ").append(i).append(": ").append(at(i)).append("\n");
        }
        return sb.toString();
    }

    public void crossover(int idx1, int idx2, int cuts, boolean uniform) {
        checkIndex(idx1);
        checkIndex(idx2);
        if (idx1 == idx2) {
            throw new IllegalArgumentException("Chromosome indices must be different.");
        }

        Chromosome parent1 = at(idx1);
        Chromosome parent2 = at(idx2);
        int geneCount = parent1.getGeneCount();

        if (cuts <= 0 || cuts >= geneCount) {
            throw new IllegalArgumentException(
                    "Number of cuts must be >0 and < " + geneCount
            );
        }

        if (!uniform) {
            int maxCuts = (int) Math.floor(Math.sqrt(geneCount));
            if (cuts > maxCuts)
                throw new IllegalArgumentException(
                    "Too many random cut points requested: " + cuts +
                    ". To maintain genetic integrity and avoid excessive fragmentation, " +
                    "the number of random cuts must be ≤ √geneCount ≈ " + maxCuts +
                    " (geneCount = " + geneCount + ")."
                );
        }


        List<Integer> cutPoints = new ArrayList<>(cuts);
        if (uniform) {
            for (int i = 1; i <= cuts; i++) {
                cutPoints.add((i * geneCount) / (cuts + 1));
            }
        } else {
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            while (cutPoints.size() < cuts) {
                int cp = rnd.nextInt(1, geneCount);
                if (!cutPoints.contains(cp)) {
                    cutPoints.add(cp);
                }
            }
            Collections.sort(cutPoints);
        }

        Chromosome offspring1 = new Chromosome(geneCount, false);
        Chromosome offspring2 = new Chromosome(geneCount, false);
        boolean swap = false;
        int prevCut = 0;

        for (int cp : cutPoints) {
            HelperCrossover(parent1, parent2, offspring1, offspring2, swap, prevCut, cp);
            swap = !swap;
            prevCut = cp;
        }

        HelperCrossover(parent1, parent2, offspring1, offspring2, swap, prevCut, geneCount);

        chromosomes[idx1 - 1] = offspring1;
        chromosomes[idx2 - 1] = offspring2;
    }

    private void HelperCrossover(Chromosome parent1, Chromosome parent2, Chromosome offspring1, Chromosome offspring2, boolean swap, int prevCut, int cp) {
        for (int i = prevCut + 1; i <= cp; i++) {
            boolean g1 = swap ? parent2.getGene(i) : parent1.getGene(i);
            boolean g2 = swap ? parent1.getGene(i) : parent2.getGene(i);
            offspring1.setGene(i, g1);
            offspring2.setGene(i, g2);
        }
    }
}
