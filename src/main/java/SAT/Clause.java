package SAT;

import java.util.List;
import genetics.Chromosome;

public class Clause {
    private final int size;
    private final int[] literals;

    public Clause(List<Integer> literals) {
        size = literals.size();
        this.literals = literals.stream().mapToInt(i -> i).toArray();
    }

    public int getSize() {
        return size;
    }

    public int[] getLiterals() {
        return literals;//!!!DO NOT MODIFY
    }

    public boolean isSatisfied(Chromosome chromosome) {
        for (int literal : literals) {
            if (literal < 0) {
                if (chromosome.getGene(-literal)) return false;
            } else {
                if (!chromosome.getGene(literal)) return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < literals.length; i++) {
            int lit = literals[i];
            if (i > 0) sb.append(" ∨ "); // conjunction symbol
            if (lit < 0) sb.append("¬x").append(-lit);
            else sb.append("x").append(lit);
        }
        return sb.toString();
    }

}
