/*
 * Sonar Groovy Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.groovy.codenarc;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.ActiveRuleParam;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CodeNarcProfileExporter {

  private static final String AUTO_CLOSING_TAG = "\"/>\n";
  private Writer writer;

  public CodeNarcProfileExporter(Writer writer) {
    this.writer = writer;
  }

  public void exportProfile(RulesProfile profile) {
    try {
      generateXML(profile.getActiveRulesByRepository(CodeNarcRulesDefinition.REPOSITORY_KEY));

    } catch (IOException e) {
      throw new IllegalStateException("Fail to export CodeNarc profile : " + profile, e);
    }
  }

  private void generateXML(List<ActiveRule> activeRules) throws IOException {
    appendXmlHeader();
    for (ActiveRule activeRule : activeRules) {
      appendRule(activeRule);
    }
    appendXmlFooter();
  }

  private void appendXmlHeader() throws IOException {
    writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        .append("<!-- Generated by Sonar -->\n")
        .append("<ruleset xmlns=\"http://codenarc.org/ruleset/1.0\"\n")
        .append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n")
        .append("         xsi:schemaLocation=\"http://codenarc.org/ruleset/1.0 http://codenarc.org/ruleset-schema.xsd\"\n")
        .append("         xsi:noNamespaceSchemaLocation=\"http://codenarc.org/ruleset-schema.xsd\">\n");
  }

  private void appendXmlFooter() throws IOException {
    writer.append("</ruleset>");
  }

  private void appendRule(ActiveRule activeRule) throws IOException {
    String ruleKey = activeRule.getRuleKey();
    // SONARGROOV-40 : key of rule having null parameters have been suffixed with ".fixed"
    if (ruleKey.endsWith(".fixed")) {
      ruleKey = ruleKey.substring(0, ruleKey.length() - ".fixed".length());
    }
    writer.append("<rule class=\"").append(ruleKey);
    if (activeRule.getActiveRuleParams().isEmpty()) {
      writer.append(AUTO_CLOSING_TAG);
    } else {
      writer.append("\">\n");
      for (ActiveRuleParam activeRuleParam : activeRule.getActiveRuleParams()) {
        String value = activeRuleParam.getValue();
        String defaultValue = activeRuleParam.getRuleParam().getDefaultValue();
        if (StringUtils.isNotBlank(value) && !value.equals(defaultValue)) {
          writer.append("<property name=\"")
            .append(activeRuleParam.getKey())
            .append("\" value=\"")
            .append(StringEscapeUtils.escapeXml(value))
            .append(AUTO_CLOSING_TAG);
        }
      }
      writer.append("</rule>\n");
    }
  }

}
