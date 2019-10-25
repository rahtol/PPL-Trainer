package com.becke.quizappfx;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class questionList {

    public static class score {
        int total;
        int open;
        int solved;
        int failed;
        score (int total, int open, int solved, int failed)
        {
            this.total = total;
            this.open = open;
            this.solved = solved;
            this.failed = failed;
        }
        public int total() {return total; };  // total number of questions existing in this category
        public int open() {return open; };  // number of questions that have not yet been asked
        public int solved() {return solved; }; // the number of correct answers
        public int failed() {return failed; }; // the number of question that have been answered false when presented for the first time
    }

    public static int modeSelectionStrategy = 0;  // how to select next question: in order (=0) or random (=1)
    public static int modeRepeatFailedQuestions = 0;  // repeat a question that has not been solved in this run
    public static int modeNumberOfQuestionsInRound = 5000; // upper limit on the number of questions to be asked in one round
    public static int modeDebug = 0;

    private XPathFactory xpathFactory;
    private List<questionWithState> questions;

    private int current;
    private int total;
    private int open;
    private int solved;
    private int failed;

    /*
        Initialize a new question list (or quiz).
        Select all questions with given category from the given xml document.
        Initially all questions found are open (i.e. unanswered), non failed or solved.
     */
    public questionList(Document doc, String category)
    {
        xpathFactory = XPathFactory.instance();
        XPathExpression<Object> expr = xpathFactory.compile(String.format("//question[@category='%s']", category));
        List<Object> questions0 = expr.evaluate(doc);
        questions = new ArrayList<questionWithState>();
        for (ListIterator<Object> i = questions0.listIterator(); i.hasNext();)
        {
            questionWithState q = new questionWithState((Element) i.next());
            questions.add(q);
        }
        total = Math.min(questions.size(), modeNumberOfQuestionsInRound);
        open = total;
        solved = 0;
        failed = 0;
        current = 0;

        if (modeSelectionStrategy > 0) {
            // scramble questions
            for (int i=0; i < questions.size(); i++) {
                int i1 = (int) Math.floor(Math.random()*questions.size());
                // swap (i, i1)
                questionWithState tmp = questions.get(i);
                questions.set(i, questions.get(i1));
                questions.set(i1, tmp);
            }

        }
    }

    public int getCurrent() {
        return current;
    }

    /*
        Return whether there are one or more open questions.
        An open question is a question that has not (or not correctly depending on quiz mode) been answered yet.
     */
    public boolean openQuestionAvailable()
    {
        return open > 0;
    }

    /*
        Return text of current question.
        Applicable only if there are open questions available.
     */
    public String getQuestionText()
    {
        return questions.get(current).text;
    }
    /*
        Returns the set of possible responses (or answers) to the current question
     */
    public String[] getResponse()
    {
        return questions.get(current).response;
    }

    /*
        Process an answer.
        Return 1 iff the answer was correct (currently 1 out of n only, no multiple selections).
        Return 0 if the answer was false and -1 if no answer was given at all.
        Update the score and select the next question, if any.
     */
    public int registerAnswer(int responseIdx)
    {
        if (responseIdx == 0) return -1;

        questionWithState currentQuestion = questions.get(current);
        currentQuestion.answer = responseIdx;
        boolean correct = currentQuestion.correctAnswer == currentQuestion.answer;

        if (currentQuestion.solved == 0 && currentQuestion.failed == 0) {
            this.open--;
        }
        if (correct && currentQuestion.solved == 0 && currentQuestion.failed == 0) {
            currentQuestion.solved = 1;
            this.solved++;
        }
        if (!correct) {
            if (currentQuestion.failed == 0) {
                this.failed++;
            }
            currentQuestion.failed++;
        }

        return (correct ? 1 : 0);
    }

    public int getAnswer ()
    {
        return questions.get(current).answer;
    }

    public int getCorrectAnswer ()
    {
        return questions.get(current).correctAnswer;
    }

    public boolean nextQuestion()
    {
        if (current < total-1) {
            current++;
            return true;
        }
        return false;
    }

    /*
        Select the previous question in list order.
        Do nothing if there is no predecessor.
     */
    public boolean prevQuestion()
    {
        if (current > 0) {
            current--;
            return true;
        }
        return false;
    }

    /*
        get the current score
     */
    public score getScore()
    {
        return new score (total, open, solved, failed);
    }

    public int getQuestionId() {
        return questions.get(current).id;
    }

    public boolean hasImage() {
        return questions.get(current).imageName != null;
    }

    public String getImageName() {
        return questions.get(current).imageName;
    }

    /*
        Debug helper
    */
    public void dumpstate () {
        if (modeDebug == 0) return;

        System.out.printf("current=%d, total=%d, open=%d, solved=%d, failed=%d\n", current, total, open, solved, failed);
        for (int i=0; i<total; i++){
            questionWithState q = questions.get(i);
            System.out.printf("i=%02d, id=%02d, answer=%d, correctAnswer=%d, solved=%d, failed=%d\n", i, q.id, q.answer, q.correctAnswer, q.solved, q.failed);
        }
    }

}
