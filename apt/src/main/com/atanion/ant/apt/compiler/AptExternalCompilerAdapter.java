package com.atanion.ant.apt.compiler;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Feb 8, 2005 7:14:08 PM
 * User: bommel
 * $Id: AptExternalCompilerAdapter.java,v 1.1 2005/03/05 15:00:36 bommel Exp $
 */
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
import org.apache.tools.ant.types.Commandline;
import com.atanion.ant.apt.Apt;

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
