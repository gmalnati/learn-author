package dev.learnlab.exercise;

import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Calcola l'orario di una scuola piccolissima: due docenti, due classi, due ore. Il seme fisso rende
 * il risultato sempre uguale, e il risolutore si ferma appena trova un orario senza penalità.
 */
@Service
public class TimetableService {
    static final List<String> CLASSES = List.of("3A", "3B");
    static final List<TimeSlot> SLOTS = List.of(new TimeSlot("Lun", 1), new TimeSlot("Lun", 2));

    private final SolverFactory<Timetable> solverFactory = SolverFactory.create(new SolverConfig()
            .withSolutionClass(Timetable.class)
            .withEntityClasses(Lesson.class)
            .withConstraintProviderClass(TimetableConstraints.class)
            .withRandomSeed(42L)
            .withTerminationConfig(new TerminationConfig()
                    .withBestScoreLimit("0hard/0soft")
                    .withSpentLimit(Duration.ofSeconds(5))));

    private Timetable current = solve();

    public synchronized Timetable solve() {
        List<Lesson> lessons = List.of(
                new Lesson(1, "Rossi", "3A"),
                new Lesson(2, "Rossi", "3B"),
                new Lesson(3, "Bianchi", "3A"),
                new Lesson(4, "Bianchi", "3B"));
        current = solverFactory.buildSolver().solve(new Timetable(SLOTS, lessons));
        return current;
    }

    public synchronized Timetable current() {
        return current;
    }

    public TimetableView view(Timetable timetable) {
        List<TimetableView.Row> rows = SLOTS.stream()
                .map(slot -> new TimetableView.Row(
                        slot.label(),
                        CLASSES.stream().map(schoolClass -> cell(timetable, slot, schoolClass)).toList()))
                .toList();
        return new TimetableView(CLASSES, rows, TimetableValidator.conflicts(timetable.getLessons()));
    }

    private static String cell(Timetable timetable, TimeSlot slot, String schoolClass) {
        String teachers = timetable.getLessons().stream()
                .filter(lesson -> slot.equals(lesson.getSlot()) && schoolClass.equals(lesson.getSchoolClass()))
                .map(Lesson::getTeacher)
                .collect(Collectors.joining(", "));
        return teachers.isEmpty() ? "—" : teachers;
    }
}
