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
 * Rule for checking the maximum allowed allowed parameter count for a function.
 */
class GosuFunctionParameterLengthRule extends AbstractGosuRule {
	String name = 'GosuFunctionParameterLength'
	String description = 'Check for the maximum allow parameter count.' 
	int priority = 2
	int maxParameters = 4

	@Override
	void gosuApplyTo(SourceCode sourceCode, List violations) {
		
		List functions = GosuUtil.getFunctions(sourceCode)
		
		functions.each {
			int parameterCount = GosuUtil.countFunctionParameters(it.lines[0])
			if (parameterCount > maxParameters) {
				violations << createViolation(it.startLineNumber, null, "Maximum parameter count for function exceeded. Max: ${maxParameters}, Actual: ${parameterCount}")
			}			
		}
	}
	
}
