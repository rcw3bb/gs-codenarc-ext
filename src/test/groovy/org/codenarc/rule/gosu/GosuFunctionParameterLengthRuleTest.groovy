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

/**
 * Test the max function parameter count rule.
 */
class GosuFunctionParameterLengthRuleTest extends GroovyTestCase {

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
            } sat on or the electric toot
            */
            
            function anotherOne( n : Number, str : String ) : Number {
              return n * n
            }
            
            function anotherOne( n : Number ) : Number {
                  // print something
                  // {
                  /* AA */
                  int i = 0
                  return n * n
            }
            
            function anotherOne() : Number {
                  // print something
                  // {
                  /* AA */
                  int i = 0
                  return n * n
            }

            // Test generics won't count as paramers
            function anotherOne( n : Number, myMap : java.util.HashMap<String, String>) : Number {
                  // print something
                  // {
                  /* AA */
                  int i = 0
                  return n * n
            }
        """

        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToNoViolations")

        def violations = []

        def rule = new GosuFunctionParameterLengthRule()
        rule.maxParameters = 2
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
        
        function anotherOne( n : Number, str : String, num : Integer ) : Number {
          return n * n
        }
            
        function anotherOne( n : Number, name : String, description : String ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
              return n * n
        }
        
        function anotherOne( n : Number, name : String, description : String, i : Integer ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
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

        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolations")

        def violations = []

        def rule = new GosuFunctionParameterLengthRule()
        rule.maxParameters = 2
        rule.applyTo(sourceCode, violations)

        assert 3 == violations.size()
    }

    void testApplyToWithViolationsDisable() {

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
        
        function anotherOne( n : Number, str : String, num : Integer ) : Number {
          return n * n
        }

        //codenarc-disable GosuFunctionParameterLength            
        function anotherOne( n : Number, name : String, description : String ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
              return n * n
        }
        
        function anotherOne( n : Number, name : String, description : String, i : Integer ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
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

        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsDisable")

        def violations = []

        def rule = new GosuFunctionParameterLengthRule()
        rule.maxParameters = 2
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsDisableEnable() {

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
        
        function anotherOne( n : Number, str : String, num : Integer ) : Number {
          return n * n
        }

        //codenarc-disable GosuFunctionParameterLength            
        function anotherOne( n : Number, name : String, description : String ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
              return n * n
        }
        
        //codenarc-enable GosuFunctionParameterLength
        function anotherOne( n : Number, name : String, description : String, i : Integer ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
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

        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsDisableEnable")

        def violations = []

        def rule = new GosuFunctionParameterLengthRule()
        rule.maxParameters = 2
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()
    }

    void testApplyToWithViolationsDisableAll() {

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
        
        function anotherOne( n : Number, str : String, num : Integer ) : Number {
          return n * n
        }

        //codenarc-disable             
        function anotherOne( n : Number, name : String, description : String ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
              return n * n
        }
        
        function anotherOne( n : Number, name : String, description : String, i : Integer ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
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

        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsDisable")

        def violations = []

        def rule = new GosuFunctionParameterLengthRule()
        rule.maxParameters = 2
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsDisableEnableAll() {

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
        
        function anotherOne( n : Number, str : String, num : Integer ) : Number {
          return n * n
        }

        //codenarc-disable            
        function anotherOne( n : Number, name : String, description : String ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
              return n * n
        }
        
        //codenarc-enable
        function anotherOne( n : Number, name : String, description : String, i : Integer ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
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

        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsDisableEnableAll")

        def violations = []

        def rule = new GosuFunctionParameterLengthRule()
        rule.maxParameters = 2
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()
    }


}
