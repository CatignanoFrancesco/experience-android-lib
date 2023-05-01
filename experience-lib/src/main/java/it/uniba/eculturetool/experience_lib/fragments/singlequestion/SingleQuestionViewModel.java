package it.uniba.eculturetool.experience_lib.fragments.singlequestion;

import androidx.lifecycle.ViewModel;

import it.uniba.eculturetool.experience_lib.models.SingleQuestion;

public class SingleQuestionViewModel extends ViewModel {
    private SingleQuestion singleQuestion = new SingleQuestion();

    public SingleQuestion getSingleQuestion() {
        return singleQuestion;
    }

    public void setSingleQuestion(SingleQuestion singleQuestion) {
        this.singleQuestion = singleQuestion;
    }
}
