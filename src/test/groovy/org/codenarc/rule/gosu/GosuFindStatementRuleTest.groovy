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

class GosuFindStatementRuleTest extends GroovyTestCase {
    void testApplyToNoViolations() {

        def code = """
            // filter so only the claims with this policy vehicle are returned
            var list = new ArrayList<String>()
            return list.findAll( \\ item -> item.equalsIgnoreCase("test"));
        """

        def sourceCode = new SourceString(code)

        assert 5 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuFindStatementRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToWithViolations() {

        def code = """
            // Get misc. accounts
            var accounts = find (acc  IN Account
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")

        """

        def sourceCode = new SourceString(code)

        assert 7 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuFindStatementRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()

    }

    void testApplyToWithViolationsMultiFind() {

        def code = """
            // Get misc. accounts
            var accounts = find (acc  IN Account
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")
                                 
            var accounts2 = find (acc  IN Account
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")

        """

        def sourceCode = new SourceString(code)

        assert 11 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuFindStatementRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()

    }

    void testApplyToWithViolationsMultiFindDisable() {

        def code = """
            // Get misc. accounts
            var accounts = find (acc  IN Account
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")
                                 
            //codenarc-disable GosuFindStatement
            var accounts2 = find (acc  IN Account 
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")

        """

        def sourceCode = new SourceString(code)

        assert 12 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuFindStatementRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()

    }

    void testApplyToWithViolationsMultiFindDisableEnable() {

        def code = """
            //codenarc-disable GosuFindStatement
            // Get misc. accounts
            var accounts = find (acc  IN Account
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")
            
            //codenarc-enable GosuFindStatement                     
            var accounts2 = find (acc  IN Account
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")

        """

        def sourceCode = new SourceString(code)

        assert 13 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuFindStatementRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()

    }

    void testApplyToWithViolationsMultiFindDisableAll() {

        def code = """
            // Get misc. accounts
            var accounts = find (acc  IN Account
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")
                                 
            //codenarc-disable
            var accounts2 = find (acc  IN Account 
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")

        """

        def sourceCode = new SourceString(code)

        assert 12 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuFindStatementRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()

    }

    void testApplyToWithViolationsMultiFindDisableEnableAll() {

        def code = """
            //codenarc-disable
            // Get misc. accounts
            var accounts = find (acc  IN Account
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")
            
            //codenarc-enable                     
            var accounts2 = find (acc  IN Account
                               WHERE acc.AccountNumber != "Unassigned"
                                 AND acc.Status == "Draft")

        """

        def sourceCode = new SourceString(code)

        assert 13 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuFindStatementRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()

    }
}
