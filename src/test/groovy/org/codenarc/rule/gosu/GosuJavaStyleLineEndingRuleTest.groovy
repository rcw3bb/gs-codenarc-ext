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
class GosuJavaStyleLineEndingRuleTest extends GroovyTestCase {
    void testApplyToNoViolations() {

        def code = """
            if (someList[0] == null) return null
            if (someList.getCount() == 1)
        """

        def sourceCode = new SourceString(code)

        assert 4 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuJavaStyleLineEndingRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToWithViolations() {

        def code = """
            if (someList.getCount() == 0) return null
            if (someList.getCount()==0) return null;
            if (someList.getCount ( )==0) return null;
        """

        def sourceCode = new SourceString(code)

        assert 5 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuJavaStyleLineEndingRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()

    }
}
