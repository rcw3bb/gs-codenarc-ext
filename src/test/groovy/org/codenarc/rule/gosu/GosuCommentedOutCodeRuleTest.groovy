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

/*
 * Test commented out code rule
 */
class GosuCommentedOutCodeRuleTest extends GroovyTestCase {

	
	void testApplyToNoViolations() {
		
		def code = """
			function square( n : Number ) : Number {
			  return n * n
			}
				
		"""
		
		def sourceCode = new SourceString(code)
		
		def violations = []
		
		def rule = new GosuCommentedOutCodeRule()
		rule.applyTo(sourceCode, violations)
		
		assert 0 == violations.size()
	}

    void testApplyToNoViolationsInCodeDocumentationBlock() {

        def code = """
            /*
              Example code
              <pre><code>
              foo.increment(1)
              </code></pre>
            */
			function square( n : Number ) : Number {
			  return n * n
			}

		"""

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuCommentedOutCodeRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }
	
	void testApplyToWithViolations() {
		
		def code = """
		    //  else {
		    //    existingCount=existingCount + 1;
        //    existingCount = existingCount + 1;
		    //  }
		"""
		
		def sourceCode = new SourceString(code)
		
		def violations = []
		
		def rule = new GosuCommentedOutCodeRule()
		rule.applyTo(sourceCode, violations)
		assert 3 == violations.size()
	}
  
  void testApplyToWithBlockViolations() {

    def code = """
      /*
        else {
          existingCount=existingCount + 1;
        }
      */
    """

    def sourceCode = new SourceString(code)

    def violations = []

    def rule = new GosuCommentedOutCodeRule()
    rule.applyTo(sourceCode, violations)
    assert 2 == violations.size()
  }
  
  void testApplyToVarDefs() {
    def code = """
    		// var myVar = yetAnotherVar
        // var mySecondVar : SecondVar
    """
  
    def sourceCode = new SourceString(code)
    def violations = []
    def rule = new GosuCommentedOutCodeRule()
    rule.applyTo(sourceCode, violations)
    assert 2 == violations.size()
  }

    void testApplyToVarDefsDisabled() {
        def code = """
            //codenarc-disable GosuCommentedOutCode
    		// var myVar = yetAnotherVar
        // var mySecondVar : SecondVar
    """

        def sourceCode = new SourceString(code)
        def violations = []
        def rule = new GosuCommentedOutCodeRule()
        rule.applyTo(sourceCode, violations)
        assert 0 == violations.size()
    }

    void testApplyToVarDefsDisabledEnabled() {
        def code = """
            //codenarc-disable GosuCommentedOutCode
    		// var myVar = yetAnotherVar
    		//codenarc-enable GosuCommentedOutCode
        // var mySecondVar : SecondVar
    """

        def sourceCode = new SourceString(code)
        def violations = []
        def rule = new GosuCommentedOutCodeRule()
        rule.applyTo(sourceCode, violations)
        assert 1 == violations.size()
    }

    void testApplyToVarDefsDisabledAll() {
        def code = """
            //codenarc-disable
    		// var myVar = yetAnotherVar
        // var mySecondVar : SecondVar
    """

        def sourceCode = new SourceString(code)
        def violations = []
        def rule = new GosuCommentedOutCodeRule()
        rule.applyTo(sourceCode, violations)
        assert 0 == violations.size()
    }

    void testApplyToVarDefsDisabledEnabledAll() {
        def code = """
            //codenarc-disable
    		// var myVar = yetAnotherVar
    		//codenarc-enable
        // var mySecondVar : SecondVar
    """

        def sourceCode = new SourceString(code)
        def violations = []
        def rule = new GosuCommentedOutCodeRule()
        rule.applyTo(sourceCode, violations)
        assert 1 == violations.size()
    }

    /** Expected to fail so assert commented out for now */
  void testShouldNotApplyToVarDefs() {
    def code = """
      // this stuff : should not pick up var
    """
    
    def sourceCode = new SourceString(code)
    def violations = []
    def rule = new GosuCommentedOutCodeRule()
    rule.applyTo(sourceCode, violations)
    assert 0 == violations.size()
  }
  
  void testApplyToAssignments() {
    def code = """
      // this = that
      // this=that
      //============
    """

    def sourceCode = new SourceString(code)
    def violations = []
    def rule = new GosuCommentedOutCodeRule()
    rule.applyTo(sourceCode, violations)
    assert 2 == violations.size()
  }
  
  void testApplyToEquality() {
    def code = """
      // this == that
      // this==that
      //============
    """

    def sourceCode = new SourceString(code)
    def violations = []
    def rule = new GosuCommentedOutCodeRule()
    rule.applyTo(sourceCode, violations)
    assert 2 == violations.size()
  }

    void testMiscStatement() {
    def code = """
      // someFunction(someVar)
      // someFunction()
      // some text (comment) and some more
    """
    // TODO - change regex so this is not picked up 
    // @see #someFunction() 
    
    def sourceCode = new SourceString(code)
    def violations = []
    def rule = new GosuCommentedOutCodeRule()
    rule.applyTo(sourceCode, violations)
    assert 2 == violations.size()
  }
}
