package dev.learnlab.exercise;

import ai.timefold.solver.core.api.score.stream.test.ConstraintVerifier;
import org.junit.jupiter.api.Test;

/** I vincoli del risolutore, uno alla volta, su poche lezioni scritte a mano. */
class TimetableConstraintsTest {
    private static final TimeSlot FIRST = new TimeSlot("Lun", 1);
    private static final TimeSlot SECOND = new TimeSlot("Lun", 2);

    private final ConstraintVerifier<TimetableConstraints, Timetable> verifier =
            ConstraintVerifier.build(new TimetableConstraints(), Timetable.class, Lesson.class);

    private static Lesson lesson(long id, String teacher, String schoolClass, TimeSlot slot) {
        Lesson lesson = new Lesson(id, teacher, schoolClass);
        lesson.setSlot(slot);
        return lesson;
    }

    @Test
    void unDocenteInDueClassiNellaStessaOraEUnConflitto() {
        verifier.verifyThat(TimetableConstraints::teacherConflict)
                .given(lesson(1, "Rossi", "3A", FIRST), lesson(2, "Rossi", "3B", FIRST))
                .penalizesBy(1);
    }

    @Test
    void unaClasseConDueLezioniNellaStessaOraEUnConflitto() {
        verifier.verifyThat(TimetableConstraints::classConflict)
                .given(lesson(1, "Rossi", "3A", FIRST), lesson(2, "Bianchi", "3A", FIRST))
                .penalizesBy(1);
    }

    @Test
    void oreDiverseNonSonoUnConflitto() {
        verifier.verifyThat(TimetableConstraints::teacherConflict)
                .given(lesson(1, "Rossi", "3A", FIRST), lesson(2, "Rossi", "3B", SECOND))
                .penalizesBy(0);
        verifier.verifyThat(TimetableConstraints::classConflict)
                .given(lesson(1, "Rossi", "3A", FIRST), lesson(2, "Bianchi", "3A", SECOND))
                .penalizesBy(0);
    }
}
