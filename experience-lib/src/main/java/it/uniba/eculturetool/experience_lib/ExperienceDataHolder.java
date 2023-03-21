package it.uniba.eculturetool.experience_lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.uniba.eculturetool.experience_lib.models.Experience;

public class ExperienceDataHolder {
    private static ExperienceDataHolder experienceDataHolder;

    private final Map<Object, Set<Experience>> operaExperiences = new HashMap<>();

    private ExperienceDataHolder() {}

    public void addExperienceToOpera(Object operaId, Experience experience) {
        Set<Experience> experiences;

        if(operaExperiences.containsKey(operaId)) {
            experiences = operaExperiences.get(operaId);
        } else {
            experiences = new HashSet<>();
            operaExperiences.put(operaId, experiences);
        }

        experiences.add(experience);
    }

    public void addAllExperienceToOpera(String operaId, Set<Experience> experiences) {
        if(operaExperiences.containsKey(operaId)) {
            operaExperiences.get(operaId).addAll(experiences);
        } else {
            operaExperiences.put(operaId, experiences);
        }
    }

    public Set<Experience> getExperienceByOperaId(Object operaId) {
        return operaExperiences.get(operaId);
    }

    public void deleteExperience(Object operaId, Experience experience) {
        if(operaExperiences.containsKey(operaId)) {
            operaExperiences.get(operaId).remove(experience);
        }
    }

    public void clean() {
        operaExperiences.clear();
        experienceDataHolder = null;
    }

    public static ExperienceDataHolder getInstance() {
        if(experienceDataHolder == null) experienceDataHolder = new ExperienceDataHolder();

        return experienceDataHolder;
    }
}
