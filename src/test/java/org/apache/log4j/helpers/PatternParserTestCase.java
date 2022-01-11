/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j.helpers;


import static org.apache.log4j.TestContants.TARGET_OUTPUT_PREFIX;
import static org.apache.log4j.TestContants.TEST_WITNESS_PREFIX;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.util.Compare;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
   Test case for helpers/PatternParser.java. Tests the various 
   conversion patterns supported by PatternParser. This test
   class tests PatternParser via the PatternLayout class which
   uses it.
 */
public class PatternParserTestCase {
  
  static String OUTPUT_FILE   = TARGET_OUTPUT_PREFIX+"PatternParser";
  static String WITNESS_FILE  = TEST_WITNESS_PREFIX+"/PatternParser";

  static String msgPattern = "%m%n";
  
  Logger root; 
  Logger logger;


  @Before
  public void setUp() {
    root = Logger.getRootLogger();
    root.removeAllAppenders();
  }

  @After
  public void tearDown() {  
    root.getLoggerRepository().resetConfiguration();
  }

  /**
    Test case for MDC conversion pattern. */
  @Test
  public void mdcPattern() throws Exception {
    
    String mdcMsgPattern1 = "%m : %X%n";
    String mdcMsgPattern2 = "%m : %X{key1}%n";
    String mdcMsgPattern3 = "%m : %X{key2}%n";
    String mdcMsgPattern4 = "%m : %X{key3}%n";
    String mdcMsgPattern5 = "%m : %X{key1},%X{key2},%X{key3}%n";
    
    // set up appender
    PatternLayout layout = new PatternLayout(msgPattern);
    Appender appender = new FileAppender(layout, OUTPUT_FILE+"_mdc", false);
            
    // set appender on root and set level to debug
    root.addAppender(appender);
    root.setLevel(Level.DEBUG);
    
    // output starting message
    root.debug("starting mdc pattern test");
 
    layout.setConversionPattern(mdcMsgPattern1);
    root.debug("empty mdc, no key specified in pattern");
    
    layout.setConversionPattern(mdcMsgPattern2);
    root.debug("empty mdc, key1 in pattern");
    
    layout.setConversionPattern(mdcMsgPattern3);
    root.debug("empty mdc, key2 in pattern");
    
    layout.setConversionPattern(mdcMsgPattern4);
    root.debug("empty mdc, key3 in pattern");
    
    layout.setConversionPattern(mdcMsgPattern5);
    root.debug("empty mdc, key1, key2, and key3 in pattern");

    MDC.put("key1", "value1");
    MDC.put("key2", "value2");

    layout.setConversionPattern(mdcMsgPattern1);
    root.debug("filled mdc, no key specified in pattern");
    
    layout.setConversionPattern(mdcMsgPattern2);
    root.debug("filled mdc, key1 in pattern");
    
    layout.setConversionPattern(mdcMsgPattern3);
    root.debug("filled mdc, key2 in pattern");
    
    layout.setConversionPattern(mdcMsgPattern4);
    root.debug("filled mdc, key3 in pattern");
    
    layout.setConversionPattern(mdcMsgPattern5);
    root.debug("filled mdc, key1, key2, and key3 in pattern");

    MDC.remove("key1");
    MDC.remove("key2");

    layout.setConversionPattern(msgPattern);
    root.debug("finished mdc pattern test");

    assertTrue(Compare.compare(OUTPUT_FILE+"_mdc", WITNESS_FILE+"_mdc"));
  }

}