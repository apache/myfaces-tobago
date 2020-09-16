package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyBox;

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
        return new SelectManyBoxRule.TokenSeparatorsMapper(attribute);
      }
    }
    return null;
  }

  static final class TokenSeparatorsMapper extends Metadata {

    private final TagAttribute attribute;

    public TokenSeparatorsMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext faceletContext, Object instance) {
      if (attribute.isLiteral()) {
        final String[] tokenSeparators = AbstractUISelectManyBox.parseTokenSeparators(attribute.getValue());
        ((AbstractUISelectManyBox) instance).setTokenSeparators(tokenSeparators);
      } else {
        final ValueExpression expression = attribute.getValueExpression(faceletContext, Object.class);
        ((UIComponent) instance).setValueExpression(TOKEN_SEPARATORS, expression);
      }
    }
  }
}
