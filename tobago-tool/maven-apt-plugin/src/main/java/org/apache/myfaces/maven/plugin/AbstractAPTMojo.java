package org.apache.myfaces.maven.plugin;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.javac.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.FileUtils;

/**
 * @author <a href="mailto:jubu@volny.cz">Juraj Burian</a>
 * @version $Id$
 */
public abstract class AbstractAPTMojo extends AbstractMojo
{
    /**
     * PATH_SEPARATOR.
     */
    private static final String PATH_SEPARATOR = System.getProperty( "path.separator" );
    /**
     * FILE_SEPARATOR.
     */
    private static final String FILE_SEPARATOR = System.getProperty( "file.separator" );

    /**
     * Integer returned by the Apt compiler to indicate success.
     */
    private static final int APT_COMPILER_SUCCESS = 0;

    /**
     * class in tools.jar that implements APT
     */
    private static final String APT_ENTRY_POINT = "com.sun.tools.apt.Main";

    /**
     * method used for apt.
     */
    private static final String APT_METHOD_NAME = "process";

    /**
     * old method used for apt.
     */
    private static final String APT_METHOD_NAME_OLD = "compile";

    /**
     * store info about modification of system classpath for Apt compiler
     */
    private static boolean isClasspathModified;

   /**
    * Working directory when APT compiler is forked
    * @parameter default-value="${basedir}"
    * @since 1.0.10
    */
    private File workingDir;

    /**
     *  A List of targetFiles for SingleSourceTargetMapping
     *
     * @parameter
     */
    private List targetFiles;

   /**
     * enables resource filtering for generated resources
     *
     * @parameter    default-value="false"
     */
    private boolean resourceFiltering;

    /**
     *  targetPath for generated resources
     *
     * @parameter
     */
    private String resourceTargetPath;

    /**
     * Whether to include debugging information in the compiled class files. The
     * default value is true.
     * 
     * @parameter expression="${maven.compiler.debug}" default-value="true"
     * @readonly
     */
    private boolean debug;

    /**
     * Comma separated list of "-A" options: Next two examples are equivalent:
     * 
     * <pre>
     *         &lt;A&gt;-Adebug,-Aloglevel=3&lt;/A&gt;
     * </pre>
     * <pre>
     *         &lt;A&gt;debug, loglevel=3&lt;/A&gt;
     * </pre>
     *
     * @parameter alias="A"
     */
    private String aptOptions;

    /**
     * Output source locations where deprecated APIs are used
     * 
     * @parameter
     */
    private boolean showDeprecation;

    /**
     * Output warnings
     * 
     * @parameter
     */
    private boolean showWarnings;

    /**
     * The -encoding argument for the Apt
     * 
     * @parameter
     */
    private String encoding;

    /**
     * run Apt in verbode mode
     * 
     * @parameter expression="${verbose}" default-value="false"
     */
    private boolean verbose;

    /**
     * The -nocompile argument for the Apt
     * 
     * @parameter default-value="false"
     */
    private boolean nocompile;

    /**
     * The granularity in milliseconds of the last modification date for testing
     * whether a source needs recompilation
     * 
     * @parameter expression="${lastModGranularityMs}" default-value="0"
     */
    private int staleMillis;

    /**
     * Name of AnnotationProcessorFactory to use; bypasses default discovery
     * process
     * 
     * @parameter
     */
    private String factory;

    /**
     * Temporary directory that contain the files from the plugin.
     *
     * @parameter expression="${project.build.directory}/maven-apt-plugin"
     * @required
     * @readonly
     */
    private File tempRoot;

    /**
     * The directory to run the compiler from if fork is true.
     * 
     * @parameter expression="${basedir}"
     * @required
     * @readonly
     */
    private File basedir;

    /**
     * Allows running the compiler in a separate process.
     * If "false" it uses the built in compiler, while if "true" it will use an executable.
     *
     * @parameter default-value="false"
     */
    private boolean fork;
    /**
     * Force apt call without staleness checking.
     *
     * @parameter default-value="false"
     */
    private boolean force;


    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The maven project.
     * @return MavenProject
     */
    public MavenProject getProject()
    {
        return project;
    }
    /**
     * Force apt call without staleness checking.
     * @return force
     */
    public boolean isForce()
    {
        return force;
    }
    /**
     * run Apt in verbode mode
     * @return verbose
     */
    public boolean isVerbose()
    {
        return verbose;
    }

