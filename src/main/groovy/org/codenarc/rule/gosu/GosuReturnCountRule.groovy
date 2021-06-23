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
import org.codenarc.util.gosu.GosuUtil;

/**
 * This class performs a basic count of return statement
 */
class GosuReturnCountRule extends AbstractGosuRule {

	String name = 'GosuReturnCount'
	String description = 'Check for a maximum return statement count from Gosu functions.' 
	int priority = 1
	int maxReturnCount = 4
	
	void gosuApplyTo(SourceCode sourceCode, List violations) {
		List functions = GosuUtil.getFunctions(sourceCode)
		
		functions.each {
			
			int returnCount = 0
			boolean withinBlockComment = false
			int lineCount = it.startLineNumber

			for (obj in it.lines) {

				// Check for block comments and ignore until block finished
				if (GosuUtil.isStartOfBlockComment(obj)) {
					withinBlockComment = true
				}
				if (GosuUtil.isEndOfBlockComment(obj)) {
					withinBlockComment = false
				}
				if (!withinBlockComment && GosuUtil.isReturnStatement(obj)) {
					returnCount++
				}

				if (returnCount > maxReturnCount) {
					violations << createViolation(lineCount, null, "Function return statement count exceeds maximum. Maximum allowed: ${maxReturnCount}, Actual: ${returnCount}")
					break
				}

				lineCount++
			}
		}
	}
		
}
