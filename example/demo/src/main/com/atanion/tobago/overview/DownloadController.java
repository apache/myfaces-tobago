/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 04.06.2004 09:27:14.
 * $Id: DownloadController.java 1224 2005-04-21 08:54:58 +0200 (Do, 21 Apr 2005) lofwyr $
 */
package com.atanion.tobago.overview;

import com.atanion.tobago.demo.download.DownloadInfo;
import com.atanion.tobago.demo.download.TokenManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class DownloadController {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(DownloadController.class);

// ///////////////////////////////////////////// attribute

  private boolean accepted = false;

  private String serverUrl;
  private String email = "";

// ///////////////////////////////////////////// constructor

  public DownloadController() {
    if (LOG.isInfoEnabled()) {
      LOG.info("DownloadController instanciated.");
    }
  }

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// action-listener

// ///////////////////////////////////////////// action

  public String sendLink() {
    if (LOG.isInfoEnabled()) {
      LOG.info("email = " + email);
      LOG.info("accepted = " + accepted);
    }
    FacesContext facesContext = FacesContext.getCurrentInstance();
    boolean valid = true;
    if (!accepted) {
      facesContext.addMessage("ctrl_accepted", new FacesMessage("license not accepted"));
      valid = false;
    }
    try {
      new InternetAddress(email, true);
    } catch (AddressException e) {
      valid = false;
      if (LOG.isDebugEnabled()) {
        LOG.debug("bad email");
      }
      facesContext.addMessage("ctrl_email", new FacesMessage("email address bad"));
    }
    if (valid) {
      Session session = Session.getInstance(System.getProperties());
      URLName url = new URLName("smtp", "shrek", -1, null, null, null);
      Transport transport = null;
      try {
        transport = session.getTransport(url);
        transport.connect();
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress("tobago@atanion.com"));
        mimeMessage.setSubject("Evaluierung Tobago - Download");
        TokenManager tokenManager = TokenManager.getCurrentInstance(facesContext);
        String token = tokenManager.registerToken(new DownloadInfo(email));
        String link = serverUrl + "download-tobago.zip?token=" + token;
        if (LOG.isInfoEnabled()) {
          LOG.info("Download-Link:\n   "+link);
        }
        StringBuffer text = new StringBuffer("Sie können Tobago über die URL\n\n");
        text.append("  ").append(link).append("\n\nherunterladen. Der Link ist ").append(TokenManager.TOKEN_VALID_TIME/(1000*60*60));
        text.append(" Stunden gültig.");
        mimeMessage.setText(text.toString(), "ISO-8859-1");
        mimeMessage.addRecipients(MimeMessage.RecipientType.TO, email);
        mimeMessage.addRecipients(MimeMessage.RecipientType.BCC, "gf@atanion.com");
        mimeMessage.saveChanges();
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        return "download_success";
      } catch (Exception e) {
        LOG.error("error sending message", e);
        return "download_error";
      } finally {
        if (transport != null && transport.isConnected()) {
          try {
            transport.close();
          } catch (MessagingException e) {
            LOG.error("error closing transport", e);
          }
        }
      }
    }
    return null;
  }

//  License license = new License(email, "atanion GmbH");
//        license.addApplication(new ApplicationInfo("tobago"));
//        license.setValidTo(new Date(System.currentTimeMillis() + LISENCE_VALID_TIME));
//        InputStream keyStore = LicenseGenerator.class.getClassLoader().getResourceAsStream("key.store");
//        SignedObject signed = new LicenseGenerator().signLicense(license, keyStore, "atanion", "a7ani0n".toCharArray());
//        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
//        oOut = new ObjectOutputStream(bOut);
//        oOut.writeObject(signed);
//        oOut.flush();
//        final byte[] bytes = bOut.toByteArray();
//
//  DataSource dataSource = new DataSource() {
//    public InputStream getInputStream() throws IOException {
//      return new ByteArrayInputStream(bytes);
//    }
//
//    public OutputStream getOutputStream() throws IOException {
//      throw new IOException("Operation not implemented.");
//    }
//
//    public String getContentType() {
//      return "application/x-java-serialized-object";
//    }
//
//    public String getName() {
//      return LicenseCheck.LICENSE_FILE_NAME;
//    }
//  };
//  MimeMultipart multi = new MimeMultipart();
//  multi.addBodyPart(textBody);
//  BodyPart body = new MimeBodyPart();
//  body.setDataHandler(new DataHandler(dataSource));
//  body.setDisposition(Part.ATTACHMENT);
//  body.setFileName(dataSource.getName());
//  multi.addBodyPart(body);


// ///////////////////////////////////////////// bean getter + setter

  public String getServerUrl() {
    return serverUrl;
  }

  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  public boolean isAccepted() {
    return accepted;
  }

  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
