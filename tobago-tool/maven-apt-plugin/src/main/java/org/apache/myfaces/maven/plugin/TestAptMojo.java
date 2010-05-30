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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SingleTargetSourceMapping;

/**
 * @author <a href="mailto:jubu@volny.cz">Juraj Burian</a>
 * @version $Id$
 * @goal testExecute
 * @phase generate-sources
 * @requiresDependencyResolution test
 * @description Generats and/or compiles test sources
 */
public class TestAptMojo extends AbstractAPTMojo
{

    /**
     * The source directory containing the generated sources to be compiled.
     * 
     * @parameter default-value="src/test/gen"
     */
    private String testGenerated;

    /**
     * Set this to 'true' to bypass unit tests entirely. Its use is NOT
     * RECOMMENDED, but quite convenient on occasion.
     * 
     * @parameter expression="${maven.test.skip}"
     */
    private boolean skip;

    /**
     * The source directories containing the test-source to be compiled.
     * 
     * @parameter expression="${project.testCompileSourceRoots}"
     * @required
     * @readonly
     */
    private List compileSourceRoots;

    /**
     * The extra source directories containing the test-source to be processed.
     *
     * @parameter
     */
    private List aptSourceRoots;

    /**
     * Project test classpath.
     * 
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    private List testClasspathElements;

    /**
     * The directory where compiled test classes go.
     * 
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     * @readonly
     */
    private File outputDirectory;

    /**
     * A list of inclusion filters for the compiler.
     * 
     * @parameter
     */
    private Set testIncludes = new HashSet();

    /**
     * A list of exclusion filters for the compiler.
     * 
     * @parameter
     */
    private Set testExcludes = new HashSet();

    public void execute() throws MojoExecutionException
    {
        if ( skip )
        {
            //getLog().info( "Not executing test sources" );
            return;
        }
        else
        {
            super.execute();
            File absoluteGeneratedPath = new File( getProject().getBasedir(), getGenerated() );
            getProject().addTestCompileSourceRoot( absoluteGeneratedPath.getPath() );
            Resource resource = new Resource();
            resource.setFiltering( isResourceFiltering() );
            if ( getResourceTargetPath() != null )
            {
                resource.setTargetPath( getResourceTargetPath() );
            }
            resource.setDirectory( absoluteGeneratedPath.getPath() );
            resource.addExclude( "**/*.java" );
            getProject().addTestResource( resource );
        }
    }

    protected String getGenerated()
    {
        return testGenerated;
    }

    protected List getCompileSourceRoots()
    {
        return compileSourceRoots;
    }

    protected List getClasspathElements()
    {
        return testClasspathElements;
    }

    protected File getOutputDirectory()
    {
        return outputDirectory;
    }

    protected List getAptSourceRoots()
    {
        return aptSourceRoots;
    }

    protected SourceInclusionScanner getSourceInclusionScanner()
    {
        StaleSourceScanner scanner = null;

        if ( testIncludes.isEmpty() )
        {
            testIncludes.add( "**/*.java" );

        }
        if ( isForce() )
        {
            return new AllSourcesInclusionScanner( testIncludes, testExcludes );
        }
        scanner = new StaleSourceScanner( getStaleMillis(), testIncludes, testExcludes );
        if ( getTargetFiles() != null && getTargetFiles().size() > 0 )
        {
            for ( Iterator it = getTargetFiles().iterator() ; it.hasNext() ;)
            {
                String file = (String) it.next();
                scanner.addSourceMapping( new SingleTargetSourceMapping( ".java", file ) );
            }
        }
        else
        {
            scanner.addSourceMapping( new SuffixMapping( ".java", ".class" ) );
        }
        return scanner;
    }
}
