package it.uniba.eculturetool.experience_lib.fragments.singlequestion;

import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.Set;

import it.uniba.eculturetool.experience_lib.models.Answer;
import it.uniba.eculturetool.experience_lib.models.SingleQuestion;

public class SingleQuestionViewModel extends ViewModel {
    private SingleQuestion singleQuestion = new SingleQuestion();

    public SingleQuestion getSingleQuestion() {
        return singleQuestion;
    }

    public void setSingleQuestion(SingleQuestion singleQuestion) {
        this.singleQuestion = singleQuestion;
    }

    public void addAnswer(Answer answer) {

        if(singleQuestion.getAnswers() == null) {
            singleQuestion.setAnswers(new HashSet<>());
        }

        singleQuestion.getAnswers().add(answer);
    }
}
