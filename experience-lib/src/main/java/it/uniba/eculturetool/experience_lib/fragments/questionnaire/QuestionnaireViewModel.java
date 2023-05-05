package it.uniba.eculturetool.experience_lib.fragments.questionnaire;

import androidx.lifecycle.ViewModel;

import it.uniba.eculturetool.experience_lib.models.Questionnaire;

public class QuestionnaireViewModel extends ViewModel {
    private Questionnaire questionnaire = new Questionnaire();

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }
}
