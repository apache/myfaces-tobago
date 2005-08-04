package com.atanion.ant.apt.compiler;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Feb 8, 2005 7:11:30 PM
 * User: bommel
 * $Id: AptCompilerAdapter.java,v 1.1 2005/03/05 15:00:36 bommel Exp $
 */
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

import com.atanion.ant.apt.Apt;


/**
 * The implementation of the apt compiler for JDK 1.5
 *
 * @since Ant 1.7
 */
public class AptCompilerAdapter extends DefaultCompilerAdapter {

    /**
     * Integer returned by the "Modern" jdk1.3 compiler to indicate success.
     */
    private static final int APT_COMPILER_SUCCESS = 0;
    public static final String APT_ENTRY_POINT = "com.sun.tools.apt.Main";
    public static final String APT_METHOD_NAME = "compile";

    protected Apt getApt() {
        return (Apt) getJavac();
    }

    static void setAptCommandlineSwitches(Apt apt, Commandline cmd) {

        if (!apt.isCompile()) {
            cmd.createArgument().setValue("-nocompile");
        }

        // Process the factory class
        String factory = apt.getFactory();
        if (factory != null) {
            cmd.createArgument().setValue("-factory");
            cmd.createArgument().setValue(factory);
        }

        // Process the factory path
        Path factoryPath = apt.getFactoryPath();
        if (factoryPath != null) {
            cmd.createArgument().setValue("-factorypath");
            cmd.createArgument().setPath(factoryPath);
        }

        File preprocessDir = apt.getPreprocessDir();
        if (preprocessDir != null) {
            cmd.createArgument().setValue("-s");
            cmd.createArgument().setFile(preprocessDir);
        }

        // Process the processor options
        Vector options = apt.getOptions();
        Enumeration elements = options.elements();
        Apt.Option opt;
        StringBuffer arg = null;
        while (elements.hasMoreElements()) {
            opt = (Apt.Option) elements.nextElement();
            arg = new StringBuffer();
            arg.append("-A").append(opt.getName());
            if (opt.getValue() != null) {
                arg.append("=").append(opt.getValue());
            }
            cmd.createArgument().setValue(arg.toString());
        }
    }

    protected void setAptCommandlineSwitches(Commandline cmd) {
        // Process the nocompile flag
        Apt apt = getApt();
        setAptCommandlineSwitches(apt, cmd);
    }

    /**
     * Run the compilation.
     *
     * @throws BuildException if the compilation has problems.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using apt compiler", Project.MSG_VERBOSE);
        Commandline cmd = setupModernJavacCommand();
        setAptCommandlineSwitches(cmd);

        // Use reflection to be able to build on all JDKs:
        try {
            Class c = Class.forName(APT_ENTRY_POINT);
            Object compiler = c.newInstance();
            Method compile = c.getMethod(APT_METHOD_NAME,
                    new Class[]{(new String[]{}).getClass()});
            int result = ((Integer) compile.invoke
                    (compiler, new Object[]{cmd.getArguments()}))
                    .intValue();
            return (result == APT_COMPILER_SUCCESS);
        } catch (BuildException be) {
            throw be;
        } catch (Exception ex) {
            throw new BuildException("Error starting apt compiler",
                    ex, location);
        }
    }
}
