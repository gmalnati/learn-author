package dev.learnlab.exercise;

import ai.timefold.solver.core.api.score.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;

/** I vincoli rigidi dell'orario: chi li viola rende l'orario non valido. */
public class TimetableConstraints implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {teacherConflict(factory), classConflict(factory)};
    }

    /** Un docente non insegna in due classi nella stessa ora. */
    Constraint teacherConflict(ConstraintFactory factory) {
        // Da fare: penalizza ogni coppia di lezioni con lo stesso docente nella stessa ora. Per ora
        // il vincolo non penalizza niente, e il risolutore si accontenta del primo orario che trova.
        return factory.forEach(Lesson.class)
                .filter(lesson -> false)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Docente in due classi");
    }

    /** Una classe non ha due lezioni nella stessa ora. */
    Constraint classConflict(ConstraintFactory factory) {
        // Da fare: penalizza ogni coppia di lezioni della stessa classe nella stessa ora.
        return factory.forEach(Lesson.class)
                .filter(lesson -> false)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Classe con due lezioni");
    }
}