    /**
     * The granularity in milliseconds of the last modification date for testing
     * whether a source needs recompilation
     * @return staleMillis
     */
    public int getStaleMillis()
    {
        return staleMillis;
    }
   /**
    *  A List of targetFiles for SingleSourceTargetMapping
    * @return a List of TargetFiles
    */
    protected List getTargetFiles()
    {
        return targetFiles;
    }
   /**
    * enables resource filtering for generated resources
    * @return resourceFiltering
    */
    protected boolean isResourceFiltering()
    {
        return resourceFiltering;
    }
   /**
    *  targetPath for generated resources
    * @return resouceTargetPath
    */
    protected String getResourceTargetPath()
    {
        return resourceTargetPath;
    }
    /**
     *  classpath elements.
     * @return a List of classPathElements
     */
    protected abstract List getClasspathElements();
    /**
     * The source directories containing the sources to be compiled.
     * @return a List of CompileSourceRoots
     */
    protected abstract List getCompileSourceRoots();

    /**
     * The extra source directories containing the source to be processed.
     * @return a List of AptSourceRoots
     */
    protected abstract List getAptSourceRoots();

    /**
     * The directory where compiled classes go.
     * @return outputDirector
     */
    protected abstract File getOutputDirectory();

   /**
    * The directory where generated code go.
    * @return generated
    */
    protected abstract String getGenerated();

  /**
   *
   * @return a SourceInclusionScanner
   */
    protected abstract SourceInclusionScanner getSourceInclusionScanner();

  /**
   * execute
   * @throws MojoExecutionException
   */
    public void execute() throws MojoExecutionException 
    {
        getLog().debug( "Using apt compiler" );
        Commandline cmd = new Commandline();
        int result = APT_COMPILER_SUCCESS;
        StringWriter writer = new StringWriter();

        // Use reflection to be able to build on all JDKs:
        try
        {
            // init comand line
            setAptCommandlineSwitches( cmd );
            setAptSpecifics( cmd );
            setStandards( cmd );
            setClasspath( cmd );
            List sourceFiles = new ArrayList();
            if ( !fillSourcelist( sourceFiles ) )
            {
                if ( getLog().isDebugEnabled() )
                {
                    getLog().debug( "there are not stale sources." );
                }
                return;
            }
            else
            {
                if ( fork )
                {

                     if ( !tempRoot.exists() )
                     {
                         tempRoot.mkdirs();
                     }
                     File file = new File( tempRoot , "files" );
                     if ( !getLog().isDebugEnabled() )
                     {
                         file.deleteOnExit();
                     }
                     try
                     {
                         FileUtils.fileWrite( file.getAbsolutePath(),
                                 StringUtils.join( sourceFiles.iterator(), "\n" ) );
                         cmd.createArgument().setValue( '@' + file.getPath() );
                     }
                     catch ( IOException e )
                     {
                         throw new MojoExecutionException( "Unable to write temporary file for command execution", e );
                     }
                }
                else
                {
                    Iterator sourceIt = sourceFiles.iterator();
                    while ( sourceIt.hasNext() )
                    {
                        cmdAdd( cmd, (String) sourceIt.next() );
                    }
                }
            }
            if ( fork )
            {
                if ( getLog().isDebugEnabled() )
                {
                    getLog().debug( "Working dir: " + workingDir.getAbsolutePath() );
                }
                cmd.setWorkingDirectory( workingDir.getAbsolutePath() );
                cmd.setExecutable( getAptPath() );

                if ( getLog().isDebugEnabled() )
                {
                    getLog().debug( "Invoking apt with cmd " + Commandline.toString( cmd.getShellCommandline() ) );
                }

                CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
                try
                {
                    int exitCode = CommandLineUtils.executeCommandLine( cmd, new DefaultConsumer(), err );

                    getLog().error( err.getOutput() );

                    if ( exitCode != 0 )
                    {
                        throw new MojoExecutionException( "Exit code: " + exitCode + " - " + err.getOutput() );
                    }
                 }
                 catch ( CommandLineException e )
                 {
                     throw new MojoExecutionException( "Unable to execute apt command", e );
                 }
            }
            else
            {
                // we need to have tools.jar in lasspath
                // due to bug in Apt compiler, system classpath must be modified but in future:
                // TODO try separate ClassLoader (see Plexus compiler api)
                if ( !isClasspathModified )
                {
                    URL toolsJar = new File( System.getProperty( "java.home" ),
                        "../lib/tools.jar" ).toURL();
                    Method m = URLClassLoader.class.getDeclaredMethod( "addURL",
                          new Class[] { URL.class } );
                    m.setAccessible( true );
                    m.invoke( this.getClass().getClassLoader()
                        .getSystemClassLoader(), new Object[] { toolsJar } );
                    isClasspathModified = true;
                }
                Class c = this.getClass().forName( APT_ENTRY_POINT ); // getAptCompilerClass();
                Object compiler = c.newInstance();
                if ( getLog().isDebugEnabled() )
                {
                    getLog().debug( "Invoking apt with cmd " + cmd.toString() );
                }
                try
                {
                    Method compile = c.getMethod( APT_METHOD_NAME, new Class[] {
                        PrintWriter.class, ( new String[] {} ).getClass() } );
                    result = ( ( Integer ) //
                    compile.invoke( compiler, new Object[] { new PrintWriter( writer ),
                          cmd.getArguments() } ) ).intValue();
                }
                catch ( NoSuchMethodException e )
                {
                  // ignore
                    Method compile = c.getMethod( APT_METHOD_NAME_OLD, new Class[] {
                        ( new String[] {} ).getClass(),  PrintWriter.class } );
                    result = ( ( Integer ) //
                    compile.invoke( compiler, new Object[] {
                          cmd.getArguments(),  new PrintWriter( writer ) } ) ).intValue();
                }
            }


        }
        catch ( Exception ex )
        {
            throw new MojoExecutionException( "Error starting apt compiler", ex );
        }
        finally
        {
            if ( result != APT_COMPILER_SUCCESS )
            {
                throw new MojoExecutionException( this, "Compilation error.", writer.getBuffer().toString() );
            }
            if ( getLog().isDebugEnabled() )
            {
                String r = writer.getBuffer().toString();
                if ( 0 != r.length() )
                {
                    getLog().debug( r );
                }
                getLog().debug( "Apt finished." );
            }
        }
    }

