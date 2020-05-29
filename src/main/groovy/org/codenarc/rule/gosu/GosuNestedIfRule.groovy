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
import org.codenarc.source.SourceString
import org.codenarc.util.gosu.GosuUtil

/**
 * Nested if rule. Check for violations in nested if depth.
 *
 */
class GosuNestedIfRule extends AbstractRule {

	String name = 'GosuNestedIf'
	String description = 'Nested if rule. Check for violations in nested if statement depth.' 
	int priority = 1
	int maxNestedDepth = 4
	
	void applyTo(SourceCode sourceCode, List violations) {
		List ifStatements = GosuUtil.getIfStatements(sourceCode)
	
		ifStatements.each { i ->
			//Count the nested depth of the current top level if
			countNested(i.startLineNumber , 0, i.lines, violations)
		}
	}
	
	/**
	 * Helper method that will increment a nested if count
	 * @param startLine start line number of the top level "if" statement
	 * @param currentDepth the starting depth (recursive method)
	 * @param linesOfCode lines of code to check
	 * @param violations collection of violations
	 */
	private void countNested(int startLine,int currentDepth, List linesOfCode, List violations) {
		currentDepth++
		
		def code = ""
		linesOfCode.each { j ->
			code += "${j}\n"
		}
		
		def sourceCode = new SourceString(code)
		
		def ifStatements = GosuUtil.getIfStatements(sourceCode)

		if (ifStatements.size() > 0) {
			
			ifStatements.each { j ->
				countNested(startLine, currentDepth, j.lines, violations)
			}
				
		} else {
			// check current depth
			if (currentDepth > maxNestedDepth) {
				violations << createViolation(startLine, null, "Nested if depth exceeded. Max: ${maxNestedDepth}, Actual: ${currentDepth}")
			}
		}

	}
	
}
