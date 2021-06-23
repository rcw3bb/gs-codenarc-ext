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
 * Unused imports rule. Check for violations of the unused imports rule.
 */
class GosuUnusedImportsRule extends AbstractGosuRule {
	String name = 'GosuUnusedImports'
	String description = 'Unused imports rule. Check for violations of the unused imports rule.' 
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
				if (!usesTypeCache.containsKey(type) && type != "*") {
				
					//Add type to cache
					GosuCodeBlockWrapper wrapper = new GosuCodeBlockWrapper()
					wrapper.lines.add(obj)
					wrapper.startLineNumber = i+1
					usesTypeCache.put(type, wrapper)
				}
				
			} else if (!withinBlockComment && !GosuUtil.isBlankLine(obj) && !GosuUtil.isInlineCommented(obj)) {
				checkForUsage(usesTypeCache, obj)
			}
		}
		
		usesTypeCache.values().each {		
			violations << createViolation(it.startLineNumber, null, "Unused import ${it.lines[0]}")
		}
	}
	
	/**
	 * Check for the usage of imports. Attempt to find imported types within a
	 * line of code.
	 * @param usesTypeCache collection of imports
	 * @param lineOfCode 
	 */
	private void checkForUsage(def usesTypeCache, String lineOfCode) {
		
		//Collection of used imports
		def remove = []
		
		//TODO refactor me please
		for (String str in usesTypeCache.keySet()) {
			//ignore wildcard import
			if (lineOfCode.contains("new ${str}") ||
                    lineOfCode.contains(":${str}") ||
                    lineOfCode.contains(": ${str}") ||
                    lineOfCode.contains("<${str}") ||
                    lineOfCode.contains("< ${str}") ||
                    lineOfCode.contains("@${str}") ||
                    lineOfCode.contains("${str}.") ||
                    lineOfCode.contains(" ${str} ") ||
                    lineOfCode.contains(" ${str}<") ||
                    lineOfCode.contains("(${str}.") ||
                    lineOfCode.contains("(${str}") ||
                    lineOfCode.contains("${str},") ||
                    lineOfCode.contains("${str})") ||
                    lineOfCode.contains("as ${str}") ||
                    lineOfCode.contains(".${str}(") ||
                    lineOfCode.contains(".${str}") ||
                    lineOfCode.contains("=${str}") ||
                    lineOfCode.contains("= ${str}") ||
                    lineOfCode.contains("extends ${str}") ||
                    lineOfCode.contains("implements ${str}")) {
				
				remove.add(str)
			}
		}
		
		remove.each { usesTypeCache.remove(it) }
		
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
