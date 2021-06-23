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
 * Rule for identifying commented out code.
 */
class GosuCommentedOutCodeRule extends AbstractGosuRule {
	String name = 'GosuCommentedOutCode'
	String description = 'Check for commented out code.' 
	int priority = 2

	@Override
	void gosuApplyTo(SourceCode sourceCode, List violations) {
		
		def withinBlockComment = false
        def withinExampleCodeBlock = false

		sourceCode.getLines().eachWithIndex { obj, i ->
		
			// Check for block comments
			if (GosuUtil.isStartOfBlockComment(obj) ) {
				withinBlockComment = true
			}
			if (GosuUtil.isEndOfBlockComment(obj)) {
				withinBlockComment = false
			}

            // Check for example code snippets in documentation (<code></code> blocks)
            if (GosuUtil.isStartOfExampleCodeInDocumentation(obj)) {
                withinExampleCodeBlock = true
            }

            if (GosuUtil.isEndOfExampleCodeInDocumentation(obj)) {
                withinExampleCodeBlock = false
            }

            if (!withinExampleCodeBlock) {
                if (withinBlockComment && (GosuUtil.isCode(obj) || isMiscellaneousCode(obj))) {
                    violations << createViolation(i+1, null, "Commented out code found at line ${i+1}")
                } else if (!withinBlockComment && GosuUtil.isInlineCommented(obj) && (GosuUtil.isCode(obj) || isMiscellaneousCode(obj))) {
                    violations << createViolation(i+1, null, "Commented out code found at line ${i+1}")
                }
            }
		}
	}
	
	private boolean isMiscellaneousCode(String lineOfText) {
		if (lineOfText.contains(");")) {
			return true
		} else if (lineOfText.trim() =~ /.*var .*[\:].*$/) {          // var xx : yy
			return true
		} else if (lineOfText.trim() =~ /^[^=]*=[^=]*$/) {            // xxx = yyy
      return true
    } else if (lineOfText.trim() =~ /^[^=]*==[^=]*$/) {            // xxx == yyy
      return true
    } else if (lineOfText.trim() =~ /.*[a-zA-Z0-9]\(.*\)$/) {     // xxx(yyy)
      return true
		}else {
			return false
		}
	}
}
