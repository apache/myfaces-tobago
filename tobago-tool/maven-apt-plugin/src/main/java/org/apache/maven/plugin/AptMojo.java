package org.apache.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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

import org.apache.maven.model.Resource;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SingleTargetSourceMapping;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

/**
 * @author <a href="mailto:jubu@volny.cz">Juraj Burian</a>
 * @version $Id:$
 * @goal execute
 * @phase generate-sources
 * @requiresProject
 * @requiresDependencyResolution compile
 * @description generates and/or compiles application sources
 */
public class AptMojo extends AbstractAPTMojo
{

    /**
     * The source directory containing the generated sources.
     * 
     * @parameter default-value="src/main/gen"
     */
    private String generated;

    /**
     *  A List of tagetFiles for SingleSourceTargetMapping
     *
     * @parameter
     */
    private List targetFiles;
    /**
     * The source directories containing the sources to be compiled.
     * 
     * @parameter expression="${project.compileSourceRoots}"
     * @required
     * @readonly
     */
    private List compileSourceRoots;

    /**
     * Project classpath.
     * 
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    private List classpathElements;

    /**
     * The directory for compiled classes.
     * 
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     * @readonly
     */
    private File outputDirectory;


    protected String getGenerated()
    {
        return generated;
    }

    protected List getCompileSourceRoots()
    {
        return compileSourceRoots;
    }

    protected List getClasspathElements()
    {
        return classpathElements;
    }

    protected File getOutputDirectory()
    {
        return outputDirectory;
    }

    public void execute() throws MojoExecutionException
    {
        super.execute();
        project.addCompileSourceRoot( getGenerated() );
        Resource resource = new Resource();
        resource.setDirectory( getGenerated() );
        resource.addExclude( "**/*.java" );
        project.addResource( resource );
    }

    /**
     * A list of inclusion filters for the compiler.
     * 
     * @parameter
     */
    private Set includes = new HashSet();

    /**
     * A list of exclusion filters for the compiler.
     * 
     * @parameter
     */
    private Set excludes = new HashSet();

    protected SourceInclusionScanner getSourceInclusionScanner()
    {
        StaleSourceScanner scanner = null;

        if( includes.isEmpty() )
        {
            includes.add( "**/*.java" );
        }
        scanner = new StaleSourceScanner( staleMillis, includes, excludes );
        if ( targetFiles.size() > 0 )
        {
            for ( Iterator it = targetFiles.iterator() ; it.hasNext() ; )
            {
                String file = (String) it.next();
                scanner.addSourceMapping(new SingleTargetSourceMapping(".java", file));
            }
        }
        else
        {
            scanner.addSourceMapping( new SuffixMapping( ".java", ".class" ) );
        }
        return scanner;
    }
}