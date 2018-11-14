import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

public class ConvertXmlToProperties {

  public static void main(String[] args) throws Exception {

    final String inXmlFile = args[0];
    final InputStream inStream = new FileInputStream(inXmlFile);
    final Properties properties = new Properties();
    properties.loadFromXML(inStream);

    final String outPropertiesFile = inXmlFile.replace(".xml", ".properties");
    final OutputStream outStream = new FileOutputStream(outPropertiesFile);
    final Properties sorted = new Properties() {
      @Override
      public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
      }
    };
    sorted.putAll(properties);
    sorted.store(outStream, "Converted from '" + inXmlFile + "'");

    System.out.println("Converted items: " + properties.size());
  }
}
