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
 * This class performs a basic complexity check of Gosu functions.
 */
class GosuCyclomaticComplexityRule extends GosuAbstractRule {
	
	String name = 'GosuCyclomaticComplexity'
	String description = 'Perform a standard cyclomatic complexity check.'
	int priority = 1
	int maxMethodComplexity = 7

	@Override
	void gosuApplyTo(SourceCode sourceCode, List violations) {
		List functions = GosuUtil.getFunctions(sourceCode)
		
		functions.each {
			int complexityCount = 1
			boolean withinBlockComment = false

			for (obj in it.lines) {
				// Check for block comments and ignore until block finished
				if (GosuUtil.isStartOfBlockComment(obj)) {
					withinBlockComment = true
				}
				if (GosuUtil.isEndOfBlockComment(obj)) {
					withinBlockComment = false
				}
				if (!withinBlockComment) {
					complexityCount += checkAdditionalComplexity(obj)
				}
				if (complexityCount > maxMethodComplexity) {
					violations << createViolation(it.startLineNumber, null, "Function complexity exceeds maximum. Maximum allowed: ${maxMethodComplexity}, Actual: ${complexityCount}")
					break
				}
			}
		}
	}
	
	/**
	 * Helper method that will increment the complexity count for each code branch found.
	 * @param lineOfCode line of code to check
	 * @return complexity count for the supplied line of code
	 */
	private int checkAdditionalComplexity(String lineOfCode) {
		int complexityCount = 0
		
		if (GosuUtil.isIfStatement(lineOfCode)) {
			complexityCount++
			complexityCount += GosuUtil.countConditionalOperators(lineOfCode)
		} else if (GosuUtil.isElseStatement(lineOfCode)) {
			complexityCount++
			complexityCount += GosuUtil.countConditionalOperators(lineOfCode)
		} else if (GosuUtil.isTernaryConditional(lineOfCode)) {
			complexityCount++
			complexityCount += GosuUtil.countConditionalOperators(lineOfCode)
		} else if (GosuUtil.isForStatement(lineOfCode)) {
			complexityCount++
		} else if (GosuUtil.isForeachStatement(lineOfCode)) {
			complexityCount++
		} else if (GosuUtil.isWhileStatement(lineOfCode)) {
			complexityCount++
		} else if (GosuUtil.isSwitchStatement(lineOfCode)) {
			complexityCount++
		} else if (GosuUtil.isCaseStatement(lineOfCode)) {
			complexityCount++
		} else if (GosuUtil.isCatchStatement(lineOfCode)) {
			complexityCount++
		}
		
		return complexityCount
	}
}
