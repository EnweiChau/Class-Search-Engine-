import edu.uwb.css143.service.Indexer;
import edu.uwb.css143.service.IndexerImpl;
import edu.uwb.css143.service.Searcher;
import edu.uwb.css143.service.SearcherImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private Indexer indexer;
    private Searcher searcher;

    @Before
    public void setUp() {
        indexer = new IndexerImpl();
        searcher = new SearcherImpl();
    }

    @Test
    public void testSearch() {
        List<TestCase> cases = getTestCase();
        for (TestCase testCase : cases) {
            List<Integer> actual = searcher.search(
                    testCase.target,
                    indexer.createIndex(testCase.documents)
            );
            assertEquals(testCase.expect, actual);
        }
    }

    @Test
    public void testNaiveSearch() {
        List<TestCase> cases = getTestCase();
        for (TestCase testCase : cases) {
            List<Integer> actual = searcher.search(testCase.target, testCase.documents);
            assertEquals(String.format("failed case %s", testCase), testCase.expect, actual);
        }
    }
    @Test
    public void testEnweiSearch() {
        List<TestCase> cases = EnweiTest();
        for (TestCase testCase : cases){
            List<Integer> actual = searcher.search(testCase.target, indexer.createIndex(testCase.documents));
            assertEquals(testCase.expect, actual);
        }
    }
//Extra Credit
    private List<TestCase> EnweiTest() {
        List<String> documents = Arrays.asList(
                "Lebron James from the NBA will give a guest lecture is on Oct 29th",
                "Please welcome our guest speaker Lebron our 29th speaker on the 29th isn't that funny",
                "Lebron will start by explaining machine learning algorithms and how he uses it for NBA training"
        );


        List<TestCase> testCases = new ArrayList<>(Arrays.asList(
                new TestCase(
                        documents,
                        "Lebron",
                        new ArrayList<>(Arrays.asList(0, 1, 2))
                ),
                new TestCase(
                        documents,
                        "machine learning",
                        new ArrayList<>(Arrays.asList(2))
                ),
                new TestCase(
                        documents,
                        "29th",
                        new ArrayList<>(Arrays.asList(0, 1))
                ),
                new TestCase(
                        documents,
                        "NBA",
                        new ArrayList<>(Arrays.asList(0, 2))
                ),
                new TestCase(
                        documents,
                        "Jeremy Lin",
                        Util.emptyResult()
                )
        ));

        return testCases;
    }

    private List<TestCase> getTestCase() {
        List<String> documents = Util.getDocumentsForIntTest();

        List<TestCase> testCases = new ArrayList<>(Arrays.asList(
                new TestCase(
                        documents,
                        "",
                        Util.emptyResult()
                ),
                new TestCase(
                        documents,
                        "hello world",
                        new ArrayList<>(Arrays.asList(0, 1, 6))
                ),
                new TestCase(
                        documents,
                        "llo wor",
                        Util.emptyResult()
                ),
                new TestCase(
                        documents,
                        "wor",
                        Util.emptyResult()
                ),
                new TestCase(
                        documents,
                        "hello",
                        new ArrayList<>(Arrays.asList(0, 1, 2, 4, 5, 6))
                ),
                new TestCase(
                        documents,
                        "just world",
                        new ArrayList<>(Arrays.asList(0))
                ),
                new TestCase(
                        documents,
                        "sunday",
                        new ArrayList<>(Arrays.asList(6))
                ),
                new TestCase(
                        documents,
                        "hello world fun",
                        new ArrayList<>(Arrays.asList(6))
                ),
                new TestCase(
                        documents,
                        "world world fun",
                        Util.emptyResult()
                ),
                new TestCase(
                        documents,
                        "office",
                        Util.emptyResult()
                ),
                new TestCase(
                        documents,
                        "ryan murphy",
                        Util.emptyResult()
                ),
                new TestCase(
                        documents,
                        "new macbook",
                        new ArrayList<>(Arrays.asList(7))
                ),
                new TestCase(
                        documents,
                        "is awesome",
                        new ArrayList<>(Arrays.asList(7))
                )
        ));

        return testCases;
    }

    private class TestCase {
        private List<String> documents;
        private String target;
        private List<Integer> expect;

        public TestCase(List<String> documents, String target, List<Integer> expect) {
            this.documents = documents;
            this.target = target;
            this.expect = expect;
        }

        @Override
        public String toString() {
            return "TestCase{" +
                    "documents=" + documents +
                    ", target='" + target + '\'' +
                    ", expect=" + expect +
                    '}';
        }
    }
}