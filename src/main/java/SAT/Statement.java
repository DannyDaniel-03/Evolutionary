package SAT;

import genetics.Chromosome;

import java.util.List;

public class Statement {
    private final Clause[] clauses;
    private final int length;
    private final double[] weight;

    private void checkIndex(int index) {
        if (index < 1 || index > length) {
            throw new IndexOutOfBoundsException("Clause index " + index + " is out of bounds (1-" + (length) + ")");
        }
    }

    public Statement(List<Clause> clauses) {
        this.clauses = clauses.toArray(Clause[]::new);
        this.length = clauses.size();
        weight = new double[length];
        for (int i = 0; i < length; i++) {
            weight[i] = clauses.get(i).getSize();
        }
    }

    public int getLength() {
        return length;
    }

    public Clause at(int index) {
        checkIndex(index--);
        return clauses[index];
    }

    public boolean isSatisfied(Chromosome chromosome) {
        for (Clause clause : clauses) {
            if (!clause.isSatisfied(chromosome)) return false;
        }
        return true;
    }

    public double getClauseWeight(int index) {
        checkIndex(index--);
        return weight[index];
    }

    public Clause getClause(int index) {
        checkIndex(index--);
        return clauses[index];
    }

    public void incWeight(int index) {
        checkIndex(index--);
        weight[index] += 0.2;
    }

    public void setClauseWeight(int index, double newWeight) {
        checkIndex(index--);
        weight[index] = newWeight;
    }

    public int getTrueClauses(Chromosome chromosome) {
        int count = 0;
        for (Clause clause : clauses) {
            if (clause.isSatisfied(chromosome)) count++;
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Statement:\n");
        for (int i = 1; i <= length; i++) {
            sb.append("  ").append(i).append(": ").append(at(i)).append("\n");
        }
        return sb.toString();
    }

}
