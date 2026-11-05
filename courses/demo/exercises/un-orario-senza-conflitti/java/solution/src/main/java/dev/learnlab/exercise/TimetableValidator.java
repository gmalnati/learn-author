package dev.learnlab.exercise;

import java.util.List;
import java.util.Objects;

/**
 * Il controllo dell'orario, indipendente dal risolutore: conta le coppie di lezioni nella stessa ora
 * con lo stesso docente o con la stessa classe. Se un vincolo del risolutore è scritto male, il
 * risolutore ottimizza la regola sbagliata, e questo controllo se ne accorge.
 */
public final class TimetableValidator {
    private TimetableValidator() {}

    public static int conflicts(List<Lesson> lessons) {
        int conflicts = 0;
        for (int i = 0; i < lessons.size(); i++) {
            for (int j = i + 1; j < lessons.size(); j++) {
                Lesson a = lessons.get(i);
                Lesson b = lessons.get(j);
                if (a.getSlot() == null || !Objects.equals(a.getSlot(), b.getSlot())) continue;
                if (a.getTeacher().equals(b.getTeacher())) conflicts++;
                if (a.getSchoolClass().equals(b.getSchoolClass())) conflicts++;
            }
        }
        return conflicts;
    }
}
