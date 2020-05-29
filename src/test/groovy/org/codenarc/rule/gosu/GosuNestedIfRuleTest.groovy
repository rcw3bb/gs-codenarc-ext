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

class GosuNestedIfRuleTest extends GroovyTestCase {

    void testApplyToWithViolations() {

        def code = """
            function square( n : Number ) : Number {
              return n * n
              /*
                  * if () {
                  * }
                  */
            }
            
            //function square( n : Number ) : Number {
            //  return n * n
            //}
            
            /* 
            function anotherOne( n : Number ) : Number {
              //return n * n
              /*
              * if () {
              * }
              */
            }
            */
                        
            function anotherOne( n : Number ) : Number {
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

        def rule = new GosuNestedIfRule()
        rule.maxNestedDepth = 4
        rule.applyTo(sourceCode, violations)

        assert 3 == violations.size()
    }

    void testApplyToNoViolations() {

        def code = """
        function square( n : Number ) : Number {
          return n * n
          /*
              * if () {
              * }
              */
        }
        
        //function square( n : Number ) : Number {
        //  return n * n
        //}
        
        /* 
        function anotherOne( n : Number ) : Number {
          //return n * n
          /*
          * if () {
          * }
          */
        }
        */
        
        function anotherOne( n : Number ) : Number {
          if(something) {
              if(something) {
                  if(something) {
                      
                  }    
              }
          }
        }
        
        function anotherOne( n : Number ) : Number {
             if(something) {
                  
             } else {
                 if(something) {
                      if(something) {
                          
                      }    
                  }
             }
        }
        
    """

        def sourceCode = new SourceString(code)

        def violations = []

        def rule = new GosuNestedIfRule()
        rule.maxNestedDepth = 4
        rule.applyTo(sourceCode, violations)

        assert 0 == violations.size()
    }
}
