package dev.learnlab.exercise;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** L'orario in JSON, con il punteggio del risolutore e i conflitti contati dal validatore. */
@RestController
class TimetableApi {
    record LessonDto(String teacher, String schoolClass, String slot) {}

    record TimetableDto(String score, int conflicts, List<LessonDto> lessons) {}

    private final TimetableService service;

    TimetableApi(TimetableService service) {
        this.service = service;
    }

    @GetMapping("/api/timetable")
    TimetableDto timetable() {
        Timetable timetable = service.current();
        return new TimetableDto(
                String.valueOf(timetable.getScore()),
                TimetableValidator.conflicts(timetable.getLessons()),
                timetable.getLessons().stream()
                        .map(lesson -> new LessonDto(
                                lesson.getTeacher(),
                                lesson.getSchoolClass(),
                                lesson.getSlot() == null ? null : lesson.getSlot().label()))
                        .toList());
    }
}
