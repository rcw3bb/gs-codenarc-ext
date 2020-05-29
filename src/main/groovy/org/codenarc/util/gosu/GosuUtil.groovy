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
package org.codenarc.util.gosu

import org.codenarc.source.SourceCode


/**
 * Contains static utility methods related to the text processing of Gosu code.
 * This is an internal class and its API is subject to change.
 */
class GosuUtil {

    private static def functionCache = [:]

    public static void clearCache() {
        functionCache.clear()
    }


    /**
     * Check if string is commented
     * @param textLine
     * @return true if in line commented
     */
    public static boolean isInlineCommented(String textLine) {
        return textLine.trim() ==~ /\/\/.*$/ || textLine.trim() ==~ /\/\*([^*]|[\r\n]|(\*+([^*\/]|[\r\n])))*\*+\//
    }

    /**
     * Check if string is a blank line
     * @param textLine
     * @return true if a blank line
     */
    public static boolean isBlankLine(String textLine) {
        return textLine ==~ /^\s*$/
    }

    /**
     * Check for the start of a block comment
     * @param textLine
     * @return true if the start of a block comment
     */
    public static boolean isStartOfBlockComment(String textLine) {
        return textLine.trim() ==~ /\/\*.*$/
    }

    /**
     * Check for the end of a block comment
     * @param textLine
     * @return true if the end of a block comment
     */
    public static boolean isEndOfBlockComment(String textLine) {
        return textLine.trim() =~ /\*\/.*$/
    }

    /**
     * Check if string has a Java style line ending
     * @param textLine
     * @return true if line ends with java style line ending
     */
    public static boolean isJavaStyleLineEnding(String textLine) {
        return textLine.trim().endsWith(";")
    }

    /**
     * Check for the start of a <code></code> example code documentation block
     * @param textLine
     * @return true if the start of a example code documentation block
     */
    public static boolean isStartOfExampleCodeInDocumentation(String textLine) {
        return textLine.trim() ==~ /.*<code>.*/
    }

    /**
     * Check for the end of a <code></code> example code documentation block
     * @param textLine
     * @return true if the end of a example code documentation block
     */
    public static boolean isEndOfExampleCodeInDocumentation(String textLine) {
        return textLine.trim() =~ /.*<\/code>.*/
    }

    /**
     * Check if string has is an override function
     * @param textLine
     * @return true if line is override function
     */
    public static boolean isOverrideFunction(String textLine) {
        return textLine.trim() ==~ /.*override.*/
    }

    /**
     * Check if the supplied line of code is a "for" statement
     * @param textLine
     * @return true if this line of code is a "for" statement
     */
    public static boolean isForStatement(String textLine) {
        return isForStatement(textLine, false)
    }

