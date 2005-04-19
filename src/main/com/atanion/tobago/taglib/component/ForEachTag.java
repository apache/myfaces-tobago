package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasVar;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <![CDATA[ Replacement for the JSTL <c:foreach> tag. <br>
 *     This tags iterates over the body content without setting up an exported
 *     scope variable, but replaces all occurrence's of <code>var</code> in
 *     <code>TobagoTag's ValueBinding</code> attributes.<br>
 *     All non TobagoTags are treated as they are, no
 *     replacement is done, and so no ability to use the <code>var</code> in el.
 *   ]]>
 */
@Tag(name="forEach", bodyContent="JSP")
public class ForEachTag extends BodyTagSupport implements HasVar {

  private static final Log LOG = LogFactory.getLog(ForEachTag.class);

  public static final String ALL = "-1";

  private String items;

  private String var;

  private String begin = "0";

  private String end = ALL;

  private String step = "1";

  private IterationHelper helper;

  public int doStartTag() throws JspException {
    super.doStartTag();

    final FacesContext facesContext = FacesContext.getCurrentInstance();

    if (helper == null) {
      helper = new IterationHelper();
    }


    String replacement = items.trim();
    if (replacement.startsWith("#{") && replacement.endsWith("}")) {
      replacement = replacement.substring(2, replacement.length() -1);
    }

    int position = getIntValue(begin);
    int stop = getIntValue(end);
    Object[] keys = null;
    if (stop == IterationHelper.ALL) {
      if (UIComponentTag.isValueReference(items)) {
        final Object items
            = ComponentUtil.createValueBinding(this.items, null).getValue(facesContext);
        if (items instanceof List) {
          stop = ((List)items).size();
        }
        else if (items instanceof Object[]) {
          stop = ((Object[])items).length;
        }
        else if (items instanceof Map) {
          List keyList = new ArrayList();
          for (Iterator i = ((Map)items).keySet().iterator(); i.hasNext(); ) {
            keyList.add(i.next());
          }
          keys = keyList.toArray(new Object[keyList.size()]);
          stop = keys.length;
        }
        else if (items == null) {
          if (LOG.isInfoEnabled()) {
            LOG.info("No Elements to render!");
          }
        }
        else {
          LOG.error("Illegal items object : " + items.getClass().getName());
        }
      }
      else {
        LOG.error("Not a ValueBinding : \"" + items + "\"");
      }
      if (stop == IterationHelper.ALL) {
        stop = 0;
      }
    }


    helper.init(replacement, var, position, stop, getIntValue(step), keys);

    return position < stop  ? EVAL_BODY_INCLUDE : SKIP_BODY;
  }

  public int doAfterBody() throws JspException {
    return helper.next() ? EVAL_BODY_AGAIN : SKIP_BODY;
  }


  private int getIntValue(String value) {
    int result;
    if (UIComponentTag.isValueReference(value)) {
      ValueBinding valueBinding = FacesContext.getCurrentInstance()
            .getApplication().createValueBinding(value);
       result = ComponentUtil.getIntValue(valueBinding);
    }
    else {
      result = Integer.parseInt(value);
    }
    return result;
  }


  public void release() {
    super.release();
    items = null;
    var = null;
    begin = "0";
    end = ALL;
    if (helper != null) {
      helper.reset();
    }
  }

  /**
   * <![CDATA[ <code>java.util.List</code>,
   *   <code>java.util.Map</code> or <code>Object[]</code> of items to iterate over.
   *   This <strong>must</strong> be a <code>ValueBinding</code>.
   * ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type={List.class, Map.class, Object[].class})
  public void setItems(String items) {
    this.items = items;
  }

  public void setVar(String var) {
    this.var = var;
  }


  /**
   * <![CDATA[
   *  Index at which the iteration begins.
   * ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Integer.class, defaultValue="0")
  public void setBegin(String begin) {
    this.begin = begin;
  }


  /**
   * <![CDATA[
   *  Index at which the iteration stops.
   *  Defaults to <code>items.length()</code>.
   * ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Integer.class, defaultValue="items.lenght()")
  public void setEnd(String end) {
    this.end = end;
  }


  /**
   * <![CDATA[
   *  Index increments every iteration by this value.
   * ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Integer.class, defaultValue="1")
  public void setStep(String step) {
    this.step = step;
  }

  public IterationHelper getIterationHelper() {
    return helper;
  }

  public static class IterationHelper {
    public static final int ALL = -1;

    private int position;
    private int stop;
    private int step;
    private String replacement;
    private Object[] keys;

    private Pattern pattern;

    public IterationHelper() {
      reset();
    }

    public boolean next(){
      position += step;
      return position < stop;
    }

    public String replace(String binding) {
      final String result = pattern.matcher(binding).replaceAll(
          "$1" + replacement +"["
          + (keys == null ? Integer.toString(position) : "'" + keys[position] + "'")
          + "]$2");
      if (LOG.isDebugEnabled()) {
        LOG.debug("transform \"" + binding + "\" to \"" + result + "\"");
      }
      return result;
    }

    public void reset() {
      position = 0;
      stop = ALL;
      step = 1;
      replacement = null;
      keys = null;
    }


    public void init(String replacement, String var, int position, int stop,
                     int step, Object[] keys) {
      this.replacement = replacement;
      this.position = position;
      this.stop = stop;
      this.step = step;
      this.keys = keys;
      pattern = Pattern.compile("(\\W *?[^\\.] *?)" + var + "( *?\\W)");
    }
  }
}
