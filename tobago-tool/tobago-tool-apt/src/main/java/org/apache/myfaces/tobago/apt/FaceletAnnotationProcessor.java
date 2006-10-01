package org.apache.myfaces.tobago.apt;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.util.DeclarationVisitors;

import java.util.Collection;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 30.03.2006
 * Time: 19:19:14
 */
public class FaceletAnnotationProcessor implements AnnotationProcessor {

  private final AnnotationProcessorEnvironment env;
  private final Set<AnnotationTypeDeclaration> atds;

  public FaceletAnnotationProcessor(Set<AnnotationTypeDeclaration> atds,
      AnnotationProcessorEnvironment env) {
    this.atds = atds;
    this.env = env;
    this.env.getMessager().printNotice("Starting annotation process");

  }

  public void process() {
    FaceletAnnotationVisitor visitor = new FaceletAnnotationVisitor(env);

    for (AnnotationTypeDeclaration atd : atds) {
      env.getMessager().printNotice("Collecting annotation "+atd);
      Collection<Declaration> decls = env.getDeclarationsAnnotatedWith(atd);
      for (Declaration decl : decls) {
        decl.accept(DeclarationVisitors.getDeclarationScanner(visitor, DeclarationVisitors.NO_OP));
      }
    }
    try {
      visitor.process();  
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
