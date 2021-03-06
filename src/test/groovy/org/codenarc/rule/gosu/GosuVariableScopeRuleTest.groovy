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
 * Max class size rules tests
 *
 */
class GosuVariableScopeRuleTest extends GroovyTestCase {
    void testApplyToWithViolations() {
			
        def code = """
            var claims = filteredClaims.iterator().toArray() as Claim[];
            var claims session = filteredClaims.iterator().toArray() as Claim[];
            var claims request = filteredClaims.iterator().toArray() as Claim[];
            var blah : String
            //var pmtConfMsg session : String = "";
            var foo request : int
            var pmtConfMsg session : String = "";            
            private static function isAllEmptyGetServiceSummary(request : GetServiceSummaryRequest) : boolean {
                Logger.logInfo("Created Log Statement")
            }
        """

        def sourceCode = new SourceString(code)
		
        assert 12 == sourceCode.getLines().size()
		
        def violations = []
		
        def rule = new GosuVariableScopeRule()
        rule.applyTo(sourceCode, violations)
        
        assert 4 == violations.size()
    }

    void testApplyToWithViolationsDisable() {

        def code = """
            var claims = filteredClaims.iterator().toArray() as Claim[];
            var claims session = filteredClaims.iterator().toArray() as Claim[];
            var claims request = filteredClaims.iterator().toArray() as Claim[];
            var blah : String
            //codenarc-disable GosuVariableScope
            //var pmtConfMsg session : String = "";
            var foo request : int
            var pmtConfMsg session : String = "";            
            private static function isAllEmptyGetServiceSummary(request : GetServiceSummaryRequest) : boolean {
                Logger.logInfo("Created Log Statement")
            }
        """

        def sourceCode = new SourceString(code)

        assert 13 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuVariableScopeRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()
    }

    void testApplyToWithViolationsDisableEnable() {

        def code = """
            //codenarc-disable GosuVariableScope
            var claims = filteredClaims.iterator().toArray() as Claim[];
            var claims session = filteredClaims.iterator().toArray() as Claim[];
            var claims request = filteredClaims.iterator().toArray() as Claim[];
            var blah : String
            //codenarc-enable GosuVariableScope
            //var pmtConfMsg session : String = "";
            var foo request : int
            var pmtConfMsg session : String = "";            
            private static function isAllEmptyGetServiceSummary(request : GetServiceSummaryRequest) : boolean {
                Logger.logInfo("Created Log Statement")
            }
        """

        def sourceCode = new SourceString(code)

        assert 14 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuVariableScopeRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()
    }

    void testApplyToNoViolations() {

        def code = """
            var users = new TreeSet<User>()
            var foo : String = ""           
            private static function isAllEmptyGetServiceSummary(request : GetServiceSummaryRequest) : boolean  
            Logger.logInfo("Created Log Statement")
        """

        def sourceCode = new SourceString(code)

        assert 6 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuVariableScopeRule()
        rule.applyTo(sourceCode, violations)
		
        assert 0 == violations.size()

    }
}
