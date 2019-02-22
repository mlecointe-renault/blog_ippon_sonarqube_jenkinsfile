/*
 * Copyright 2011 the original author or authors.
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
package org.codenarc.rule.basic

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule
import org.junit.Test

/**
 * Tests for EqualsOverloadedRule
 *
 * @author Hamlet D'Arcy,
 * @author Artur Gajowy
 * @author Marcin Smialek
 *
 */
class EqualsOverloadedRuleTest extends AbstractRuleTestCase {

    @Test
    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'EqualsOverloaded'
    }

    @Test
    void testSuccessScenario() {
        final SOURCE = '''
            class Person {
                boolean equals(Object other)  { true }
            }
            class Person3 {
                boolean equals(java.lang.Object other)  { true }
            }
            class Person4 {
                boolean equals(other)  { true }
            }
        '''
        assertNoViolations(SOURCE)
    }

    // TODO: WHen we have more type information then this can be moved to a passing test.
    @Test
    void testFalsePositive() {
        final SOURCE = '''
            import java.lang.Object as Foo

            class Person5 {
                boolean equals(Foo other)  { true }
            }
        '''
        assertSingleViolation(SOURCE, 5, 'boolean equals(Foo other)')
    }

    @Test
    void testSingleParameter() {
        final SOURCE = '''
            class Person {
                boolean equals(String other)  { true }
            }
        '''
        assertSingleViolation(SOURCE, 3, 'equals(String other)', 'The class Person overloads the equals method, it does not override it.')
    }

    @Test
    void testDoubleParameter() {
        final SOURCE = '''
            class Widget {
                boolean equals(Object other, String other2)  { true }
            }
        '''
        assertSingleViolation(SOURCE, 3, 'equals(Object other, String other2)', 'The class Widget overloads the equals method, it does not override it.')
    }

    @Test
    void testNoParameters() {
        final SOURCE = '''
            class Person {
                boolean equals()  { true }
            }
        '''
        assertSingleViolation(SOURCE, 3, 'equals()', 'The class Person overloads the equals method, it does not override it.')
    }

    protected Rule createRule() {
        new EqualsOverloadedRule()
    }
}
