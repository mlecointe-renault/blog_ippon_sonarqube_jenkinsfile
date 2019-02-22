/*
 * Copyright 2015 the original author or authors.
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
package org.codenarc.rule.jenkinsfile

import org.codenarc.rule.AbstractAstVisitorRule
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codenarc.rule.AbstractAstVisitor

/**
 * Check that there is no 'input' statement in a 'node' block.
 *
 * @author Maxos
 */
class CheckUserInputInNodeRule extends AbstractAstVisitorRule {

    String name = 'CheckUserInputInNode'
    int priority = 2
    Class astVisitorClass = CheckUserInputInNodeAstVisitor
}

class CheckUserInputInNodeAstVisitor extends AbstractAstVisitor {

int startBlockNode
int endBlockNode
	 
@Override
void visitMethodCallExpression(MethodCallExpression mce){

	ConstantExpression ce =  (ConstantExpression)mce.getMethod()
	

	if ("node".equals(ce.getText())){
// un élément ‘node’ !
		// le node s'étend de quelle ligne à quelle ligne ?
		startBlockNode=mce.lineNumber
		endBlockNode=mce.lastLineNumber

	}


	if ( "input".equals(ce.getText())){
		// un élément ‘input’ !
		// l'élément ‘input’ est il compris dans l'élément ‘node’ ?
		if (ce.lineNumber >= startBlockNode && ce.lineNumber<=endBlockNode){
			// création d’une violation
			addViolation(ce, 'User input detected in node !')
		}
	}
	super.visitMethodCallExpression(mce)
}


}
