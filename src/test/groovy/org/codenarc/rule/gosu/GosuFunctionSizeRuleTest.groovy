/*
 * Copyright 2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codenarc.rule.gosu

import org.codenarc.source.SourceString
import org.codenarc.util.gosu.GosuUtil

/**
 * Max function line length tests
 *
 */
class GosuFunctionSizeRuleTest extends GroovyTestCase {

    void testApplyToNoViolations() {

        def code = """
            function square( n : Number ) : Number {
              return n * n
              /*
                  * if () {
                  * }
                  */
            }
            
            //function square( n : Number ) : Number {
            //  return n * n
            //}
            
            /* 
            function anotherOne( n : Number ) : Number {
              //return n * n
              /*
              * if () {
              * }
              */
            }
            */
            
            function anotherOne( n : Number ) : Number {
              return n * n
            }
            
            function anotherOne( n : Number ) : Number {
                  // print something
                  // {
                  /* AA */
                  int i = 0
                  return n * n
            }
            
        """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToNoViolations")

        def violations = []

        def rule = new GosuFunctionSizeRule()
        rule.maxLines = 10
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToWithViolations() {

        def code = """
            function square( n : Number ) : Number {
              return n * n
              /*
                  * if () {
                  * }
                  */
            }
            
            //function square( n : Number ) : Number {
            //  return n * n
            //}
            
            /* 
            function anotherOne( n : Number ) : Number {
              //return n * n
              /*
              * if () {
              * }
              */
            }
            */
            
            function anotherOne( n : Number ) : Number {
              return n * n;
            }
            
            function anotherOne( n : Number ) : Number {
                  // print something
                  // {
                  /* AA */
                  int i = 0
                  return n * n
            }
            
        """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolations")

        def violations = []

        def rule = new GosuFunctionSizeRule()
        rule.maxLines = 4
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()
    }
}