    public static boolean isForStatement(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return (textLine.trim() =~ /for\(.*$/ || textLine.trim() =~ /for \(.*$/)
    }

    /**
     * Check if the supplied line of code is a "foreach" statement
     * @param textLine
     * @return true if this line of code is a "foreach" statement
     */
    public static boolean isForeachStatement(String textLine) {
        return isForeachStatement(textLine, false)
    }

    public static boolean isForeachStatement(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return (textLine.trim() =~ /foreach\(.*$/ || textLine.trim() =~ /foreach \(.*$/)
    }

    /**
     * Check if the supplied line of code is a "while" statement
     * @param textLine
     * @return true if this line of code is a "while" statement
     */
    public static boolean isWhileStatement(String textLine) {
        return isWhileStatement(textLine, false)
    }

    public static boolean isWhileStatement(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return (textLine.trim() =~ /while\(.*$/ || textLine.trim() =~ /while \(.*$/)
    }

    /**
     * Check if the supplied line of code is a "case" statement
     * @param textLine
     * @return true if this line of code is a "case" statement
     */
    public static boolean isCatchStatement(String textLine) {
        return isCatchStatement(textLine, false)
    }

    public static boolean isCatchStatement(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return (textLine.trim() =~ /catch\(.*$/ || textLine.trim() =~ /catch \(.*$/)
    }

    /**
     * Check if the supplied line of code is a "switch" statement
     * @param textLine
     * @return true if this line of code is a "switch" statement
     */
    public static boolean isSwitchStatement(String textLine) {
        return isSwitchStatement(textLine, false)
    }

    public static boolean isSwitchStatement(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return (textLine.trim() =~ /switch\(.*$/ || textLine.trim() =~ /switch \(.*$/)
    }

    /**
     * Check if the supplied line of code is a "case" statement
     * @param textLine
     * @return true if this line of code is a "case" statement
     */
    public static boolean isCaseStatement(String textLine) {
        return isCaseStatement(textLine, false)
    }

    public static boolean isCaseStatement(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return textLine.trim() =~ /^case.*:.*$/ ||
                textLine.trim() =~ / case.*:.*$/ ||
                textLine.trim() =~ /default:.*$/ ||
                textLine.trim() =~ /default :.*$/
    }

    /**
     * Check if the supplied line of code is a "uses" statement
     * @param textLine
     * @return true if this line of code is a "uses" statement
     */
    public static boolean isUsesStatement(String textLine) {
        return isUsesStatement(textLine, false)
    }

    public static boolean isUsesStatement(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return (textLine.trim() =~ /uses .*$/ && textLine.trim().indexOf("uses") == 0)
    }

    /**
     * Check of the supplied line of text is code
     * @param textLine
     * @return true if this line is code
     */
    public static boolean isCode(String textLine) {
        if (GosuUtil.isForStatement(textLine, true)) {
            return true
        } else if (GosuUtil.isForeachStatement(textLine, true)) {
            return true
        } else if (GosuUtil.isWhileStatement(textLine, true)) {
            return true
        } else if (GosuUtil.isCatchStatement(textLine, true)) {
            return true
        } else if (GosuUtil.isSwitchStatement(textLine, true)) {
            return true
        } else if (GosuUtil.isCaseStatement(textLine, true)) {
            return true
        } else if (GosuUtil.isUsesStatement(textLine, true)) {
            return true
        } else if (GosuUtil.isIfStatement(textLine, true)) {
            return true
        } else if (GosuUtil.isTernaryConditional(textLine, true)) {
            return true
        } else if (GosuUtil.isElseStatement(textLine, true)) {
            return true
        } else if (GosuUtil.isFunctionDefinition(textLine, true)) {
            return true
        } else {
            return false
        }
    }

    /**
     * Count the number of function parameters defined
     * @param textLine
     * @return the number of lines
     */
    public static int countFunctionParameters(String textLine) {
        int startIndex = textLine.indexOf("(")

        int endIndex = textLine.indexOf(")")

        int paramCount = 0

        if ((startIndex < endIndex) && (startIndex && endIndex)) {
            String parameters = textLine.subSequence(startIndex, endIndex)
            while (parameters.contains("<")) {
                parameters = parameters.replaceAll("<[^<>]+>", "")
            }
            paramCount = parameters.tokenize(",").size()
        }

        return paramCount
    }

    /**
     * Check if the supplied line of code is an "if" statement
     * @param textLine
     * @return true if this line of code is an "if" statement
     */
    public static boolean isIfStatement(String textLine) {
        return isIfStatement(textLine, false)
    }

    public static boolean isIfStatement(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return (textLine.trim() =~ /if\(.*$/ || textLine.trim() =~ /if \(.*$/)
    }

    /**
     * Check for ternary conditional
     * @param textLine
     * @return true if this line of code is a ternary conditional
     */
    public static boolean isTernaryConditional(String textLine) {
        return isTernaryConditional(textLine, false)
    }

    public static boolean isTernaryConditional(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }


        return textLine.count(" ? ") > 0 && textLine.count(" : ") > 0
    }

    /**
     * Count the number of condition operators
     * @param textLine
     * @return the number of conditional operators found
     */
    public static int countConditionalOperators(String textLine) {
        return countConditionalOperators(textLine, false)
    }

    public static int countConditionalOperators(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        int operatorCount = 0
        operatorCount += textLine.count("||")
        operatorCount += textLine.count(" or ")
        operatorCount += textLine.count("&&")
        operatorCount += textLine.count(" and ")

        return operatorCount
    }

    /**
     * Check if the supplied line of code is an "else" statement
     * @param textLine
     * @return true if this line of code is an "else" statement
     */
    public static boolean isElseStatement(String textLine) {
        return isElseStatement(textLine, false)
    }

    public static boolean isElseStatement(String textLine, boolean ignoreComments) {
        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return (textLine.trim() =~ /else .*$/ || textLine.trim() =~ /else$/ || textLine.trim() =~ /else\{.*$/ || textLine.trim() =~ /else \{.*$/ || textLine.trim() =~ /else if\{.*$/ || textLine.trim() =~ /else if \{.*$/)
    }

    /**
     * Check if the supplied line of code is a "return" statement
     * @param textLine
     * @return true if this line of code is a "return" statement
     */
    public static isReturnStatement(String textLine) {
        return isReturnStatement(textLine, false)
    }

    public static isReturnStatement(String textLine, boolean ignoreComments) {

        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return textLine.trim() =~ /return .*$|return$/
    }

    /**
     * Check if the supplied line of code is a "function" definition
     * @param textLine
     * @return true if this line of code is a "function" definition
     */
    public static boolean isFunctionDefinition(String textLine) {
        return isFunctionDefinition(textLine, false)
    }

    public static boolean isFunctionDefinition(String textLine, boolean ignoreComments) {

        if (!ignoreComments && (GosuUtil.isInlineCommented(textLine) ||
                GosuUtil.isStartOfBlockComment(textLine) ||
                GosuUtil.isEndOfBlockComment(textLine))) {

            return false
        }

        return textLine.trim() =~ /^function [a-zA-Z0-9]*\(.*$/ ||
                textLine.trim() =~ / function [a-zA-Z0-9]*\(.*$/
    }

    /**
     * Obtain a collection of if statements within the source code
     * @param sourceCode
     * @return collection of if statement wrapper objects
     */
    public static List getIfStatements(SourceCode sourceCode) {

        def withinBlockComment = false
        def withinFunction = false

        def semiColCount = 0

        List functionList = []

        GosuCodeBlockWrapper functionWrapper = null;

        sourceCode.getLines().eachWithIndex() { obj, i ->

            // Check for block comments and ignore until block finished
            if (!withinFunction && GosuUtil.isStartOfBlockComment(obj)) {
                withinBlockComment = true
            }
            if (!withinFunction && GosuUtil.isEndOfBlockComment(obj)) {
                withinBlockComment = false
            }

            if (!withinBlockComment && !withinFunction && !GosuUtil.isBlankLine(obj) && !GosuUtil.isInlineCommented(obj)) {
                // Check for function
                if (GosuUtil.isIfStatement(obj)) {
                    withinFunction= true
                    functionWrapper = new GosuCodeBlockWrapper()
                    functionWrapper.startLineNumber = i+1
                }
            } else if (withinFunction && !GosuUtil.isBlankLine(obj)) {

                functionWrapper.lines.add(obj)

                if (!GosuUtil.isInlineCommented(obj) && obj.contains("{")) {
                    semiColCount++
                }
                if (!GosuUtil.isInlineCommented(obj) && obj.contains("}")) {
                    semiColCount--
                }
                if (semiColCount == 0) {
                    //end of function
                    withinFunction = false
                }

                // If at this stage we are no longer within the function
                if (!withinFunction) {
                    functionList.add(functionWrapper)
                    functionWrapper = null
                }
            }
        }

        return functionList
    }

    /**
     * Obtain a collection of function with the source code
     * @param sourceCode
     * @return collection of function wrapper objects
     */
    public static List getFunctions(SourceCode sourceCode) {

        List functionList = []

        //Get if the extracted functions are already in the cache
        if (functionCache.containsKey(sourceCode.getPath())) {
            functionList = functionCache.get(sourceCode.getPath())
            return functionList
        }

        def withinBlockComment = false
        def withinFunction = false
        def hasFunctionSignatureEnded = false

        def semiColCount = 0

        GosuCodeBlockWrapper functionWrapper = null;

        sourceCode.getLines().eachWithIndex() { obj, i ->

            // Check for block comments and ignore until block finished
            if (!withinFunction && GosuUtil.isStartOfBlockComment(obj)) {
                withinBlockComment = true
            }
            if (!withinFunction && GosuUtil.isEndOfBlockComment(obj)) {
                withinBlockComment = false
            }

            if (!withinBlockComment && !withinFunction && !GosuUtil.isBlankLine(obj) && !GosuUtil.isInlineCommented(obj)) {
                // Check for function
                if (obj =~ /function/) {
                    withinFunction= true
                    hasFunctionSignatureEnded = false
                    functionWrapper = new GosuCodeBlockWrapper()
                    functionWrapper.startLineNumber = i+1
                }
            }

            // Check within function
            if (withinFunction && !GosuUtil.isBlankLine(obj)) {

                functionWrapper.lines.add(obj)

                // handle case where opening brace is not on same line as declaration of function
                semiColCount += bracesIncrement(obj)
                if (semiColCount == 0 && !(obj.contains("function") && !obj.contains("{")) && hasFunctionSignatureEnded) {
                    //end of function
                    withinFunction = false
                }

                if (obj.contains(")")) {
                    hasFunctionSignatureEnded = true
                }

                // If at this stage we are no longer within the function
                if (!withinFunction) {
                    functionList.add(functionWrapper)
                    functionWrapper = null
                }
            }
        }
        functionCache.put(sourceCode.getPath(), functionList)

        return functionList
    }

    /**
     * Detects opening and closing braces within a code block
     * @param obj
     * @return increment either +1 or -1
     */
    private static int bracesIncrement(Object obj) {
        def retVal = 0
        if (!GosuUtil.isInlineCommented(obj) && obj.contains("{")) {
            retVal += 1
        }
        if (!GosuUtil.isInlineCommented(obj) && obj.contains("}")) {
            retVal -= 1
        }
        return retVal
    }

    /**
     * Count the number of occurances for a given function
     * @param functionSignature, sourceCode
     * @return the number of occurances
     */
    public static int countFunctionOccurances(String functionSignature, SourceCode sourceCode) {
        int functionCount = 0
        int startIndex = functionSignature.indexOf("function")
        int endIndex = functionSignature.indexOf("(")
        if ((startIndex < endIndex) && (startIndex && endIndex)) {
            String functionOnly = functionSignature.subSequence(startIndex, endIndex).replaceAll("function", "").trim()

            // Remove generics
            if (functionOnly.contains("<")) {
                functionOnly = functionOnly.substring(0, functionOnly.indexOf("<"))
            }

            boolean insideBlockComment = false
            sourceCode.getLines().eachWithIndex() {obj, i ->
                if (GosuUtil.isStartOfBlockComment(obj)) {
                    insideBlockComment = true
                }

                if (GosuUtil.isEndOfBlockComment(obj)) {
                    insideBlockComment = false
                }

                if (!GosuUtil.isInlineCommented(obj) && !insideBlockComment &&
                    (obj =~ /\b$functionOnly\b/)) {
                        functionCount++
                }
            }
        }

        return functionCount
    }

    /**
     * Returns a list of function parameters
     * @param functionWrapper
     * @return a list of function parameters
     */
    public static List<String> getFunctionParameters(GosuCodeBlockWrapper functionWrapper) {
        List<String> parametersWithType;
        String functionSignature = ""

        Iterator it = functionWrapper.lines.iterator()

        while(it.hasNext()) {
            String currentLine = it.next()

            if (currentLine.contains("(") && currentLine.contains(")")) {
                functionSignature = currentLine.substring(currentLine.indexOf("(") + 1, currentLine.lastIndexOf(")")).trim()
                break
            }

            if (currentLine.contains("(")) {
                functionSignature = currentLine.substring(currentLine.indexOf("(") + 1).trim()
                continue
            }

            if (currentLine.contains(")")) {
                functionSignature += " " + currentLine.substring(0, currentLine.lastIndexOf(")")).trim()
                break
            }

            functionSignature += " " + currentLine.trim()
        }

        // TODO FIXME Need to split by , where it is not within ( ) to handle blocks
        parametersWithType = functionSignature.split(",")

        return parametersWithType
    }

    /**
     * Returns a boolean indicating if the parameter is used within the function
     * @param parameter, functionWrapper
     * @return a boolean indicating if the parameter is used
     */
    public static boolean isParameterUsedWithinFunction(String parameter, GosuCodeBlockWrapper functionWrapper) {
        int numberOfTimesParameterUsed = 0

        functionWrapper.lines.each() { line ->
            String loc = ((String) line).trim()

            if (loc =~ /\b$parameter\b/ ) {
                numberOfTimesParameterUsed++
            }
        }

        return (numberOfTimesParameterUsed > 1)
    }

    public static boolean isInterface(SourceCode sourceCode) {
        return GosuUtil.getClassDefinition(sourceCode).contains("interface ")
    }

    public static String getClassDefinition(SourceCode sourceCode) {
        StringBuilder classDef = new StringBuilder()
        boolean inComment = false, inDef = false
        sourceCode.getLines().each() { obj ->
            // attempting to short circuit all this once we have the class def line(s)
            if (classDef.length() == 0 || inDef) {
                String line = ((String) obj).trim()

                if (GosuUtil.isStartOfBlockComment(line)) {
                    inComment = true
                }

                if (!(inComment || GosuUtil.isInlineCommented(line))
                        && line =~ /(public )?(abstract )?(class|interface).*/) {
                    inDef = true
                }

                if (inDef) {
                    classDef.append(line + " ")
                }

                if (line.contains("{")) {
                    inDef = false
                }

                if (GosuUtil.isEndOfBlockComment(line)) {
                    inComment = false
                }
            }
        }
        return classDef.toString().replace("{", "").trim()
    }
}
