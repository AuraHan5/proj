/* JUnit: QuestionTest — validates responses for different question types */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class QuestionTest {

    //Numeric questions
    /**
     * Comprova que una FreeNumberQuestion accepta un enter com a resposta valida.
     */
    @Test
    void testFreeNumberValidInteger() {
        Question q = new Question.FreeNumberQuestion("How old are you?");
        assertTrue(q.validarResposta(20));
    }

    /**
     * Comprova que una FreeNumberQuestion accepta un valor decimal com a resposta valida.
     */
    @Test
    void testFreeNumberValidDouble() {
        Question q = new Question.FreeNumberQuestion("How tall are you?");
        assertTrue(q.validarResposta(1.61));
    }

    /**
     * Comprova que una FreeNumberQuestion rebutja respostes no numeriques.
     */
    @Test
    void testFreeNumberInvalidString() {
        Question q = new Question.FreeNumberQuestion("What is your favorite animal?");
        assertFalse(q.validarResposta("dog"));
    }

    /**
     * Comprova que una FreeNumberQuestion rebutja una resposta nulla.
     */
    @Test
    void testFreeNumberNull() {
        Question q = new Question.FreeNumberQuestion("How old are you?");
        assertFalse(q.validarResposta(null));
    }


    //Single Choice Ordered
    /**
     * Comprova que es validi correctament un index valid per una SingleChoiceQuestionOrdered.
     */
    @Test
    void testSingleChoiceOrderedValidIndex() {
        Question q = new Question.SingleChoiceQuestionOrdered(
            "Select:",
            Arrays.asList("a","b","c")
        );
        assertTrue(q.validarResposta(1));
    }

    /**
     * Comprova que una SingleChoiceQuestionOrdered rebutgi un index negatiu.
     */
    @Test
    void testSingleChoiceOrderedNegativeIndex() {
        Question q = new Question.SingleChoiceQuestionOrdered(
            "Select:",
            Arrays.asList("a","b","c")
        );
        assertFalse(q.validarResposta(-1));
    }

    /**
     * Comprova que una SingleChoiceQuestionOrdered rebutgi un index superior al nombre d opcions.
     */
    @Test
    void testSingleChoiceOrderedIndexTooLarge() {
        Question q = new Question.SingleChoiceQuestionOrdered(
            "Select:",
            Arrays.asList("a","b","c")
        );
        assertFalse(q.validarResposta(5));
    }

    /**
     * Comprova que una SingleChoiceQuestionOrdered rebutgi una resposta que no sigui un enter.
     */
    @Test
    void testSingleChoiceOrderedInvalidType() {
        Question q = new Question.SingleChoiceQuestionOrdered(
            "Select:",
            Arrays.asList("a","b","c")
        );
        assertFalse(q.validarResposta("a"));
    }


    //Single Choice Unordered
    /**
     * Comprova que una SingleChoiceQuestionUnordered accepti una opcio valida.
     */
    @Test
    void testSingleChoiceUnorderedValidOption() {
        Question q = new Question.SingleChoiceQuestionUnordered(
            "Color?",
            Arrays.asList("red","green","blue")
        );
        assertTrue(q.validarResposta("red"));
    }

    /**
     * Comprova que una SingleChoiceQuestionUnordered rebutgi una opcio no existent.
     */
    @Test
    void testSingleChoiceUnorderedInvalidOption() {
        Question q = new Question.SingleChoiceQuestionUnordered(
            "Color?",
            Arrays.asList("red","green","blue")
        );
        assertFalse(q.validarResposta("yellow"));
    }

    /**
     * Comprova que una SingleChoiceQuestionUnordered rebutgi un tipus de resposta incorrecte.
     */
    @Test
    void testSingleChoiceUnorderedInvalidType() {
        Question q = new Question.SingleChoiceQuestionUnordered(
            "Color?",
            Arrays.asList("red","green","blue")
        );
        assertFalse(q.validarResposta(5));
    }


    //Multiple Choice
    /**
     * Comprova que MultipleChoiceQuestion accepti un array d indexs valids dins del limit permès.
     */
    @Test
    void testMultipleChoiceValid() {
        Question q = new Question.MultipleChoiceQuestion(
            "Pick:",
            Arrays.asList("a","b","c","d"),
            3
        );
        assertTrue(q.validarResposta(new int[]{0,2}));
    }

    /**
     * Comprova que FreeNumberQuestion rebutja valors extremadament grans.
     */
    @Test
    void testFreeNumberExtremeValue() {
        Question q = new Question.FreeNumberQuestion("Enter a number:");
        assertTrue(q.validarResposta(Double.MAX_VALUE));
        assertTrue(q.validarResposta(Double.MIN_VALUE));
    }

    /**
     * Comprova que FreeNumberQuestion rebutja NaN i Infinity.
     */
    @Test
    void testFreeNumberInvalidSpecialValues() {
        Question q = new Question.FreeNumberQuestion("Enter a number:");
        assertFalse(q.validarResposta(Double.NaN));
        assertFalse(q.validarResposta(Double.POSITIVE_INFINITY));
        assertFalse(q.validarResposta(Double.NEGATIVE_INFINITY));
    }

    /**
     * Comprova que SingleChoiceQuestionOrdered funciona amb una sola opcio.
     */
    @Test
    void testSingleChoiceOrderedOneOption() {
        Question q = new Question.SingleChoiceQuestionOrdered(
            "Only one:",
            Arrays.asList("only")
        );
        assertTrue(q.validarResposta(0));
        assertFalse(q.validarResposta(1));
    }

    /**
     * Comprova que SingleChoiceQuestionUnordered funciona amb opcions duplicades.
     */
    @Test
    void testSingleChoiceUnorderedDuplicates() {
        Question q = new Question.SingleChoiceQuestionUnordered(
            "Pick:",
            Arrays.asList("a","b","a")
        );
        assertTrue(q.validarResposta("a"));
    }

    /**
     * Comprova que MultipleChoiceQuestion accepta un array buit si el limit ho permet.
     */
    @Test
    void testMultipleChoiceEmptyArray() {
        Question q = new Question.MultipleChoiceQuestion(
            "Pick:",
            Arrays.asList("a","b","c"),
            3
        );
        assertTrue(q.validarResposta(new int[]{}));
    }

    /**
     * Comprova que MultipleChoiceQuestion funciona amb maxRespostes = 1.
     */
    @Test
    void testMultipleChoiceMaxOne() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Question.MultipleChoiceQuestion(
                "Pick one:",
                Arrays.asList("a","b","c"),
                1
            );
        });
    }

    /**
     * Comprova que MultipleChoiceQuestion funciona amb maxRespostes = 2.
     */
    @Test
    void testMultipleChoiceMaxTwo() {
        Question q = new Question.MultipleChoiceQuestion(
            "Pick two:",
            Arrays.asList("a","b","c"),
            2
        );
        assertTrue(q.validarResposta(new int[]{0,1}));
        assertFalse(q.validarResposta(new int[]{0,1,2}));
    }

    /**
     * Comprova que MultipleChoiceQuestion funciona amb maxRespostes = -1 (sense límit).
     */
    @Test
    void testMultipleChoiceNoLimit() {
        Question q = new Question.MultipleChoiceQuestion(
            "Pick any:",
            Arrays.asList("a","b","c"),
            -1
        );
        assertTrue(q.validarResposta(new int[]{0,1,2}));
    }

    /**
     * Comprova que MultipleChoiceQuestion rebutja maxRespostes = 0.
     */
    @Test
    void testMultipleChoiceInvalidMaxZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Question.MultipleChoiceQuestion(
                "Pick:",
                Arrays.asList("a","b","c"),
                0
            );
        });
    }

    /**
     * Comprova que MultipleChoiceQuestion rebutja maxRespostes > nombre d'opcions.
     */
    @Test
    void testMultipleChoiceInvalidMaxTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Question.MultipleChoiceQuestion(
                "Pick:",
                Arrays.asList("a","b","c"),
                5
            );
        });
    }

    /**
     * Comprova que FreeTextQuestion accepta strings buides.
     */
    @Test
    void testFreeTextEmpty() {
        Question q = new Question.FreeTextQuestion("Comment:");
        assertTrue(q.validarResposta(""));
    }

    /**
     * Comprova que FreeTextQuestion funciona amb strings llargues (fins a 1000 caràcters).
     */
    @Test
    void testFreeTextMaxLength() {
        Question q = new Question.FreeTextQuestion("Comment:");
        String maxText = "a".repeat(1000);
        assertTrue(q.validarResposta(maxText));
    }

    /**
     * Comprova que FreeTextQuestion funciona amb strings de longitud mitjana.
     */
    @Test
    void testFreeTextMediumLength() {
        Question q = new Question.FreeTextQuestion("Comment:");
        String mediumText = "a".repeat(500);
        assertTrue(q.validarResposta(mediumText));
    }

    /**
     * Comprova que MultipleChoiceQuestion rebutgi un array amb un index fora de rang.
     */
    @Test
    void testMultipleChoiceIndexOutOfRange() {
        Question q = new Question.MultipleChoiceQuestion(
            "Pick:",
            Arrays.asList("a","b","c"),
            3
        );
        assertFalse(q.validarResposta(new int[]{0,5}));
    }

    /**
     * Comprova que MultipleChoiceQuestion rebutgi un nombre excessiu de respostes.
     */
    @Test
    void testMultipleChoiceTooManyAnswers() {
        Question q = new Question.MultipleChoiceQuestion(
            "Pick:",
            Arrays.asList("a","b","c","d"),
            2
        );
        assertFalse(q.validarResposta(new int[]{0,1,2}));
    }

    /**
     * Comprova que MultipleChoiceQuestion rebutgi respostes que no siguin arrays d indexs.
     */
    @Test
    void testMultipleChoiceInvalidType() {
        Question q = new Question.MultipleChoiceQuestion(
            "Pick:",
            Arrays.asList("a","b","c"),
            3
        );
        assertFalse(q.validarResposta("a,b"));
    }


    //Free Text
    /**
     * Comprova que FreeTextQuestion accepti un text curt com a resposta valida.
     */
    @Test
    void testFreeTextShortTextValid() {
        Question q = new Question.FreeTextQuestion("What is your favorite color?");
        assertTrue(q.validarResposta("red"));
    }

    /**
     * Comprova que FreeTextQuestion accepti un enter com a resposta valida.
     */
    @Test
    void testFreeTextIntegerValid() {
        Question q = new Question.FreeTextQuestion("Age:");
        assertTrue(q.validarResposta(35));
    }

    /**
     * Comprova que FreeTextQuestion rebutgi una resposta nulla.
     */
    @Test
    void testFreeTextNullInvalid() {
        Question q = new Question.FreeTextQuestion("What is your favorite color?");
        assertFalse(q.validarResposta(null));
    }
}
