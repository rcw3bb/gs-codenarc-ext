package org.codenarc.rule.gosu

import org.codenarc.source.SourceString

class GosuUnusedImportsRuleTest extends GroovyTestCase {
    void testApplyToWithViolations() {

        def code = """
            uses java.util.ArrayList;
            uses java.util.ArrayList;
            uses java.util.*;
            uses java.lang.Long
            uses org.foo.MyUtil
            uses gw.plugin.addressbook.IAddressBookAdapter
            uses com.guidewire.pl.web.controller.UserDisplayableException;
                        
            function anotherOne( n : Number ) : Number {
                var plugin : IAddressBookAdapter = PluginRegistry.getPluginRegistry().getPlugin(com.guidewire.cc.plugin.addressbook.IAddressBookAdapter) as IAddressBookAdapter
            
            
                static var user:String = "Guidewire Contact"
                  if(something) {
                      if(something) {
                          if(something) {
                              if(something) {
                                  if(something) {
                                      if(something) {

                                      }
                                  }
                              }
                          }
                      }
                  }
            }
            
            function anotherOne( n : Number ) : Number {
                 _contactAPI = new soap.abintegration.api.IContactAPI();

                 var util = MyUtil.doSomeMagic()
                 throw new UserDisplayableException("Error converting contact from address book to claimcenter: " + e);
            
                  if(something) {
                      if(something) {
                          
                      }
                  } else {
                      if(something) {
                          if(something) {
                              if(something) {
                                  if(something) {
                                      if(something) {
                                          if(something) {
                                                  
                                          }    
                                      }
                                  }
                              }
                          }    
                      }
                  }
                }
            
            function anotherOne( n : Number ) : Number {
                 if(something) {
                      
                 } else {
                     if(something) {
                          if(something) {
                              if(something) {
                                  if(something) {
                                      if(something) {
                                        if(something) {
              
                                          }
                                      }
                                  }
                              }
                          }    
                      }
                 }
            }
            
        """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuUnusedImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 2 == violations.size()
    }

    void testApplyToNoViolations() {
        def code = """
            uses util.TranslationObject.ClaimTO
            //uses util.regulatoryRules.*

            class SomeTestClass
            {
              construct()
              {
              }

              override function condition( context: Object ) : boolean {
                
                return false
                
                var claim = determineClaimInstance(context)
                var cto = ClaimTO.getUtil(claim)
                var recoveryInvestigation = "01"
                var RECOV_IND_NO_RECOVERY_APPLICABLE = "02"
                
                if (recoveryInvestigation != RECOV_IND_NO_RECOVERY_APPLICABLE) {
                  return false
                }
                
                var recovery = cTO.ManualRecoveriesTotal( claim )

                return (recovery > 0)
                
              }
            }
            """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuUnusedImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToWithViolationsTwo() {

        def code = """
            package util.rules.regulatory
            uses util.TranslationObject.ClaimTO
            uses util.regulatoryRules.*

            class AnotherTestClass
            {
              construct()
              {
              }

              override function condition( context: Object ) : boolean {
                
                return false
                /*
                var claim = determineClaimInstance(context)
                var cTO = ClaimTO.getUtil(claim)
                var recoveryInvestigation = cTO.RecoveryInvestigationIndicator( claim )
                var RECOV_IND_NO_RECOVERY_APPLICABLE = "02"
                
                if (recoveryInvestigation != RECOV_IND_NO_RECOVERY_APPLICABLE) {
                  return false
                }
                
                var recovery = cTO.ManualRecoveriesTotal( claim )

                return (recovery > 0)
                */
              }
            }
            """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuUnusedImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 1 == violations.size()
    }

    void testApplyToNoViolitionsWithGenerics() {
        def code = """
        uses org.magic.bunnies.TestMe
      uses org.magic.bunnies.TestMeToo
      
      class Test
      {
        function test1() {
          var testList = List<TestMe>
          var testMeToo = List< TestMeToo >
        }
      }
      """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuUnusedImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToNoViolitionsWithAnnotations() {
        def code = """
      uses org.magic.bunnies.MyAnnotation

      @MyAnnotation
      class Test
      {
        function test1() {
          // empty
        }
      }
      """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuUnusedImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

    void testApplyToNoViolitionsAsParameter() {
        def code = """
      uses org.magic.bunnies.MyParameter

      @MyAnnotation
      class Test
      {
        function test1() {
          doSomethingWith("This class", MyParameter, "is a parameter")
        }
      }
      """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuUnusedImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }
}
