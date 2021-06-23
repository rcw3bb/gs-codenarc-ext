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
 * Rule that checks the size of a method.
 */
class GosuFunctionSizeRule extends AbstractGosuRule {
	
	String name = 'GosuFunctionSize'
	String description = 'Lines of code within a Gosu function, including comments.' 
	int priority = 2
	int maxLines = 25

	@Override
	void gosuApplyTo(SourceCode sourceCode, List violations) {
		
		List functions = GosuUtil.getFunctions(sourceCode)

		functions.each {

			int lineWithCode = 0
			int lineCount = it.startLineNumber

			for (def obj in it.lines) {
				// Only count non-blank lines
				if (!GosuUtil.isBlankLine(obj)) {
					lineWithCode++

					// Check for violation
					if (lineWithCode > maxLines) {
						violations << createViolation(lineCount, obj, "Maximum line count for function exceeded. Max: ${maxLines}, Actual: ${lineWithCode}")
						break
					}
				}

				lineCount++
			}
		}
	}
	
}
