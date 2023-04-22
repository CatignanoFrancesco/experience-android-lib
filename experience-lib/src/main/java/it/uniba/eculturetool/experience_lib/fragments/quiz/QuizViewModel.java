package it.uniba.eculturetool.experience_lib.fragments.quiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Set;

import it.uniba.eculturetool.experience_lib.models.Answer;
import it.uniba.eculturetool.experience_lib.models.Question;
import it.uniba.eculturetool.experience_lib.models.Quiz;

public class QuizViewModel extends ViewModel {

    private MutableLiveData<Quiz> quiz = new MutableLiveData<>(new Quiz());

    private Question activeQuestion = new Question();  // Question vuota, verrà utilizzata per salvare momentaneamente tutti i dati relativi alla domanda


    public LiveData<Quiz> getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        if(quiz == null) return;
        this.quiz.setValue(quiz);
    }

    public void addAnswer(Answer answer) {
        activeQuestion.getAnswers().add(answer);
    }

    public Set<Answer> getAnswers() {
        return activeQuestion.getAnswers();
    }

    public Question getActiveQuestion() {
        return activeQuestion;
    }

    public void setActiveQuestion(Question question) {
        activeQuestion = question;
    }

    public void saveQuestion(int previousPoints) {
        quiz.getValue().addQuestion(activeQuestion);
        quiz.getValue().setPoints(quiz.getValue().addPoints(activeQuestion.getPoints() - previousPoints));
        activeQuestion = new Question();    // A questo punto non c'è più la question attiva
    }

    private void trigger() {
        quiz.setValue(quiz.getValue());
    }
}
