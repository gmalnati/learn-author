package dev.learnlab.exercise;

import java.util.List;

/** L'orario come lo mostra la pagina: una riga per ora, una colonna per classe. */
public record TimetableView(List<String> classes, List<Row> rows, int conflicts) {
    public record Row(String slot, List<String> cells) {}
}
