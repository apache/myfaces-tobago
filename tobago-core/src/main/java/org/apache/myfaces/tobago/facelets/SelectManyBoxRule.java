package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyBox;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

public class SelectManyBoxRule extends MetaRule {

  public static final String TOKEN_SEPARATORS = "tokenSeparators";

  public static final SelectManyBoxRule INSTANCE = new SelectManyBoxRule();

  public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(AbstractUISelectManyBox.class)) {
      if (TOKEN_SEPARATORS.equals(name)) {
        return new SelectManyBoxRule.SelectManyBoxMapper(attribute);
      }
    }
    return null;
  }

  static final class SelectManyBoxMapper extends Metadata {

    private final TagAttribute attribute;

    public SelectManyBoxMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext faceletContext, Object instance) {
      if (attribute.isLiteral()) {
        final String[] components = AbstractUISelectManyBox.parseTokenSeparators(attribute.getValue());
        ((AbstractUISelectManyBox) instance).setTokenSeparators(components);
      } else {
        final ValueExpression expression = attribute.getValueExpression(faceletContext, Object.class);
        ((UIComponent) instance).setValueExpression(TOKEN_SEPARATORS, expression);
      }
    }
  }
}
