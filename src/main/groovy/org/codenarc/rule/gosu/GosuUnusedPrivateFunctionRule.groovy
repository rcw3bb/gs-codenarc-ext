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

import org.codenarc.rule.AbstractRule
import org.codenarc.source.SourceCode
import org.codenarc.util.gosu.GosuUtil

/**
 * Rule that checks for unused private methods in a class
 */
class GosuUnusedPrivateFunctionRule extends AbstractRule {

    String privateFunctionIdentifier = 'private'
	String name = 'GosuUnusedFunctionMethod'
	String description = 'Unused private function within a Gosu class.'
	int priority = 2

	void applyTo(SourceCode sourceCode, List violations) {
		List functions = GosuUtil.getFunctions(sourceCode)

		functions.each {
			String functionSignature = it.lines[0]

            // Check if the function is a private method
            if (functionSignature.trim().startsWith(privateFunctionIdentifier)) {
                // Check the rest of the sourceCode to see the count of occurances
                int functionOccurances = GosuUtil.countFunctionOccurances(functionSignature, sourceCode)
                if (functionOccurances == 1) {
                    violations << createViolation(it.startLineNumber, null, "Private function ${functionSignature} is never used")
                }
            }
		}
	}
}
