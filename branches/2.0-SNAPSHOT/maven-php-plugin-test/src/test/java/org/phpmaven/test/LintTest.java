/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.phpmaven.test;

import java.io.File;

import junit.framework.TestCase;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

/**
 * Testing the lint check.
 * 
 * @author Martin Eisengardt
 */
public class LintTest extends TestCase {
    
    /**
     * Setup test case.
     */
    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();
    }

    /**
     * tests the goal "compile" with simple error within the sources.
     *
     * @throws Exception
     */
    public void testSimpleFailure() throws Exception {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/org/phpmaven/test/projects/check-lint");
        
        final Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        
        // delete the pom from previous runs
        verifier.deleteArtifact( "org.phpmaven.test", "check-lint", "0.0.1", "pom" );
        verifier.deleteArtifact( "org.phpmaven.test", "check-lint", "0.0.1", "phar" );

        try
        {
            verifier.executeGoal("compile");
            verifier.resetStreams();
            fail("Build failure expected");
        }
        catch (VerificationException ex)
        {
            // we expect a verification exception
            verifier.verifyTextInLog("syntax check failure");
            verifier.resetStreams();
        }
    }

    /**
     * tests the goal "compile" with simple error in test class. Will expected to work because compile does not look at the test classes.
     *
     * @throws Exception
     */
    public void testCompileOkWithTestFailure() throws Exception {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/org/phpmaven/test/projects/check-linttests");
        
        final Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        
        // delete the pom from previous runs
        verifier.deleteArtifact( "org.phpmaven.test", "check-linttests", "0.0.1", "pom" );
        verifier.deleteArtifact( "org.phpmaven.test", "check-linttests", "0.0.1", "phar" );

        verifier.executeGoal("compile");
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

    /**
     * tests the goal "test-compile" with simple error in test class.
     *
     * @throws Exception
     */
    public void testSimpletestFailure() throws Exception {
        final File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/org/phpmaven/test/projects/check-linttests");
        
        final Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        
        // delete the pom from previous runs
        verifier.deleteArtifact( "org.phpmaven.test", "check-linttests", "0.0.1", "pom" );
        verifier.deleteArtifact( "org.phpmaven.test", "check-linttests", "0.0.1", "phar" );

        try
        {
            verifier.executeGoal("test-compile");
            verifier.resetStreams();
            fail("Build failure expected");
        }
        catch (VerificationException ex)
        {
            // we expect a verification exception
            verifier.verifyTextInLog("syntax check failure");
            verifier.resetStreams();
        }
    }

}
