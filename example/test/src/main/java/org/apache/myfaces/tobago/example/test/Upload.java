package org.apache.myfaces.tobago.example.test;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Upload {

  private static final Log LOG = LogFactory.getLog(Upload.class);

  private FileItem file;

  public String upload() {
    LOG.info("type=" + file.getContentType());
    LOG.info("file=" + file.get().length);
    LOG.info("name=" + file.getName());
    return "/tc/file/file.xhtml";
  }
  
  public FileItem getFile() {
    return file;
  }

  public void setFile(FileItem file) {
    this.file = file;
  }
}
