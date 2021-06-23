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

import org.codenarc.source.SourceString
import org.codenarc.util.gosu.GosuUtil

/**
 * Max return statement count rule tests
 */
class GosuReturnCountRuleTest extends GroovyTestCase {

    void testApplyToNoViolations() {

        def code = """
            function anotherOne( n : Number ) : Number {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
        """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToNoViolations")

        def violations = []

        def rule = new GosuReturnCountRule()
        rule.maxReturnCount = 2
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()

    }

    void testApplyToWithViolations() {

        def code = """
            function anotherOne( n : Number ) : Number {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
            
            function anotherOne( n : Number ) : Number 
            {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
        """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolations")

        def violations = []

        def rule = new GosuReturnCountRule()
        rule.maxReturnCount = 1
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()
    }

    void testApplyToWithViolationsDisable() {

        def code = """
            function anotherOne( n : Number ) : Number {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
            
            //codenarc-disable GosuReturnCount
            function anotherOne( n : Number ) : Number 
            {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
        """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsDisable")

        def violations = []

        def rule = new GosuReturnCountRule()
        rule.maxReturnCount = 1
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsDisableEnable() {

        def code = """
            //codenarc-disable GosuReturnCount
            function anotherOne( n : Number ) : Number {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
            //codenarc-enable GosuReturnCount
            
            function anotherOne( n : Number ) : Number 
            {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
        """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsDisableEnable")

        def violations = []

        def rule = new GosuReturnCountRule()
        rule.maxReturnCount = 1
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsDisableAll() {

        def code = """
            function anotherOne( n : Number ) : Number {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
            
            //codenarc-disable
            function anotherOne( n : Number ) : Number 
            {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
        """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsDisableAll")

        def violations = []

        def rule = new GosuReturnCountRule()
        rule.maxReturnCount = 1
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsDisableEnableAll() {

        def code = """
            //codenarc-disable
            function anotherOne( n : Number ) : Number {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
            //codenarc-enable
            
            function anotherOne( n : Number ) : Number 
            {
              if (true) {
                  return true
              } else {
                  return false
              }
            }
        """
        GosuUtil.clearCache()
        def sourceCode = new SourceString(code)
        sourceCode.setPath("testApplyToWithViolationsDisableEnableAll")

        def violations = []

        def rule = new GosuReturnCountRule()
        rule.maxReturnCount = 1
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }
}
