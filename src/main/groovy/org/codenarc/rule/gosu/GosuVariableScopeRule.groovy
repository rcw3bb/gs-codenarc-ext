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
 * Rule that checks for usage of scoped variables
 */
class GosuVariableScopeRule extends GosuAbstractRule {
    String name = 'GosuVariableScope'
    String description = 'Detects usage of request or session scoped variables.'
    int priority = 3

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
                    !GosuUtil.isInlineCommented(obj)) {

                if (isRequestScopedVariable(obj)) {
                    violations << createViolation(i + 1, null, "Request scoped variable detected at line ${i + 1}")
                }

                if (isSessionScopedVariable(obj)) {
                    violations << createViolation(i + 1, null, "Session scoped variable detected at line ${i + 1}")
                }
            }
        }
    }

    /**
     * Find instances of request scoped variables
     * var foo request : String = ""  OR
     * var foo request = new String()
     */
    private boolean isRequestScopedVariable(String textLine) {
        return ( textLine.trim() =~ /var.* request *[\:\=] */ ) ? true : false
	}

    /**
     * Find instances of session scoped variables
     * var foo session : String = ""  OR
     * var foo session = new String()
     */
    private boolean isSessionScopedVariable(String textLine) {
        return ( textLine.trim() =~ /var.* session *[\:\=] */ ) ? true : false
	}
}