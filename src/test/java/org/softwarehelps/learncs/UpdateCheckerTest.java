/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.softwarehelps.learncs;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

/**
 *
 * @author Peter Dobson
 */
public class UpdateCheckerTest extends TestCase {
    
    UpdateChecker instance;
    
    public UpdateCheckerTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instance = new UpdateChecker(null, 
                "http://softwarehelps.org/download/learnCS.version",
                "http://softwarehelps.org/wiki/index.php?title="
                + "Download_CIS_5_Lab_Software");
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetNewVersion() {
        System.out.println("getNewVersion");
        String expResult = "1.0.1";
        String result = instance.getNewVersion();
        assertTrue(expResult != null && !expResult.isEmpty());
    }

    public void testCompareVersions() {
        System.out.println("compareVersions");
        String a = "1.0.1";
        String b = "1.0.1";
        int expResult = 0;
        int result = instance.compareVersions(a, b);
        assertEquals(expResult, result);
        b = "1.0.0";        
        result = instance.compareVersions(a, b);
        assertTrue(result > 0);
        b = "1.0.2";        
        result = instance.compareVersions(a, b);
        assertTrue(result < 0);
        b = "1.0.0-SNAPSHOT";
        result = instance.compareVersions(a, b);
        assertTrue(result > 0);
    }    

    /**
     * Test of getThisVersion method, of class UpdateChecker.
     */
    public void testGetThisVersion() {
        System.out.println("getThisVersion");
        String result = instance.getThisVersion();
        assertTrue(result != null && !result.isEmpty());        
    }     
}
