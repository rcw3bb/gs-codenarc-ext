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
 * Rule that checks for unused function parameters in a function
 */
class GosuUnusedFunctionParameterRule extends GosuAbstractRule {

    String name = 'GosuUnusedFunctionParameter'
    String description = 'Unused function parameter within a Gosu function.'
    int priority = 2

    void gosuApplyTo(SourceCode sourceCode, List violations) {
        if (GosuUtil.isInterface(sourceCode)) {
            return
        }

        List functions = GosuUtil.getFunctions(sourceCode)

        functions.each { func ->

            String functionSignature = func.lines[0]

            /* There are cases where we don't want to run this rule over override functions.
             * It is valid to override a parent function but not use the parameters in the
             * parent signature.
             */
            if (!GosuUtil.isOverrideFunction(functionSignature)) {
                // get the function signature (stuff between ()) and create list of parameters
                List<String> functionParameters = GosuUtil.getFunctionParameters(func)

                functionParameters.each { param ->

                    if (param.contains(":") && !(param.contains("(") || param.contains(")"))) {
                        String parameterName = ((String)param).substring(0, ((String)param).indexOf(":")).trim()
                        if (!GosuUtil.isParameterUsedWithinFunction(parameterName, func)) {
                            violations << createViolation(func.startLineNumber, null, "Function parameter ${param} is never used within function")
                        }
                    }
                }
            }
        }
    }
}
