package org.codenarc.rule.gosu

import org.codenarc.source.SourceString
import org.codenarc.util.gosu.GosuUtil

class GosuCyclomaticComplexityRuleTest extends GroovyTestCase {

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
              if (true) {
                  return true
              } else {
                  return false
              }
            }
            
            function anotherOne( n : Number ) : Number {
                  // print something
                  // {
                  /* AA */
                  int i = 0
                  return n * n
            }
            
            function anotherOne( n : Number ) : Number {
                  if (true || true) {
                      return true
                  }
            }
            
        """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToNoViolations")

        def violations = []

        def rule = new GosuCyclomaticComplexityRule()
        rule.maxMethodComplexity = 3
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
          if (true) {
              return true
          } else {
              return false
          }
        }
            
        function anotherOne( n : Number ) : Number {
              if (true)
                  return true
              else
                  return false
        }
            
        function anotherTwo( n : Number ) : Number {
                for(int i = 0; i < 12; i++) {
                
                }
        }
            
        function anotherThree( n : Number ) : Number {
            foreach(...) {
            
            }
        }
            
        function anotherFour( n : Number ) : Number {
            while(...) {
            
            }
        }
            
        function anotherOne( n : Number ) : Number {
            return 2 > 3 ? true : false
        }
        
        function anotherOne( n : Number ) : Number {
              // print something
              // {
              /* AA */
              int i = 0
              return n * n
        }
            
        function anotherOne( n : Number ) : Number {
              switch(str) {
                case "one":
                default:
              }
        }
            
        function anotherOne( n : Number ) : Number {
            try {
            
            } catch (AnotherException ex) {
            
            }
        }
            
        
    """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolations")

        def violations = []

        def rule = new GosuCyclomaticComplexityRule()
        rule.maxMethodComplexity = 1
        rule.applyTo(sourceCode, violations)

        assert 8 == violations.size()
    }

    void testApplyToWithViolationsSwitchCase() {
        def code = """
            function anotherOne( n : Number ) : Number {
              switch(str) {
                case "one":
                default:
              }
            }
            """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsSwitchCase")

        def violations = []

        def rule = new GosuCyclomaticComplexityRule()
        rule.maxMethodComplexity = 3
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsConditionalOperator() {
        def code = """
            function conditionalOrOne( n : Number ) : Number {
                if (true || true) {
                
                }
            }

      function anotherOrTwo( n : Number ) : Number {
        if (true or true) {
        
        }
        }

      function conditionalAndOne( n : Number ) : Number {
        if (true && true) {
        
        }
        }

      function anotherAndTwo( n : Number ) : Number {
        if (true and true) {
        
        }
        }
            """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsConditionalOperator")

        def violations = []

        def rule = new GosuCyclomaticComplexityRule()
        rule.maxMethodComplexity = 2
        rule.applyTo(sourceCode, violations)

        assert 4 == violations.size()
    }

    void testApplyToWithViolationsTernaryConditional() {
        def code = """
            function anotherOne( n : Number ) : Number {
                return var1 && var2 ? true : false
            }
            """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsTernaryConditional")

        def violations = []

        def rule = new GosuCyclomaticComplexityRule()
        rule.maxMethodComplexity = 2
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsCatchStatement() {
        def code = """
            function anotherOne( n : Number ) : Number {
                try {
                
                } catch (AnotherException ex) {
                
                } catch (Exception ex) {
                
                }
            }
            """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsCatchStatement")

        def violations = []

        def rule = new GosuCyclomaticComplexityRule()
        rule.maxMethodComplexity = 2
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsSampleCode() {

        def code = """
              function testCyclomaticComplexity() :java.util.List {
                  
                    var list = new java.util.ArrayList();
                    
                    if (this.testValue == "Test") {   //First Case
                      for (r in this.SomeList) {
                        if (r.code != "one" and
                            r.code != "two" and
                            r.code != "three") {
                              
                          list.add( r )
                        }
                      }
                    } else if (this.testValue2) { // Second case
                        for (r in this.SomeOtherList) {
                          if (r.code == "four" or
                              r.code == "five" or
                              r.code == "six"  or
                              r.code == "seven") {
                              
                            list.add( r )
                          }
                        }
                    }
                     else { // Other Cases
                        for (r in this.thirdList) {
                          if (r.code == "eight" or
                              r.code == "nine" or
                              r.code == "ten") {
                              
                            list.add( r )
                          }
                        }
                    }
                    return list;
                  }
            """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsSampleCode")

        def violations = []

        def rule = new GosuCyclomaticComplexityRule()
        rule.maxMethodComplexity = 7
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()

    }

}
