package it.uniba.eculturetool.experience_lib.models;

import java.util.Set;

/**
 * Experience per la compatibilità contiene solo una domanda
 */
public class SingleQuestion extends TimedExperience {
    private String question;
    private Set<Answer> answers;
}
