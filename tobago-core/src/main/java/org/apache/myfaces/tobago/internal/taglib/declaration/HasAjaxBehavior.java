package org.apache.myfaces.tobago.internal.taglib.declaration;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

public interface HasAjaxBehavior {

  /**
   * <b>TODO: check this all</b>
   * <p>
   * Indicate the partially rendered components in a case of a submit.
   * </p>
   * <p>
   * The search depends on the number of prefixed colons in the relativeId:
   * </p>
   * <dl>
   *   <dd>number of prefixed colons == 0</dd>
   *   <dt>fully relative</dt>
   *   <dd>number of prefixed colons == 1</dd>
   *   <dt>absolute (still normal findComponent syntax)</dt>
   *   <dd>number of prefixed colons == 2</dd>
   *   <dt>search in the current naming container (same as 0 colons)</dt>
   *   <dd>number of prefixed colons == 3</dd>
   *   <dt>search in the parent naming container of the current naming container</dt>
   *   <dd>number of prefixed colons &gt; 3</dd>
   *   <dt>go to the next parent naming container for each additional colon</dt>
   * </dl>
   * <p>
   * If a literal is specified: to use more than one identifier the identifiers must be space delimited.
   * </p>
   * <p>
   * Using this in a UISheet or UITabGroup component this list indicates components to update when calling
   * internal AJAX requests like sort or paging commands.
   * Don't forget to add the sheet-id or tagGroup-id in that case, if needed.
   * </p>
   * <p>
   * You can also use @this for the component itself.
   * </p>
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.String[]")
  void setRenderPartially(String componentIds);

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.String[]")
  void setExecutePartially(String componentIds);

}
