package com.atanion.tobago.taglib.component;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.atanion.tobago.component.ComponentUtil;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;
import javax.faces.webapp.UIComponentTag;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ForEachTag extends BodyTagSupport {

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


    helper.init(replacement, var, position, stop, getIntValue(step));

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

  public void setItems(String items) {
    this.items = items;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public void setBegin(String begin) {
    this.begin = begin;
  }

  public void setEnd(String end) {
    this.end = end;
  }

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
          "$1" + replacement +"[" + position + "]");
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
    }


    public void init(String replacement, String var, int position, int stop, int step) {
      this.replacement = replacement;
      this.position = position;
      this.stop = stop;
      this.step = step;
      pattern = Pattern.compile("([^\\.] *?)" + var);
    }
  }
}
