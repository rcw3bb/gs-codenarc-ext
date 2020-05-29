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
import org.codenarc.source.SourceString

/**
 * Util method tests for working with Gosu code
 *
 */
class GosuUtilTest extends GroovyTestCase {
	void testIsInlineCommented() {
		assert !GosuUtil.isInlineCommented("The quick brown fox")

		// This is not considered a comment because there is uncommented
		// content. This may need to change.
		assert !GosuUtil.isInlineCommented("The quick brown //fox")
		assert GosuUtil.isInlineCommented("//The quick brown fox")
		assert GosuUtil.isInlineCommented("/* This is a comment*/")
		assert GosuUtil.isInlineCommented("       /* This is a comment*/")
		assert GosuUtil.isInlineCommented("/** This is a comment*/")
		assert GosuUtil.isInlineCommented("/** This is a comment**/")
	}

	void testIsBlankLine() {
		assert !GosuUtil.isBlankLine(" //The quick brown fox")
		assert GosuUtil.isBlankLine("                      ")
		assert GosuUtil.isBlankLine("                      \n")
		assert GosuUtil.isBlankLine("                      \n\r")
	}

    void testJavaStyleLineEnding() {
        assert !GosuUtil.isJavaStyleLineEnding(" blah")
        assert GosuUtil.isJavaStyleLineEnding(" blah;")
        assert !GosuUtil.isJavaStyleLineEnding("for(i;i < 10;i++)")
    }

	void testIsStartOfBlockComment() {
		assert GosuUtil.isStartOfBlockComment("/*")
		assert GosuUtil.isStartOfBlockComment("              /**")
		assert GosuUtil.isStartOfBlockComment("              /** function")
		assert GosuUtil.isStartOfBlockComment("/* function")
		assert !GosuUtil.isStartOfBlockComment("function")
		assert !GosuUtil.isStartOfBlockComment("function /*")
	}

	void testCountFunctionParameters() {
		assert 1 == GosuUtil.countFunctionParameters("public function getDebtItemsForReserve(ReserveLine : ReserveLine) : SC_DebtItem[]")
		assert 2 == GosuUtil.countFunctionParameters("public function getDebtItemsForReserve(ReserveLine : ReserveLine, str : String) : SC_DebtItem[]")
	}

	void testIsEndOfBlockComment() {
		assert GosuUtil.isEndOfBlockComment("*/")
		assert GosuUtil.isEndOfBlockComment("              */")
		assert GosuUtil.isEndOfBlockComment("              function */")
		assert GosuUtil.isEndOfBlockComment("function   */")
		assert !GosuUtil.isEndOfBlockComment("function")

	}

	void testGetFunctions() {
		def code = """
			function square(n : Number,
                            m : Number,
                            o : Number) : Number
                            {
                return n * n
                /*
                 * if () {
                 * }
                 */
            }

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
			  return n * n
			}

			function anotherOne( n : Number ) : Number {
				  // print something
				  // {
				  /* AA */
				  int i = 0
				  return n * n
			}

		"""

		def sourceCode = new SourceString(code)
		sourceCode.setPath("testGetFunctions")

		List functions = GosuUtil.getFunctions(sourceCode)

		assert 4 == functions.size()
        // Check to make sure that multi-line signatures are handled correctly
        assert 10 == functions.get(0).lines.size()

	}

	void testGetIfStatements() {
		def code = """
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
		  return n * n
		}

		function anotherOne( n : Number ) : Number {
			  // print something
			  // {
			  /* AA */
			  if (something)
			  {
			  	if (somethingElse) {

				}
			  }
			  else
			  {
			  	if () {
				//The quick brown fox
				}
			  }
		}

		function anotherOne( n : Number ) : Number {
			  // print something
			  // {
			  /* AA */
			  if (something) {
			  //Some code
			  } else {
				if () {
					  //Some other code
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
			"""

		def sourceCode = new SourceString(code)

		List ifStatements = GosuUtil.getIfStatements(sourceCode)

		assert 6 == ifStatements.size()

	}


	void testIsIfStatement() {
		assert GosuUtil.isIfStatement("if (true) {")
		assert GosuUtil.isIfStatement("if(true) {")
		assert GosuUtil.isIfStatement("if (true) {")
		assert !GosuUtil.isIfStatement("//if")
		assert !GosuUtil.isIfStatement("// if (false) {")
		assert !GosuUtil.isIfStatement("/* if (false) {")
		assert !GosuUtil.isIfStatement("/** if (false) {")
		assert !GosuUtil.isIfStatement(" if (false) { */")
		assert !GosuUtil.isIfStatement(" if (false) { **/")
	}

