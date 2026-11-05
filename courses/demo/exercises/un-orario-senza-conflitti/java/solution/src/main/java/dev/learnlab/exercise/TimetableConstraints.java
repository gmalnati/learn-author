package dev.learnlab.exercise;

import ai.timefold.solver.core.api.score.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;

/** I vincoli rigidi dell'orario: chi li viola rende l'orario non valido. */
public class TimetableConstraints implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {teacherConflict(factory), classConflict(factory)};
    }

    /** Un docente non insegna in due classi nella stessa ora. */
    Constraint teacherConflict(ConstraintFactory factory) {
        return factory.forEachUniquePair(
                        Lesson.class,
                        Joiners.equal(Lesson::getSlot),
                        Joiners.equal(Lesson::getTeacher))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Docente in due classi");
    }

    /** Una classe non ha due lezioni nella stessa ora. */
    Constraint classConflict(ConstraintFactory factory) {
        return factory.forEachUniquePair(
                        Lesson.class,
                        Joiners.equal(Lesson::getSlot),
                        Joiners.equal(Lesson::getSchoolClass))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Classe con due lezioni");
    }
}
