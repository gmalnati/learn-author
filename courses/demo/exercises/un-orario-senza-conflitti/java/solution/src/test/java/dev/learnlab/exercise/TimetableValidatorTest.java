package dev.learnlab.exercise;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

/** Il validatore conta i conflitti senza usare il risolutore. */
class TimetableValidatorTest {
    private static Lesson lesson(long id, String teacher, String schoolClass, TimeSlot slot) {
        Lesson lesson = new Lesson(id, teacher, schoolClass);
        lesson.setSlot(slot);
        return lesson;
    }

    @Test
    void contaIConflittiDiDocentiEClassi() {
        TimeSlot first = new TimeSlot("Lun", 1);
        List<Lesson> allTogether = List.of(
                lesson(1, "Rossi", "3A", first),
                lesson(2, "Rossi", "3B", first),
                lesson(3, "Bianchi", "3A", first),
                lesson(4, "Bianchi", "3B", first));
        assertThat(TimetableValidator.conflicts(allTogether)).isEqualTo(4);
    }

    @Test
    void unOrarioBenFattoNonHaConflitti() {
        TimeSlot first = new TimeSlot("Lun", 1);
        TimeSlot second = new TimeSlot("Lun", 2);
        List<Lesson> good = List.of(
                lesson(1, "Rossi", "3A", first),
                lesson(2, "Rossi", "3B", second),
                lesson(3, "Bianchi", "3A", second),
                lesson(4, "Bianchi", "3B", first));
        assertThat(TimetableValidator.conflicts(good)).isZero();
    }
}
