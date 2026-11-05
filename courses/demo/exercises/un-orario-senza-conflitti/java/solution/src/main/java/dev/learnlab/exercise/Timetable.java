package dev.learnlab.exercise;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.HardSoftScore;
import java.util.List;

/** Le ore della settimana e le lezioni da sistemare, con il punteggio dell'orario. */
@PlanningSolution
public class Timetable {
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<TimeSlot> slots;

    @PlanningEntityCollectionProperty
    private List<Lesson> lessons;

    @PlanningScore
    private HardSoftScore score;

    public Timetable() {}

    public Timetable(List<TimeSlot> slots, List<Lesson> lessons) {
        this.slots = slots;
        this.lessons = lessons;
    }

    public List<TimeSlot> getSlots() {
        return slots;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public HardSoftScore getScore() {
        return score;
    }

    // Timefold 2 scrive il punteggio con un setter pubblico: un campo privato senza setter non basta.
    public void setScore(HardSoftScore score) {
        this.score = score;
    }
}
