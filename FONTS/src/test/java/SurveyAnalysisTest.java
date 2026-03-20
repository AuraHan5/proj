/* JUnit: SurveyAnalysisTest — small integration tests around Survey.SurveyAnalysis */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

/**
 * Proves d'integració petita per a Survey.SurveyAnalysis.
 */
public class SurveyAnalysisTest {

    /**
     * Comprova que SurveyAnalysis retorna un resultat sense excepcions amb dades petites i completes.
     */
    @Test
    public void testSurveyAnalysisSmallDataset() throws Exception {
        Survey survey = new Survey(1, "desc", "title");

        // Preguntes: numèrica i opció única desordenada
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question.FreeNumberQuestion("Q1"));
        ArrayList<String> opts = new ArrayList<>();
        opts.add("A");
        opts.add("B");
        questions.add(new Question.SingleChoiceQuestionUnordered("Q2", opts));
        survey.addQuestions(questions);

        // Respostes: dues mostres
        Answer a1 = new Answer();
        a1.setAnswer(new Object[]{1.0, "A"});
        Answer a2 = new Answer();
        a2.setAnswer(new Object[]{2.0, "B"});
        survey.getAnswers().add(a1);
        survey.getAnswers().add(a2);

        String result = survey.SurveyAnalysis(2);
        assertNotNull(result);
        assertTrue(result.contains("Analysis completed"));
    }

    /**
     * Comprova que SurveyAnalysis no falla quan una pregunta queda sense resposta (s'omple per defecte).
     */
    @Test
    public void testSurveyAnalysisHandlesMissingAnswers() throws Exception {
        Survey survey = new Survey(2, "desc", "title");

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question.FreeNumberQuestion("Q1"));
        ArrayList<String> opts = new ArrayList<>();
        opts.add("A");
        opts.add("B");
        questions.add(new Question.SingleChoiceQuestionUnordered("Q2", opts));
        survey.addQuestions(questions);

        // Només una resposta plena, l'altra parcial (nulls)
        Answer a1 = new Answer();
        a1.setAnswer(new Object[]{1.0, "A"});
        Answer a2 = new Answer();
        a2.setAnswer(new Object[]{null, null});
        survey.getAnswers().add(a1);
        survey.getAnswers().add(a2);

        String result = survey.SurveyAnalysis(2);
        assertNotNull(result);
        assertTrue(result.contains("Analysis completed"));
    }
}