	void testIsElseStatement() {
		assert GosuUtil.isElseStatement("else")
		assert GosuUtil.isElseStatement("else {")
		assert GosuUtil.isElseStatement("else if {")
		assert GosuUtil.isElseStatement("else{")
		assert GosuUtil.isElseStatement("} else {")
		assert !GosuUtil.isElseStatement("// else {")
		assert !GosuUtil.isElseStatement("/* else {")
		assert !GosuUtil.isElseStatement("/** else {")
		assert !GosuUtil.isElseStatement(" else { */")
		assert !GosuUtil.isElseStatement(" else { **/")
    assert !GosuUtil.isElseStatement(" else.", true)
	}

	void testForStatement() {
		assert GosuUtil.isForStatement("for (")
		assert GosuUtil.isForStatement("for(")
		assert !GosuUtil.isForStatement("// for (")
		assert !GosuUtil.isForStatement("/* for (")
		assert !GosuUtil.isForStatement("/** for (")
		assert !GosuUtil.isForStatement(" for ( */")
		assert !GosuUtil.isForStatement(" for ( **/")
	}

	void testForeachStatement() {
		assert GosuUtil.isForeachStatement("foreach (")
		assert GosuUtil.isForeachStatement("foreach(")
		assert !GosuUtil.isForeachStatement("each (")
		assert !GosuUtil.isForeachStatement("for (")
		assert !GosuUtil.isForeachStatement("// foreach (")
		assert !GosuUtil.isForeachStatement("/* foreach (")
		assert !GosuUtil.isForeachStatement("/** foreach (")
		assert !GosuUtil.isForeachStatement(" foreach ( */")
		assert !GosuUtil.isForeachStatement(" foreach ( **/")
	}

	void testWhileStatement() {
		assert GosuUtil.isWhileStatement("while (")
		assert GosuUtil.isWhileStatement("while(")
		assert GosuUtil.isWhileStatement("} while (")
		assert !GosuUtil.isWhileStatement("// while (")
		assert !GosuUtil.isWhileStatement("/* while (")
		assert !GosuUtil.isWhileStatement("/** while (")
		assert !GosuUtil.isWhileStatement(" while ( */")
		assert !GosuUtil.isWhileStatement(" while ( **/")
	}

	void testSwitchStatement() {
		assert GosuUtil.isSwitchStatement("switch (")
		assert GosuUtil.isSwitchStatement("switch(")
		assert !GosuUtil.isSwitchStatement("// switch (")
		assert !GosuUtil.isSwitchStatement("/* switch (")
		assert !GosuUtil.isSwitchStatement("/** switch (")
		assert !GosuUtil.isSwitchStatement(" switch ( */")
		assert !GosuUtil.isSwitchStatement(" switch ( **/")
	}

	void testCaseStatement() {
		assert !GosuUtil.isCaseStatement("case ")
		assert !GosuUtil.isCaseStatement("case")
		assert !GosuUtil.isCaseStatement("default")
		assert GosuUtil.isCaseStatement("default:")
		assert GosuUtil.isCaseStatement("default :")
		assert GosuUtil.isCaseStatement("case label1 :")
    assert GosuUtil.isCaseStatement("case label1:")
    assert GosuUtil.isCaseStatement("// case label1:", true)
    assert !GosuUtil.isCaseStatement("showcase label1:", true)
		assert !GosuUtil.isCaseStatement("// case ")
		assert !GosuUtil.isCaseStatement("/* case ")
		assert !GosuUtil.isCaseStatement("/** case ")
		assert !GosuUtil.isCaseStatement(" case  */")
		assert !GosuUtil.isCaseStatement(" case  **/")
	}

	void testConditionalOperator() {
		assert 1 == GosuUtil.countConditionalOperators("if (true || true)")
		assert 2 == GosuUtil.countConditionalOperators("if (true || true || false)")
		assert 1 == GosuUtil.countConditionalOperators("if (true && true)")
		assert 1 == GosuUtil.countConditionalOperators("if (true and true)")
		assert 1 == GosuUtil.countConditionalOperators("if (true or true)")
		assert 2 == GosuUtil.countConditionalOperators("if (true && true && false)")
		assert 0 == GosuUtil.countConditionalOperators("if (true)")
	}

	void testTernaryConditional() {
		assert GosuUtil.isTernaryConditional("result = a > b ? x : y")
		assert !GosuUtil.isTernaryConditional("result = a > b ? x  y")
	}

	void testCatchStatement() {
		assert GosuUtil.isCatchStatement("catch (")
		assert GosuUtil.isCatchStatement("catch(")
		assert !GosuUtil.isCatchStatement("// catch (")
		assert !GosuUtil.isCatchStatement("/* catch (")
		assert !GosuUtil.isCatchStatement("/** catch (")
		assert !GosuUtil.isCatchStatement(" catch ( */")
		assert !GosuUtil.isCatchStatement(" catch ( **/")
	}

