package org.apache.myfaces.maven.plugin;

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

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;

/**
 * @author <a href="mailto:jubu@volny.cz">Juraj Burian</a>
 * @version $Id:$
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
        if( skip )
        {
            //getLog().info( "Not executing test sources" );
            return;
        } else
        {
            super.execute();
            project.addTestCompileSourceRoot( getGenerated() );
            Resource resource = new Resource();
            resource.setDirectory( getGenerated() );
            resource.addExclude( "**/*.java" );
            project.addTestResource( resource );
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

    protected SourceInclusionScanner getSourceInclusionScanner()
    {
        StaleSourceScanner scanner = null;

        if( true == testIncludes.isEmpty() )
        {
            testIncludes.add( "**/*.java" );
            scanner = new StaleSourceScanner( staleMillis, testIncludes,
                    testExcludes );
        }
        scanner.addSourceMapping( new SuffixMapping( ".java", ".class" ) );
        return scanner;
    }
}
