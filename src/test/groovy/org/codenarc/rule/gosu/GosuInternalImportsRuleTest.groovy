package org.codenarc.rule.gosu

import org.codenarc.source.SourceString

class GosuInternalImportsRuleTest extends GroovyTestCase {
    void testApplyToWithViolations() {

        def code = """
            uses java.util.ArrayList;
            uses java.util.ArrayList;
            uses java.util.*;
            uses java.lang.Long
            uses gw.plugin.addressbook.IAddressBookAdapter
            uses com.guidewire.pl.web.controller.UserDisplayableException;
            //uses com.guidewire.pl.web.controller.UserDisplayableException;

        """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuInternalImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToNoViolations() {
        def code = """
            package awesome.sauce.rules.regulatory
            uses awesome.sauce.util.TranslationObject.ClaimTO
            //uses shared.regulatoryRules.*
            """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuInternalImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToWithViolationsMultiple() {

        def code = """
            uses java.util.ArrayList;
            uses java.util.ArrayList;
            uses java.util.*;
            uses java.lang.Long
            uses gw.plugin.addressbook.IAddressBookAdapter
            uses com.guidewire.pl.web.controller.UserDisplayableException;
            uses com.guidewire.pl.web.controller.TestOnly;
            //uses com.guidewire.pl.web.controller.UserDisplayableException;
        """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuInternalImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()
    }

    void testApplyToWithViolationsMultipleDisable() {

        def code = """
            uses java.util.ArrayList;
            uses java.util.ArrayList;
            uses java.util.*;
            uses java.lang.Long
            uses gw.plugin.addressbook.IAddressBookAdapter
            uses com.guidewire.pl.web.controller.UserDisplayableException;
            //codenarc-disable GosuInternalImports
            uses com.guidewire.pl.web.controller.TestOnly;
            //uses com.guidewire.pl.web.controller.UserDisplayableException;
        """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuInternalImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsMultipleDisableEnable() {

        def code = """
            uses java.util.ArrayList;
            uses java.util.ArrayList;
            uses java.util.*;
            uses java.lang.Long
            uses gw.plugin.addressbook.IAddressBookAdapter
            //codenarc-disable GosuInternalImports
            uses com.guidewire.pl.web.controller.UserDisplayableException;
            //codenarc-enable GosuInternalImports
            uses com.guidewire.pl.web.controller.TestOnly;
            //uses com.guidewire.pl.web.controller.UserDisplayableException;
        """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuInternalImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsMultipleDisableAll() {

        def code = """
            uses java.util.ArrayList;
            uses java.util.ArrayList;
            uses java.util.*;
            uses java.lang.Long
            uses gw.plugin.addressbook.IAddressBookAdapter
            uses com.guidewire.pl.web.controller.UserDisplayableException;
            //codenarc-disable
            uses com.guidewire.pl.web.controller.TestOnly;
            //uses com.guidewire.pl.web.controller.UserDisplayableException;
        """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuInternalImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToWithViolationsMultipleDisableEnableAll() {

        def code = """
            uses java.util.ArrayList;
            uses java.util.ArrayList;
            uses java.util.*;
            uses java.lang.Long
            uses gw.plugin.addressbook.IAddressBookAdapter
            //codenarc-disable
            uses com.guidewire.pl.web.controller.UserDisplayableException;
            //codenarc-enable
            uses com.guidewire.pl.web.controller.TestOnly;
            //uses com.guidewire.pl.web.controller.UserDisplayableException;
        """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuInternalImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }
}
