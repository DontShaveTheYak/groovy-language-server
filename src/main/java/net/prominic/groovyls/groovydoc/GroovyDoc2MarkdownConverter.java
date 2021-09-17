////////////////////////////////////////////////////////////////////////////////
// Copyright 2019 Prominic.NET, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License
//
// Author: DontShaveTheYak
// No warranty of merchantability or fitness of any kind.
// Use this software at your own risk.
////////////////////////////////////////////////////////////////////////////////
package net.prominic.groovyls.groovydoc;

import java.io.Reader;
import java.lang.reflect.Field;

import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import com.overzealous.remark.Options;
import com.overzealous.remark.Options.Tables;
import com.overzealous.remark.Remark;

/**
 * Converts JavaDoc tags into Markdown equivalent.
 * <p>
 * Copied into this package from
 * <code>org.eclipse.jdt.ls.core.internal.javadoc.html.SubstitutionTextReader</code>.
 * @author Fred Bricon
 */
public class GroovyDoc2MarkdownConverter extends AbstractGroovyDocConverter {
  private static Remark remark;

  static {
    Options options = new Options();
    options.tables = Tables.MULTI_MARKDOWN;
    options.hardwraps = true;
    options.inlineLinks = true;
    options.autoLinks = true;
    options.reverseHtmlSmartPunctuation = true;
    remark = new Remark(options);
    // Stop remark from stripping file and jdt protocols in an href
    try {
      Field cleanerField = Remark.class.getDeclaredField("cleaner");
      cleanerField.setAccessible(true);

      Cleaner c = (Cleaner) cleanerField.get(remark);

      Field whitelistField = Cleaner.class.getDeclaredField("whitelist");
      whitelistField.setAccessible(true);

      Whitelist w = (Whitelist) whitelistField.get(c);

      w.addProtocols("a", "href", "file", "jdt");
      w.addProtocols("img", "src", "file");
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      System.err.println("Unable to modify jsoup to include file and jdt protocols" + e.toString());
    }
  }

  public GroovyDoc2MarkdownConverter(Reader reader) {
    super(reader);
	}

  public GroovyDoc2MarkdownConverter(String javadoc) {
    super(javadoc);
	}

  @Override
  String convert(String rawHtml) {
    return remark.convert(rawHtml);
  }
}
