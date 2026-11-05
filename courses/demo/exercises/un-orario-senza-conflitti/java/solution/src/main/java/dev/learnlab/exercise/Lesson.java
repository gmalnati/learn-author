package dev.learnlab.exercise;

import ai.timefold.solver.core.api.domain.common.PlanningId;
import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;

/** Una lezione di un docente in una classe: il risolutore sceglie la sua ora. */
@PlanningEntity
public class Lesson {
    @PlanningId
    private Long id;

    private String teacher;
    private String schoolClass;

    @PlanningVariable
    private TimeSlot slot;

    public Lesson() {}

    public Lesson(long id, String teacher, String schoolClass) {
        this.id = id;
        this.teacher = teacher;
        this.schoolClass = schoolClass;
    }

    public Long getId() {
        return id;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public void setSlot(TimeSlot slot) {
        this.slot = slot;
    }
}
