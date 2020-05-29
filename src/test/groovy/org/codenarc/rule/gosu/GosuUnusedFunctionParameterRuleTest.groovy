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

import org.codenarc.source.SourceCode
import org.codenarc.source.SourceString
/*
 * Test unused function parameters
 */
class GosuUnusedFunctionParameterRuleTest extends GroovyTestCase {

    void testApplyToWithNoViolations() {

        def code = """
			private function square( n : Number, b : int ) : Number {
			  return n * b
			}

			function foo() {
			    square(4)
			}

		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithNoViolations"

        def violations = []

        def rule = new GosuUnusedFunctionParameterRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToWithNoViolationsOnOverrideFunction() {

        def code = """
			public override function square( n : Number, b : int ) : Number {
			  return n * n
			}
		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToWithNoViolationsOnOverrideFunction"

        def violations = []

        def rule = new GosuUnusedFunctionParameterRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToViolatingFunctionParamOnSingleLine() {

        def code = """
			private function bar( m : Number, b : int ) : Number {
			  return m * m
			}

			function dog() {
			    square(4)
			}

		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToViolatingFunctionParamOnSingleLine"

        def violations = []

        def rule = new GosuUnusedFunctionParameterRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }


    void testApplyToSampleGWClass() {

        def code = """
        private static var fields : java.util.List;

        construct()
        {
        }

        public static function hasChangedFields(fs : java.util.Set) : boolean {
        var list = getFields();
        for (var s in fs) {
            if (list.contains( s )) continue;
            return true;
        }
        return false;
    }

        private static function getFields() : java.util.List {
        if (fields == null) {
            fields = new java.util.ArrayList();
            fields.add( "UpdateTime" );
            fields.add( "BeanVersion" );
        }
        return fields;
    }
		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToSampleGWClass"

        def violations = []

        def rule = new GosuUnusedFunctionParameterRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToSampleGWMethod() {

        def code = """
    public static function assignBasedOnEarlierActivity(ruleName : String,
    originalActivity : Activity,
    newActivity : Activity): boolean
    {
        return  assignRuleForGroupAndUser(ruleName, newActivity, originalActivity.AssignedGroup, originalActivity.CloseUser )
    }
		"""

        def sourceCode = new SourceString(code)
        sourceCode.path = "testApplyToSampleGWMethod"

        def violations = []

        def rule = new GosuUnusedFunctionParameterRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }



    void testViolationsNotGeneratedForInterfaces() {
        SourceCode code = new SourceString(
                """
interface IReferenceDataService {
  public function getValue1(req : Account) : List<AccountContactRole>
  public function getValue2(req : Policy) : List<PolicyPeriod>
  public function getValue3(requestCriteria : Contact) : List<Contact>
}
                """
        )
        code.path = "testApplyToSampleGWMethod3"
        def violations = []

        def rule = new GosuUnusedFunctionParameterRule()
        rule.applyTo(code, violations)

        assert 0 == violations.size()
    }

}
