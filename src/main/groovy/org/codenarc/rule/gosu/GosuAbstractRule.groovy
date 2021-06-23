package org.codenarc.rule.gosu

import org.codenarc.plugin.FileViolations
import org.codenarc.plugin.disablerules.DisableRulesInCommentsPlugin
import org.codenarc.results.FileResults
import org.codenarc.rule.AbstractRule
import org.codenarc.rule.Violation
import org.codenarc.source.SourceCode
import org.codenarc.source.SourceString

abstract class GosuAbstractRule extends AbstractRule {

    protected DisableRulesInCommentsPlugin filter = new DisableRulesInCommentsPlugin()

    abstract void gosuApplyTo(SourceCode sourceCode, List<Violation> violations)

    @Override
    void applyTo(SourceCode sourceCode, List<Violation> violations) {
        gosuApplyTo(sourceCode, violations)
        filterViolations(sourceCode.text, violations)
    }

    protected void filterViolations(String sourceText, List<Violation> violations) {
        SourceString sourceCode = new SourceString(sourceText)
        FileResults fileResults = new FileResults('path', violations, sourceCode)
        FileViolations fileViolations = new FileViolations(fileResults)
        filter.processViolationsForFile(fileViolations)
    }
}
