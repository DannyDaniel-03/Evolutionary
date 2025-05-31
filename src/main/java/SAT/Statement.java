package SAT;

import genetics.Chromosome;

import java.util.List;

public class Statement {
    private final Clause[] clauses;
    private final int length;

    private void checkIndex(int index) {
        if (index < 1 || index > length) {
            throw new IndexOutOfBoundsException("Clause index " + index + " is out of bounds (1-" + (length) + ")");
        }
    }

    public Statement(List<Clause> clauses) {
        this.clauses = clauses.toArray(Clause[]::new);
        this.length = clauses.size();
    }

    public int getLength() {
        return length;
    }

    public Clause at(int index) {
        checkIndex(index--);
        return clauses[index];
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
