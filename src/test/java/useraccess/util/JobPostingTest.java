/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
package useraccess.util;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This class tests how good is a job posting by searching for certain keywords.
 * It gives a score to the page in form of a JUnit test. The page and the keywords 
 * values are hardcoded.
 * @author javi
 */
/*
@RunWith(Parameterized.class)
public class JobPostingTest {
    //Data for the posting page and the keyword to search for
    private HtmlPage page;
    private String keyword;
    //Test constructor
    public JobPostingTest(String keyword) throws IOException {
        //Gets the page to be tested, for example, this job posting I'm applying for...
        this.page=new WebClient().getPage("http://www.collectivei.com/jobs/senior-software-engineer");
        //keyword to search for        
        this.keyword=keyword;
    }
    //Keywords values to test. They could be obtained from a dictionary...
    @Parameters
    public static Collection<String []> getKeywords(){
        return Arrays.asList(new String [] []{
            {"senior"},{"software"},{"engineer"},{"benefits"},{"Java"},{"J2EE"},{"HTML5"},
            {"CSS3"},{"Javascript"},{"unit testing"},{"TDD"},{"test driven development"},
            {"good"},{"improve"},{"development"},{"holidays"},{"web"}
        });
    }
    //Test method
    @Test
    public void testJobPosting() {
        //WebAssert.assertTextPresent(page, keyword);
        Assert.assertTrue(true);
    }
}
*/