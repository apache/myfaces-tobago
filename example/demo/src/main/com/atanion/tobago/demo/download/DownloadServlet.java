package com.atanion.tobago.demo.download;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DownloadServlet extends HttpServlet {

  private static final Log LOG = LogFactory.getLog(DownloadServlet.class);

  static final long serialVersionUID = 4699752061857528050L;

//  private static final long LISENCE_VALID_TIME = 1000L * 60L * 60L * 24L * 30L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ServletOutputStream out = response.getOutputStream();
    TokenManager tokenManager = (TokenManager)
        getServletContext().getAttribute("tokenManager"); // todo: get it as mbean from facesContext?
    if (tokenManager != null) {
      String token = request.getParameter("token");
      if (token != null) {
        DownloadInfo info = tokenManager.checkToken(token);
        if (info != null) {
          InputStream demoStream = getServletContext().getResourceAsStream("/WEB-INF/lib/tobago-SNAPSHOT.jar");
          if (demoStream != null) {
            try {
              byte[] license = createLicense(info.getEmail());
              ZipInputStream zipIn = new ZipInputStream(demoStream);
              ZipOutputStream zipOut = new ZipOutputStream(out);
              response.setContentType("application/octet-stream");
              zipOut.putNextEntry(new ZipEntry("resources/license.ser"));
              zipOut.write(license);
              zipOut.closeEntry();
              ZipEntry current = null;
              current = zipIn.getNextEntry();
              byte[] buffer = new byte[4096];
              while (current != null) {
                if (LOG.isDebugEnabled()) {
                  LOG.debug("ZipEntry: "+current.getName());
                }
                zipOut.putNextEntry(current);
                int l = -1;
                int bytes = 0;
                while ((l = zipIn.read(buffer)) > -1) {
                  zipOut.write(buffer, 0, l);
                  bytes += l;
                }
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Wrote "+bytes+" bytes.");
                }
                zipOut.closeEntry();
                current = zipIn.getNextEntry();
              }
              zipOut.finish();
              out.flush();
            } catch (Exception e) {
              LOG.error("Error.",e);
              response("Error: "+e.toString(), response, out);
            }
          } else {
            response("Distribution not found.", response, out);
          }
        } else {
          response("Token not valid or no longer registered.", response, out);
        }
      } else {
        response("Token parameter not found.", response, out);
      }
    } else {
      response("TokenManager not found.", response, out);
    }
  }

  private void response(String message, HttpServletResponse response, ServletOutputStream out) throws IOException {
    if (LOG.isWarnEnabled()) {
      LOG.warn(message);
    }
    response.setContentType("text/plain");
    response.setStatus(HttpServletResponse.SC_OK);
    out.println(message);
    out.flush();
  }

  public byte[] createLicense(String email) throws IOException, NoSuchAlgorithmException, SignatureException,
      CertificateException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException {
/*
    License license = new License(email, "atanion GmbH");
    license.addApplication(new ApplicationInfo("tobago"));
    license.setValidTo(new Date(System.currentTimeMillis() + LISENCE_VALID_TIME));
*/
    if (LOG.isInfoEnabled()) {
//      LOG.info("Creating license: "+license);
      LOG.info("Creating license switched off");
    }
//    InputStream keyStore = LicenseGenerator.class.getClassLoader().getResourceAsStream("key.store");
//    SignedObject signed = new LicenseGenerator().signLicense(license, keyStore, "atanion", "a7ani0n".toCharArray());
//    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
//    ObjectOutputStream oOut = new ObjectOutputStream(bOut);
//    oOut.writeObject(signed);
//    oOut.flush();
//    return bOut.toByteArray();
    return new byte[0];
  }

}
