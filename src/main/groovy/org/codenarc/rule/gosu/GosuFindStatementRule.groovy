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
import org.codenarc.util.gosu.GosuUtil

/**
 * Rule that checks for usage of the 'find' statement
 */
class GosuFindStatementRule extends GosuAbstractRule {
    String name = 'GosuFindStatement'
    String description = 'Lines of code which use the find statement, which can be performance intensive.'
    int priority = 1

    @Override
    void gosuApplyTo(SourceCode sourceCode, List violations) {

        def withinBlockComment = false
        sourceCode.getLines().eachWithIndex { obj, i ->

            // Check for block comments and ignore until block finished
            if (GosuUtil.isStartOfBlockComment(obj)) {
                withinBlockComment = true
            }
            if (GosuUtil.isEndOfBlockComment(obj)) {
                withinBlockComment = false
            }

            if (!withinBlockComment &&
                    !GosuUtil.isBlankLine(obj) &&
                    !GosuUtil.isInlineCommented(obj) &&
                    isUsageOfFindStatement(obj)) {
                violations << createViolation(i + 1, null, "Usage of find statement detected at line ${i + 1}")
            }
        }
    }

    /**
     * Find instances of the pattern getCount() == 0
     */
    private boolean isUsageOfFindStatement(String textLine) {
        return ( textLine.trim() =~ /find *\(/ ) ? true : false
	}
}