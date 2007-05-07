package org.apache.myfaces.tobago.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;

//
// Buffering scheme: we use a tremendously simple buffering
// scheme that greatly reduces the number of calls into the
// Writer/PrintWriter.  In practice this has produced significant
// measured performance gains (at least in JDK 1.3.1).  We only
// support adding single characters to the buffer, so anytime
// multiple characters need to be written out, the entire buffer
// gets flushed.  In practice, this is good enough, and keeps
// the core simple.
//

/**
 * User: lofwyr
 * Date: 07.05.2007 12:03:26
 */
public class ResponseWriterBuffer {

  private static final Log LOG = LogFactory.getLog(ResponseWriterBuffer.class);

  private static final int BUFFER_SIZE = 64;

  private final char[] buff;

  private int bufferIndex;

  private Writer writer;

  public ResponseWriterBuffer(Writer writer) {
    buff = new char[BUFFER_SIZE];
    this.writer = writer;
  }

  /**
   * Add a character to the buffer, flushing the buffer if the buffer is
   * full, and returning the new buffer index
   */
  public void addToBuffer(final char ch) throws IOException {
    if (bufferIndex >= BUFFER_SIZE) {
      writer.write(buff, 0, bufferIndex);
      bufferIndex = 0;
    }

    buff[bufferIndex++] = ch;
  }

  /**
   * Flush the contents of the buffer to the output stream
   * and return the reset buffer index
   */
  public void flushBuffer() throws IOException {
    if (bufferIndex > 0) {
      writer.write(buff, 0, bufferIndex);
    }
    bufferIndex = 0;
  }
}
