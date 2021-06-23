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

import org.codenarc.rule.Violation
import org.codenarc.source.SourceString

/**
 * Max class size rules tests
 *
 */
class GosuClassSizeRuleTest extends GroovyTestCase {
    void testApplyToNoViolations() {

        def code = """
            int lineCount = 0
            
            sourceCode.getLines().each {
                if (it) {
                    lineCount++
                }
            }        
        """

        def sourceCode = new SourceString(code)

        assert 9 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuClassSizeRule(maxLines: 6)
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()

    }

    void testApplyToWithViolations() {

        def code = """
            int lineCount = 0
            //comment
            sourceCode.getLines().each {
                if (it) {
                    lineCount++
                }
            }        
        """

        def sourceCode = new SourceString(code)

        assert 9 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuClassSizeRule(maxLines: 6)
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()

    }

    void testApplyToWithViolationsDisable() {

        def code = """
            //codenarc-disable GosuClassSize
            int lineCount = 0
            //comment 
            sourceCode.getLines().each {
                if (it) {
                    lineCount++
                }
            }        
        """

        def sourceCode = new SourceString(code)

        assert 10 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuClassSizeRule(maxLines: 6)
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()

    }

    void testApplyToWithViolationsDisableAndEnabled() {

        def code = """
            //codenarc-disable GosuClassSize
            int lineCount = 0
            //comment 
            sourceCode.getLines().each {
                if (it) {
                    lineCount++
                    //codenarc-enable GosuClassSize
                }
            }
            print("something")
        """

        def sourceCode = new SourceString(code)

        assert 12 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuClassSizeRule(maxLines: 6)
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()

    }

    void testApplyToWithViolationsDisableAll() {

        def code = """
            //codenarc-disable
            int lineCount = 0
            //comment 
            sourceCode.getLines().each {
                if (it) {
                    lineCount++
                }
            }        
        """

        def sourceCode = new SourceString(code)

        assert 10 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuClassSizeRule(maxLines: 6)
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()

    }

    void testApplyToWithViolationsDisableAndEnabledAll() {

        def code = """
            //codenarc-disable GosuClassSize
            int lineCount = 0
            //comment 
            sourceCode.getLines().each {
                if (it) {
                    lineCount++
                    //codenarc-enable
                }
            }
            print("something")
        """

        def sourceCode = new SourceString(code)

        assert 12 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuClassSizeRule(maxLines: 6)
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()

    }


}
