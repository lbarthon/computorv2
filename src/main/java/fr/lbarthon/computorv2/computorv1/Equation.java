package fr.lbarthon.computorv2.computorv1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Equation class.
 * Each equation has two parts, left and right part.
 * Each part has an array of entries, containing the power of the unknown number (x),
 * and the number by whom he's multiplied.
 */
public class Equation {

    @Getter
    private Part leftPart;
    @Getter
    private Part rightPart;
    @Getter
    private boolean isReduced;

    public Equation() {
        this.leftPart = new Part();
        this.rightPart = new Part();
        this.isReduced = false;
    }

    public long getDegree() {
        // Default entry returned if one of the list is empty
        Entry defaultEntry = new Entry(0, 0);

        Entry leftMax = leftPart.getEntries().stream()
                .max(Comparator.comparingLong(Entry::getPower))
                .orElse(defaultEntry);
        Entry rightMax = rightPart.getEntries().stream()
                .max(Comparator.comparingLong(Entry::getPower))
                .orElse(defaultEntry);

        return Math.max(leftMax.getPower(), rightMax.getPower());
    }

    /**
     * Changes the equation to it's reduced form.
     * The goal is to get a right part as small as possible (always should be 0)
     */
    public void reduce() {
        this.rightPart.getEntries().forEach(rightEntry -> {
            Entry leftEntry = this.leftPart.getEntryByPower(rightEntry.getPower());
            if (leftEntry != null) {
                leftEntry.setNbr(leftEntry.getNbr() - rightEntry.getNbr());
                if (leftEntry.getNbr() == 0) {
                    this.leftPart.removeEntry(leftEntry);
                }
            } else if (rightEntry.getNbr() != 0) {
                this.leftPart.addEntry(rightEntry.getPower(), -rightEntry.getNbr());
            }
        });

        this.rightPart.getEntries().clear();
        this.isReduced = true;
    }

    public String toString() {
        return this.leftPart.toString() + " = " + this.rightPart.toString();
    }

    @NoArgsConstructor
    public class Part {
        @Getter
        private List<Entry> entries = new ArrayList<>();

        public void addEntry(Entry e) {
            this.entries.add(e);
        }

        public void removeEntry(Entry e) {
            this.entries.remove(e);
        }

        public void addEntry(long power, double nbr) {
            Entry foundEntry = this.getEntryByPower(power);
            if (foundEntry != null) {
                foundEntry.setNbr(foundEntry.getNbr() + nbr);
            } else {
                this.addEntry(new Entry(power, nbr));
            }
        }

        public Entry getEntryByPower(long power) {
            return this.entries.stream().filter(e -> e.getPower() == power).findFirst().orElse(null);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();

            this.entries.sort(Comparator.comparingLong(Entry::getPower));
            this.entries.forEach(entry -> {
                // If it isn't empty, adds a space
                if (builder.length() > 0) {
                    builder.append(' ');
                }

                if (entry.getNbr() < 0) {
                    builder.append("- ");
                    builder.append(-entry.getNbr());
                } else {
                    if (builder.length() > 0) {
                        builder.append("+ ");
                    }
                    builder.append(entry.getNbr());
                }

                builder.append(" * X^");
                builder.append(entry.getPower());
            });

            // If the part is empty, displays only a 0
            if (builder.length() == 0) {
                builder.append(0);
            }

            return builder.toString();
        }
    }

    @Data
    @AllArgsConstructor
    public class Entry {
        private long power;
        private double nbr;

        public double getNbr() {
            return nbr == -0 ? -nbr : nbr;
        }
    }
}
