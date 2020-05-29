package org.codenarc.rule.gosu

import org.codenarc.source.SourceString

class GosuIllegalImportsRuleTest extends GroovyTestCase {
    void testApplyToWithViolations() {

        def code = """
            uses java.util.ArrayList;
            uses java.util.ArrayList;
            uses java.util.*;
            uses java.lang.Long
            uses gw.plugin.addressbook.IAddressBookAdapter
            uses com.guidewire.pl.web.controller.UserDisplayableException;

            function anotherOne( n : Number ) : Number {
            
              var list : ArrayList<String> = {"test1", "test2"}
            
            
              static var name : String = list[0]
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

                return null
            
                  if(something) {
                      if(something) {
                          if(something) {
                              
                          }    
                      } else {
                      
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

        def rule = new GosuIllegalImportsRule()
        rule.applyTo(sourceCode, violations)



        assert 2 == violations.size()
    }

    void testApplyToNoViolations() {
        def code = """
            package some.package.for.testing
            uses awesome.sauce.util.TranslationObject.ClaimTO
            //uses shared.regulatoryRules.*

            /**
             Test class for this test case
            */

            class Test extends TestSuper
            {
              construct()
              {
              }

              override function condition( context: Object ) : boolean {
                
              return (context as String).equalsIgnoreCase("test") ? true : false
              }
            }
            """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuIllegalImportsRule()
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }

}
