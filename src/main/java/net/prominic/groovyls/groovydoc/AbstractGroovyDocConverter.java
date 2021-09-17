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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Converts GroovyDoc comments into an output format.
 * <p>
 * Copied into this package from
 * <code>org.eclipse.jdt.ls.core.internal.javadoc.html.SingleCharReader</code>.
 */
abstract class AbstractGroovyDocConverter {

  private GroovyDoc2HTMLTextReader reader;

  private boolean read;

  private String result;

  public AbstractGroovyDocConverter(Reader reader) {
		setGroovyDoc2HTMLTextReader(reader);
	}

  public AbstractGroovyDocConverter(String groovydoc) {
		setGroovyDoc2HTMLTextReader(groovydoc == null ? null : new StringReader(groovydoc));
	}

  private void setGroovyDoc2HTMLTextReader(Reader reader) {
    if (reader == null || reader instanceof GroovyDoc2HTMLTextReader) {
      this.reader = (GroovyDoc2HTMLTextReader) reader;
    } else {
      this.reader = new GroovyDoc2HTMLTextReader(reader);
    }
  }

  public String getAsString() throws IOException {
    if (!read && reader != null) {
      String rawHtml = reader.getString();
      result = convert(rawHtml);
    }
    return result;
  }

  public Reader getAsReader() throws IOException {
    String m = getAsString();
    return m == null ? null : new StringReader(m);
  }

  abstract String convert(String rawHtml);

}