	void testReturnStatement() {
		assert GosuUtil.isReturnStatement("return")
    assert GosuUtil.isReturnStatement("return ")
		assert GosuUtil.isReturnStatement("return true")
		assert !GosuUtil.isReturnStatement("//return")
		assert !GosuUtil.isReturnStatement("/* return ")
		assert !GosuUtil.isReturnStatement("/** return (")
		assert !GosuUtil.isReturnStatement(" return */")
		assert !GosuUtil.isReturnStatement(" return **/")
    assert !GosuUtil.isReturnStatement("returnVal")
	}

	void testIsCode() {
		assert GosuUtil.isCode("for (int i = 0; i < 10 ; i++)")
		assert GosuUtil.isCode("foreach (type item in set)")
		assert GosuUtil.isCode("while (true) {")
		assert GosuUtil.isCode("catch (Exception ex)")
		assert GosuUtil.isCode("switch (Str) {")
		assert GosuUtil.isCode("if (true) {")
		assert GosuUtil.isCode("result = a > b ? x : y")
		assert GosuUtil.isCode("} else {")
		assert !GosuUtil.isCode("not code")
	}


	void testUsesStatement() {
		assert !GosuUtil.isUsesStatement("var statuses : TransactionStatus[];")
		assert !GosuUtil.isUsesStatement("uses")
		assert GosuUtil.isUsesStatement("uses java.util.ArrayList; ")
		assert GosuUtil.isUsesStatement("uses com.guidewire.pl.web.controller.UserDisplayableException")
		assert !GosuUtil.isUsesStatement("UsersThis")
		assert !GosuUtil.isUsesStatement("/*uses ")
		assert !GosuUtil.isUsesStatement("/*uses")
		assert !GosuUtil.isUsesStatement("/** uses java.io;")
		assert !GosuUtil.isUsesStatement(" uses java.util.ArrayList*/")
		assert !GosuUtil.isUsesStatement(" uses java.util.ArrayList**/")
	}

  void testIsFunctionDefinition() {
   assert GosuUtil.isFunctionDefinition("private function xx(x : y) : z")
   assert GosuUtil.isFunctionDefinition("override private function xx(x : y) : z")
   assert GosuUtil.isFunctionDefinition("function xx() : z {")
   assert !GosuUtil.isFunctionDefinition("function xx yy() : z {")
   assert !GosuUtil.isFunctionDefinition("function something or other")
   assert !GosuUtil.isFunctionDefinition(" function something or other")
   assert !GosuUtil.isFunctionDefinition("my function something or other")
  }

    void testGetClassDefinitionWorksForPublicClass() {
        SourceCode code = new SourceString(
                """
package shared.webservices
uses gw.api.webservice.WSRunlevel
uses shared.webservices.services.assessmentbooking.IAssessmentBookingService
uses shared.webservices.services.assessmentbooking.AssessmentBookingServiceImpl

/**
* This class is used to supply the details of assessment bookings made at a specific
* assessment centre. This data is, in turn, used with some java code to create a beautiful
*/
@RpcWebService(WSRunlevel.NODAEMONS)
@Export
public class IAssessmentBookingAPI {
  more stuff
                """
        )

        def definition = GosuUtil.getClassDefinition(code)
        assert definition == "public class IAssessmentBookingAPI"
    }

    void testGetClassDefinitionWorksOverMultpileLines() {
        SourceCode code = new SourceString(
                """
package shared.webservices
uses gw.api.webservice.WSRunlevel
uses shared.webservices.services.assessmentbooking.IAssessmentBookingService
uses shared.webservices.services.assessmentbooking.AssessmentBookingServiceImpl

/**
* This class is used to supply the details of assessment bookings made at a specific
* assessment centre. This data is, in turn, used with some java code to create a beautiful
*/
@RpcWebService(WSRunlevel.NODAEMONS)
@Export
public class
    IAssessmentBookingAPI {
  more stuff
                """
        )

        def definition = GosuUtil.getClassDefinition(code)
        assert definition == "public class IAssessmentBookingAPI"
    }

    void testGetClassDefinitionWorksForClass() {
        SourceCode code = new SourceString(
                """
package shared.webservices
uses gw.api.webservice.WSRunlevel
uses shared.webservices.services.assessmentbooking.IAssessmentBookingService
uses shared.webservices.services.assessmentbooking.AssessmentBookingServiceImpl

/**
* This class is used to supply the details of assessment bookings made at a specific
* assessment centre. This data is, in turn, used with some java code to create a beautiful
*/
@RpcWebService(WSRunlevel.NODAEMONS)
@Export
class ActivityClosed extends shared.rules.ActivityClosed.ActivityClosed {
  more stuff
                """
        )

        def definition = GosuUtil.getClassDefinition(code)
        assert definition == "class ActivityClosed extends shared.rules.ActivityClosed.ActivityClosed"
    }


