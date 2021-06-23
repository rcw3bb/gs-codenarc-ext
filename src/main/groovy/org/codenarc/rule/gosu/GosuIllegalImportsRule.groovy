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
import org.codenarc.util.gosu.GosuCodeBlockWrapper

/**
 * Illegal imports rule. Check for violations of the illegal imports rule.
 */
class GosuIllegalImportsRule extends GosuAbstractRule {
	String name = 'GosuIllegalImports'
	String description = 'Illegal imports rule. Check for violations of the illegal imports rule.' 
	int priority = 2

	@Override
	void gosuApplyTo(SourceCode sourceCode, List violations) {
		
		def usesTypeCache = [:]
		def withinBlockComment = false

		sourceCode.getLines().eachWithIndex { obj, i ->
			
			// Check for block comments and ignore until block finished
			if (GosuUtil.isStartOfBlockComment(obj)) {
				withinBlockComment = true
			}
			if (GosuUtil.isEndOfBlockComment(obj)) {
				withinBlockComment = false
			}
			
			if (!withinBlockComment && !GosuUtil.isBlankLine(obj) && !GosuUtil.isInlineCommented(obj) && GosuUtil.isUsesStatement(obj)) {
				
				String type = stripToType(obj)
				
				//Check for duplicate import
				if (usesTypeCache.containsKey(type)) {
					violations << createViolation(i+1, null, "Duplicate import found at line ${i+1}")
				} else if (type == "*") {
					violations << createViolation(i+1, null, "Illegal use of wildcard import at line ${i+1}")
				} else {
					//Add type to cache
					GosuCodeBlockWrapper wrapper = new GosuCodeBlockWrapper()
					wrapper.lines.add(obj)
					wrapper.startLineNumber = i+1
					usesTypeCache.put(type, wrapper)
				}
				
			}
		}
		
	}
	
	/**
	 * Strip an import statement to a specific type.
	 * @param lineOfCode
	 * @return the type
	 */
	private String stripToType(String lineOfCode) {
		def result = lineOfCode.tokenize(".").last().split(";")[0]
		return result.trim()
	}
}
