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
 * Test unused private methods
 */
class GosuUnusedPrivateFunctionRuleTest extends GroovyTestCase {

	
	void testApplyToNoViolationsWithPrivateDefBefore() {
		
		def code = """
			private function square<T extends Number>( n : T) : Number {
			  return n * n
			}

			function foo() {
			    square(4)
			}
				
		"""
		
		def sourceCode = new SourceString(code)
		
		def violations = []
		
		def rule = new GosuUnusedPrivateFunctionRule()
		rule.applyTo(sourceCode, violations)
		
		assert 0 == violations.size()
	}


    void testApplyToNoViolationsWithPrivateDefAfter() {

        def code = """
			function foo() {
               square(4)
            }

			private function square( n : Number ) : Number {
			  return n * n
			}
		"""

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuUnusedPrivateFunctionRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToNoViolationsWithSpace() {

        def code = """
			function foo() {
               square(4)
            }

			private function square ( n : Number ) : Number {
			  return n * n
			}
		"""

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuUnusedPrivateFunctionRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToWithInlineCommentedOutViolations() {

        def code = """
			private function square( n : Number ) : Number {
			  return n * n
			}

			function foo() {
			    //square(4)
			}

		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithInlineCommentedOutViolations"

        def violations = []

        def rule = new GosuUnusedPrivateFunctionRule()
        rule.applyTo(sourceCode, violations)
        assert 1 == violations.size()
    }

    void testApplyToWithBlockCommentedOutViolations() {

        def code = """
			private function square( n : Number ) : Number {
			  return n * n
			}

			/*
			function foo() {
			    square(4)
			}
			*/

		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithBlockCommentedOutViolations"

        def violations = []

        def rule = new GosuUnusedPrivateFunctionRule()
        rule.applyTo(sourceCode, violations)
        assert 1 == violations.size()
    }

    void testApplyToWithBlockCommentedOnSingleLineOutViolations() {

        def code = """
			private function square( n : Number ) : Number {
			  return n * n
			}


			function foo() {
			    /*square(4)*/
			}


		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithBlockCommentedOnSingleLineOutViolations"

        def violations = []

        def rule = new GosuUnusedPrivateFunctionRule()
        rule.applyTo(sourceCode, violations)
        assert 1 == violations.size()
    }
	
	void testApplyToWithViolations() {
		
		def code = """
		    private function square( n : Number ) : Number {
                return n * n
            }

		    private function square2( n : Number ) : Number {
                return n * n
            }

            function foo() {
                bar(4)
            }
		"""
		
		def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithViolations14"
		
		def violations = []
		
		def rule = new GosuUnusedPrivateFunctionRule()
		rule.applyTo(sourceCode, violations)
		assert 2 == violations.size()
	}

    void testApplyToWithViolationsDisable() {

        def code = """
		    private function square( n : Number ) : Number {
                return n * n
            }

            //codenarc-disable GosuUnusedFunctionMethod
		    private function squar2( n : Number ) : Number {
                return n * n
            }

            function foo() {
                bar(4)
            }
		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithViolationsDisable"

        def violations = []

        def rule = new GosuUnusedPrivateFunctionRule()
        rule.applyTo(sourceCode, violations)
        assert 1 == violations.size()
    }

    void testApplyToWithViolationsDisableEnable() {

        def code = """
            //codenarc-disable GosuUnusedFunctionMethod
		    private function square1( n : Number ) : Number {
                return n * n
            }

            //codenarc-enable GosuUnusedFunctionMethod
		    private function square2( n : Number ) : Number {
                return n * n
            }

            function foo() {
                bar(4)
            }
		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithViolationsDisableEnable"

        def violations = []

        def rule = new GosuUnusedPrivateFunctionRule()
        rule.applyTo(sourceCode, violations)
        assert 1 == violations.size()
    }

    void testApplyToWithViolationsDisableAll() {

        def code = """
		    private function square( n : Number ) : Number {
                return n * n
            }

            //codenarc-disable
		    private function squar2( n : Number ) : Number {
                return n * n
            }

            function foo() {
                bar(4)
            }
		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithViolationsDisableAll1234"

        def violations = []

        def rule = new GosuUnusedPrivateFunctionRule()
        rule.applyTo(sourceCode, violations)
        assert 1 == violations.size()
    }

    void testApplyToWithViolationsDisableEnableAll() {

        def code = """
            //codenarc-disable
		    private function square1( n : Number ) : Number {
                return n * n
            }

            //codenarc-enable
		    private function square2( n : Number ) : Number {
                return n * n
            }

            function foo() {
                bar(4)
            }
		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithViolationsDisableEnable"

        def violations = []

        def rule = new GosuUnusedPrivateFunctionRule()
        rule.applyTo(sourceCode, violations)
        assert 1 == violations.size()
    }
}