    void testGetClassDefinitionWorksForAbstractClass() {
        SourceCode code = new SourceString(
                """
package shared.webservices
uses gw.api.webservice.WSRunlevel
uses shared.webservices.services.assessmentbooking.IAssessmentBookingService
uses shared.webservices.services.assessmentbooking.AssessmentBookingServiceImpl

/**
* This class is used to supply the details of assessment bookings made at a specific
* assessment centre. This data is, in turn, used with some java code to create a beautiful
*/
@RpcWebService(WSRunlevel.NODAEMONS)
@Export
abstract class ActivityClosed extends shared.rules.ActivityClosed.ActivityClosed {
  more stuff
                """
        )

        def definition = GosuUtil.getClassDefinition(code)
        assert definition == "abstract class ActivityClosed extends shared.rules.ActivityClosed.ActivityClosed"
    }

    void testGetClassDefinitionWorksForPublicAbstractClass() {
        SourceCode code = new SourceString(
                """
package shared.webservices
uses gw.api.webservice.WSRunlevel
uses shared.webservices.services.assessmentbooking.IAssessmentBookingService
uses shared.webservices.services.assessmentbooking.AssessmentBookingServiceImpl

/**
* This class is used to supply the details of assessment bookings made at a specific
* assessment centre. This data is, in turn, used with some java code to create a beautiful
*/
@RpcWebService(WSRunlevel.NODAEMONS)
@Export
public abstract class IAssessmentBookingAPI {
  more stuff
                """
        )

        def definition = GosuUtil.getClassDefinition(code)
        assert definition == "public abstract class IAssessmentBookingAPI"
    }

    void testGetClassDefinitionWorksForInterface() {
        SourceCode code = new SourceString(
                """
package shared.webservices
uses gw.api.webservice.WSRunlevel
uses shared.webservices.services.assessmentbooking.IAssessmentBookingService
uses shared.webservices.services.assessmentbooking.AssessmentBookingServiceImpl

/**
* This class is used to supply the details of assessment bookings made at a specific
* assessment centre. This data is, in turn, used with some java code to create a beautiful
*/
@RpcWebService(WSRunlevel.NODAEMONS)
@Export
interface IReferenceDataService {
  more stuff
                """
        )

        def definition = GosuUtil.getClassDefinition(code)
        assert definition == "interface IReferenceDataService"
    }

    void testGetClassDefinitionWorksForPublicInterface() {
        SourceCode code = new SourceString(
                """
package shared.webservices
uses gw.api.webservice.WSRunlevel
uses shared.webservices.services.assessmentbooking.IAssessmentBookingService
uses shared.webservices.services.assessmentbooking.AssessmentBookingServiceImpl

/**
* This class is used to supply the details of assessment bookings made at a specific
* assessment centre. This data is, in turn, used with some java code to create a beautiful
*/
@RpcWebService(WSRunlevel.NODAEMONS)
@Export
public interface IReferenceDataService {
  more stuff
                """
        )

        def definition = GosuUtil.getClassDefinition(code)
        assert definition == "public interface IReferenceDataService"
    }

    void testIsInterfaceReturnsFalseForClass() {
        SourceCode code = new SourceString(
                """
package shared.webservices
uses gw.api.webservice.WSRunlevel
uses shared.webservices.services.assessmentbooking.IAssessmentBookingService
uses shared.webservices.services.assessmentbooking.AssessmentBookingServiceImpl

/**
* This class is used to supply the details of assessment bookings made at a specific
* assessment centre. This data is, in turn, used with some java code to create a beautiful
*/
@RpcWebService(WSRunlevel.NODAEMONS)
@Export
public abstract class IAssessmentBookingAPI {
  more stuff
                """
        )

        assertFalse GosuUtil.isInterface(code)
    }

    void testIsInterfaceReturnsTrueForInterface() {
        SourceCode code = new SourceString(
                """
package shared.webservices
uses gw.api.webservice.WSRunlevel
uses shared.webservices.services.assessmentbooking.IAssessmentBookingService
uses shared.webservices.services.assessmentbooking.AssessmentBookingServiceImpl

/**
* This class is used to supply the details of assessment bookings made at a specific
* assessment centre. This data is, in turn, used with some java code to create a beautiful
*/
@RpcWebService(WSRunlevel.NODAEMONS)
@Export
interface IReferenceDataService {
  more stuff
                """
        )

        assertTrue GosuUtil.isInterface(code)
    }
}