  /**
   *
   * @param cmd
   */
    private void setAptCommandlineSwitches( Commandline cmd )
    {
        if ( null == aptOptions )
        {
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer( aptOptions.trim(), "," );
        while ( tokenizer.hasMoreElements() )
        {
            String option = tokenizer.nextToken().trim();
            if ( !option.startsWith( "-A" ) )
            {
                option = "-A" + option;
            }
            cmdAdd( cmd, option );
        }
    }

  /**
   *
   * @param cmd
   * @throws MojoExecutionException
   */
    private void setAptSpecifics( Commandline cmd ) throws MojoExecutionException
    {
        try
        {
            String g = basedir.getAbsolutePath() + FILE_SEPARATOR
                    + getGenerated();
            File generatedDir = new File( g );
            cmdAdd( cmd, "-s", generatedDir.getCanonicalPath() );
            if ( !generatedDir.exists() )
            {
                generatedDir.mkdirs();
            }
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( //
                    "Generated directory is invalid.", e );
        }
        if ( nocompile )
        {
            cmdAdd( cmd, "-nocompile" );
        }
        if ( null != factory && 0 != factory.length() )
        {
            cmdAdd( cmd, "-factory", factory );
        }
    }

  /**
   *
   * @param cmd
   * @throws MojoExecutionException
   */
    private void setStandards( Commandline cmd ) throws MojoExecutionException
    {
        if ( debug )
        {
            cmdAdd( cmd, "-g" );
        }
        if ( !showWarnings )
        {
            cmdAdd( cmd, "-nowarn" );
        }
        if ( showDeprecation )
        {
            cmdAdd( cmd, "-depecation" );
        }
        if ( null != encoding )
        {
            cmdAdd( cmd, "-encoding", encoding );
        }
        if ( verbose )
        {
            cmdAdd( cmd, "-verbose" );
        }

        // add sourcepath directory
        setSourcepath(cmd);
        // add output directory
        try
        {
            if ( !getOutputDirectory().exists() )
            {
                getOutputDirectory().mkdirs();
            }
            cmdAdd( cmd, "-d", getOutputDirectory().getCanonicalPath() );
        }
        catch ( Exception ex )
        {
            throw new MojoExecutionException( //
                    "Output directory is invalid.", ex );
        }
    }

    private void setSourcepath(Commandline cmd) {
        StringBuffer buffer = new StringBuffer();
        for ( Iterator it = getCompileSourceRoots().iterator(); it.hasNext();)
        {
            buffer.append( it.next() );
            if ( it.hasNext() )
            {
                buffer.append( PATH_SEPARATOR );
            }
        }
        if ( getAptSourceRoots() != null)
        {
            if (buffer.length() > 0 && getAptSourceRoots().size() > 0)
            {
                buffer.append( PATH_SEPARATOR );
            }
            for ( Iterator it = getAptSourceRoots().iterator(); it.hasNext();)
            {
                buffer.append( it.next() );
                if ( it.hasNext() )
                {
                    buffer.append( PATH_SEPARATOR );
                }
            }
        }
        cmdAdd( cmd, "-sourcepath", buffer.toString() );
    }

  /**
   *
   * @param cmd
   * @return
   * @throws MojoExecutionException
   */
    private boolean fillSourcelist( List cmd ) throws MojoExecutionException
    {
        boolean has = false;
        // sources ....
        Iterator it = getCompileSourceRoots().iterator();
        if ( getLog().isDebugEnabled() )
        {
            getLog().debug( "Checking sourcepath" );
        }
        while ( it.hasNext() )
        {
            File srcFile = new File( (String) it.next() );
            has = addIncludedSources( srcFile, cmd, has );
        }
        List aptSourcesRoots = getAptSourceRoots();
        if ( aptSourcesRoots != null )
        {
            it = aptSourcesRoots.iterator();
            while ( it.hasNext() )
            {
                File srcFile = new File( (String) it.next() );
                has = addIncludedSources( srcFile, cmd, has );
            }
        }
        return has;
    }

  /**
   *
   * @param srcFile
   * @param cmd
   * @param has
   * @return
   * @throws MojoExecutionException
   */
    private boolean addIncludedSources( File srcFile, List cmd, boolean has ) throws MojoExecutionException
    {
        if ( getLog().isDebugEnabled() )
        {
            getLog().debug( "Checking sourcepath in " + srcFile );
        }
        if ( srcFile.isDirectory() )
        {
            Collection sources = null;
            try
            {
                sources = //
                    getSourceInclusionScanner().getIncludedSources( srcFile,
                        getOutputDirectory() );
            }
            catch ( Exception ex )
            {
                throw new MojoExecutionException(
                    "Can't agregate sources.", ex );
            }
            if ( getLog().isDebugEnabled() )
            {
                getLog().debug(
                        "sources from: " + srcFile.getAbsolutePath() );
                String s = "";
                for ( Iterator jt = sources.iterator(); jt.hasNext();)
                {
                     s += jt.next() + "\n";
                }
                getLog().debug( s );
            }
            Iterator jt = sources.iterator();
            while ( jt.hasNext() )
            {
                File src = (File) jt.next();
                if ( fork )
                {
                    cmd.add( quotedPathArgument( src.getAbsolutePath() ) );
                }
                else
                {
                    cmd.add( src.getAbsolutePath() );
                }
                has = true;
            }
        }
        return has;
    }

  /**
   *
   * @param cmd
   * @throws MojoExecutionException
   * @throws DependencyResolutionRequiredException
   */
    private void setClasspath( Commandline cmd ) throws MojoExecutionException, DependencyResolutionRequiredException
    {
        StringBuffer buffer = new StringBuffer();
        for ( Iterator it = getClasspathElements().iterator(); it.hasNext();)
        {
            buffer.append( it.next() );
            if ( it.hasNext() )
            {
                buffer.append( PATH_SEPARATOR );
            }
        }
        cmdAdd( cmd, "-classpath", buffer.toString() );
    }

    /**
     *
     * @param cmd
     * @param arg
     */
    private void cmdAdd( Commandline cmd, String arg )
    {
        /**
         * OBSOLETE
         * if( true == getLog().isDebugEnabled() ) { getLog().debug(
         * arg ); }
         */
         cmd.createArgument().setValue( arg );
        //cmd.add( arg );
    }

  /**
   *
   * @param cmd
   * @param arg1
   * @param arg2
   */
    private void cmdAdd( Commandline cmd, String arg1, String arg2 )
    {
        /**
         * OBSOLETE
         * if( true == getLog().isDebugEnabled() ) { getLog().debug(
         * arg1 + " " + arg2 ); }
         */
        cmdAdd( cmd, arg1 );
        cmdAdd( cmd, arg2 );
    }

  /**
     * Get the path of apt tool depending the OS.
     *
     * @return the path of the apt tool
     */
    private String getAptPath()
    {
        String aptCommand = "apt";
        if ( Os.isFamily( "windows" ) )
        {
            aptCommand = "apt.exe";
        }

        File aptExe;

        // For IBM's JDK 1.2
        if ( Os.isName( "aix" ) )
        {
            aptExe = new File( System.getProperty( "java.home" ) + "/../sh", aptCommand );
        }
        else if ( Os.isFamily( "unix" ) && Os.isFamily( "mac" ) )
        {
            aptExe = new File( System.getProperty( "java.home" ) + "/bin", aptCommand );
        }
        else
        {
            aptExe = new File( System.getProperty( "java.home" ) + "/../bin", aptCommand );
        }

        getLog().debug( "Apt executable=[" + aptExe.getAbsolutePath() + "]" );

        return aptExe.getAbsolutePath();
    }

  /**
   *
   * @param value
   * @return quotedPathArgument
   */
    private String quotedPathArgument( String value )
    {
        String path = value;

        if ( !StringUtils.isEmpty( path ) )
        {
            path = path.replace( '\\', '/' );
            if ( path.indexOf( "\'" ) != -1 )
            {
                String split[] = path.split( "\'" );
                path = "";

                for ( int i = 0; i < split.length; i++ )
                {
                    if ( i != split.length - 1 )
                    {
                        path = path + split[i] + "\\'";
                    }
                    else
                    {
                        path = path + split[i];
                    }
                }
            }
            path = "'" + path + "'";
        }

        return path;
    }
}
