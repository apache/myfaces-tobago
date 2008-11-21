package org.apache.myfaces.tobago.example.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.security.Identity;


@Name("authenticationManager")
public class AuthenticationManager {

  @In
  Identity identity;

  @In
  Actor actor;

  public boolean authenticate() {

    actor.setId(identity.getUsername());
    identity.addRole("authenticated");
    actor.getGroupActorIds().add("actor");
    actor.getGroupActorIds().add("developer");
    return true;
  }
}
