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

/**
 * Max class size rules tests
 *
 */
class GosuObjectEqualityRuleTest extends GroovyTestCase {
    void testApplyToNoViolations() {

        def code = """
            if (grp.Group.PublicID == "cc:139" /* Compliance & Ops Risk */ ) {
        """

        def sourceCode = new SourceString(code)

        print "***** GOSU sourcelines = " + sourceCode.getLines().size()
        assert 3 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuObjectEqualityRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()

    }

    void testApplyToWithViolations() {

        def code = """
            if (grp.Group == Group( "cc:139" /* Compliance & Ops Risk */ )) {
            if (grp.Group==Group("cc:139" /* Compliance & Ops Risk */ )) {
            if (grp.Group == Group ( "cc:139" /* Compliance & Ops Risk */ )) {
        """

        def sourceCode = new SourceString(code)

        assert 5 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuObjectEqualityRule()
        rule.applyTo(sourceCode, violations)

        assert 3 == violations.size()

    }

    void testApplyToWithViolationsDisable() {

        def code = """
            if (grp.Group == Group( "cc:139" /* Compliance & Ops Risk */ )) {
            if (grp.Group==Group("cc:139" /* Compliance & Ops Risk */ )) {
            //codenarc-disable GosuObjectEquality
            if (grp.Group == Group ( "cc:139" /* Compliance & Ops Risk */ )) {
        """

        def sourceCode = new SourceString(code)

        assert 6 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuObjectEqualityRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()

    }

    void testApplyToWithViolationsDisableEnable() {

        def code = """
            if (grp.Group == Group( "cc:139" /* Compliance & Ops Risk */ )) {
            //codenarc-disable GosuObjectEquality
            if (grp.Group==Group("cc:139" /* Compliance & Ops Risk */ )) {
            //codenarc-enable GosuObjectEquality
            if (grp.Group == Group ( "cc:139" /* Compliance & Ops Risk */ )) {
        """

        def sourceCode = new SourceString(code)

        assert 7 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuObjectEqualityRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()

    }

    void testApplyToWithViolationsDisableAll() {

        def code = """
            if (grp.Group == Group( "cc:139" /* Compliance & Ops Risk */ )) {
            if (grp.Group==Group("cc:139" /* Compliance & Ops Risk */ )) {
            //codenarc-disable
            if (grp.Group == Group ( "cc:139" /* Compliance & Ops Risk */ )) {
        """

        def sourceCode = new SourceString(code)

        assert 6 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuObjectEqualityRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()

    }

    void testApplyToWithViolationsDisableEnableAll() {

        def code = """
            if (grp.Group == Group( "cc:139" /* Compliance & Ops Risk */ )) {
            //codenarc-disable
            if (grp.Group==Group("cc:139" /* Compliance & Ops Risk */ )) {
            //codenarc-enable
            if (grp.Group == Group ( "cc:139" /* Compliance & Ops Risk */ )) {
        """

        def sourceCode = new SourceString(code)

        assert 7 == sourceCode.getLines().size()

        def violations = []

        def rule = new GosuObjectEqualityRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()

    }

}
