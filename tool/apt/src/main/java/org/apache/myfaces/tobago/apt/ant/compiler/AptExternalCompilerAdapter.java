/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created: Feb 8, 2005 7:14:08 PM
 * User: bommel
 * $Id: AptExternalCompilerAdapter.java,v 1.1 2005/03/05 15:00:36 bommel Exp $
 */
package org.apache.myfaces.tobago.apt.ant.compiler;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
import org.apache.tools.ant.types.Commandline;
import org.apache.myfaces.tobago.apt.ant.Apt;

/**
 * The implementation of the apt compiler for JDK 1.5 using an external process
 *
 * @since Ant 1.7
 */
public class AptExternalCompilerAdapter extends DefaultCompilerAdapter {

    protected Apt getApt() {
        return (Apt) getJavac();
    }

    /**
     * Performs a compile using the Javac externally.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using external apt compiler", Project.MSG_VERBOSE);

        // Setup the apt executable
        Apt apt = getApt();
        Commandline cmd = new Commandline();
        cmd.setExecutable(apt.getAptExecutable());
        setupModernJavacCommandlineSwitches(cmd);
        AptCompilerAdapter.setAptCommandlineSwitches(apt, cmd);

        //add the files
        logAndAddFilesToCompile(cmd);

        //run
        return 0 == executeExternalCompile(cmd.getCommandline(),
                cmd.size(),
                true);
    }

}
