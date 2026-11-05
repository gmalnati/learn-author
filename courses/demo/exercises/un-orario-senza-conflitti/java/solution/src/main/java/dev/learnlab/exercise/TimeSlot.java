package dev.learnlab.exercise;

/** Un'ora della settimana: il giorno e la posizione nella giornata. */
public record TimeSlot(String day, int hour) {
    public String label() {
        return day + " " + hour;
    }
}
