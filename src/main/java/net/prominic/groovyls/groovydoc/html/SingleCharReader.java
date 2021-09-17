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
package net.prominic.groovyls.groovydoc.html;

import java.io.IOException;
import java.io.Reader;

/**
 * Copied into this package from
 * <code>org.eclipse.jdt.ls.core.internal.javadoc.html.SingleCharReader</code>.
 */
public abstract class SingleCharReader extends Reader {

  /**
   * @see Reader#read()
   */
  @Override
  public abstract int read() throws IOException;

  /**
   * @see Reader#read(char[],int,int)
   */
  @Override
  public int read(char cbuf[], int off, int len) throws IOException {
    int end = off + len;
    for (int i = off; i < end; i++) {
      int ch = read();
      if (ch == -1) {
        if (i == off) {
          return -1;
        }
        return i - off;
      }
      cbuf[i] = (char) ch;
    }
    return len;
  }

  /**
   * @see Reader#ready()
   */
  @Override
  public boolean ready() throws IOException {
    return true;
  }

  /**
   * Returns the readable content as string.
   *
   * @return the readable content as string
   * @exception IOException in case reading fails
   */
  public String getString() throws IOException {
    StringBuilder builder = new StringBuilder();
    int ch;
    while ((ch = read()) != -1) {
      builder.append((char) ch);
    }
    return builder.toString();
  }
}
