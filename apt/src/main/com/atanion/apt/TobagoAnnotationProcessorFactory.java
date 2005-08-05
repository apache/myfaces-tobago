package com.atanion.apt;

import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.Set;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 27, 2005 4:27:27 PM
 * User: bommel
 * $Id: TobagoAnnotationProcessorFactory.java,v 1.1 2005/05/11 15:20:34 bommel Exp $
 */
public class TobagoAnnotationProcessorFactory implements AnnotationProcessorFactory  {

  private TobagoAnnotationProcessor annotationProcessor = null;

  private static final Collection<String> supportedAnnotations
      = Collections.unmodifiableCollection(Arrays.asList("com.atanion.util.annotation.Tag",
          "com.atanion.util.annotation.TagAttribute", "com.atanion.util.annotation.Taglib",
          "com.atanion.util.annotation.UIComponentTagAttribute" ));

  private static final Collection<String> supportedOptions = Collections.emptyList();

  public Collection<String> supportedAnnotationTypes() {
    return supportedAnnotations;
  }

  public Collection<String> supportedOptions() {
    return supportedOptions;
  }

  public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds,
      AnnotationProcessorEnvironment env) {
    if (annotationProcessor == null) {
      annotationProcessor = new TobagoAnnotationProcessor(atds, env);
    }
    return annotationProcessor;
  }


}
