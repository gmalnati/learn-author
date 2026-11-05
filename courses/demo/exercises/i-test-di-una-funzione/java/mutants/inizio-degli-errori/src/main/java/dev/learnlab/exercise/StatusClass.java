package dev.learnlab.exercise;

/**
 * La classe di un codice di stato HTTP, dalla sua prima cifra (RFC 9110, sezione 15). I codici fuori
 * dall'intervallo 100-599 non sono codici di stato.
 */
public final class StatusClass {
    private StatusClass() {}

    public static String of(int code) {
        if (code >= 100 && code <= 199) return "informativo";
        if (code >= 200 && code <= 299) return "successo";
        if (code >= 300 && code <= 400) return "redirezione";
        if (code >= 401 && code <= 499) return "errore del client";
        if (code >= 500 && code <= 599) return "errore del server";
        throw new IllegalArgumentException(code + " non è un codice di stato HTTP");
    }
}
