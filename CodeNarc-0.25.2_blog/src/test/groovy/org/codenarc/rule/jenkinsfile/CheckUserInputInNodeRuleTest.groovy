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

import org.codenarc.rule.Rule
import org.junit.Test
import org.codenarc.rule.AbstractRuleTestCase

/**
 * Tests for CheckUserInputInNodeRule
 *
 * @author Maxos
 */
class CheckUserInputInNodeRuleTest extends AbstractRuleTestCase {

    @Test
    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'CheckUserInputInNode'
    }

    @Test
    void testNoViolations() {
        final SOURCE = '''
        	stage('test'){
		userInput = input(id: 'userInput',
		message: 'Do you want to release this build?',
		parameters: [[$class: 'PasswordParameterDefinition',
			defaultValue: "",
			name: 'password',
			description: 'Password']])
			
		sh "echo User Input is" + userInput	
	}

        '''
        assertNoViolations(SOURCE)
    }

    @Test
    void testSingleViolation() {
        final SOURCE = '''
            node('DOCKER'){
		stage('test'){
			userInput = input(id: 'userInput',
			message: 'Do you want to release this build?',
			parameters: [[$class: 'PasswordParameterDefinition',
				defaultValue: "",
				name: 'password',
				description: 'Password']])
			
			sh "echo User Input is" + userInput
		}
	}

        '''
assertSingleViolation(SOURCE, 4 )

    }

  

    protected Rule createRule() {
        new CheckUserInputInNodeRule()
    }
}
