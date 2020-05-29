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
import org.codenarc.util.gosu.GosuUtil;

/**
 * Rule that checks the size of a class.
 */
class GosuClassSizeRule extends AbstractRule {
    String name = 'GosuClassSize'
    String description = 'Lines of code within a Gosu source code file, including comments.' 
    int priority = 2
    int maxLines = 1200

    void applyTo(SourceCode sourceCode, List violations) {
    	
    	//Count lines that have text
    	int lineCount = 0
    	sourceCode.getLines().each {
    		if (!GosuUtil.isBlankLine(it)) {
    			lineCount++
    		}
    	}
    	
    	if(lineCount > maxLines) {	
    		violations << createViolation(0, null, "Class size exceeds maximum lines. Maximum allowed: ${maxLines}, Actual: ${sourceCode.getLines().size()}")
    	}
    }
}