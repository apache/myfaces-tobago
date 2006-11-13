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

import org.codehaus.plexus.compiler.util.scan.AbstractSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class AllSourcesInclusionScanner extends AbstractSourceInclusionScanner
{
    /**
     *
     */
    private Set sourceIncludes;
    /**
     *
     */
    private Set sourceExcludes;

    /**
     *
     * @param sourceIncludeSet
     * @param sourceExcludeSet
     */
    public AllSourcesInclusionScanner( Set sourceIncludeSet,
                                       Set sourceExcludeSet )
    {
        this.sourceIncludes = sourceIncludeSet;
        this.sourceExcludes = sourceExcludeSet;
    }

    /**
     *
     * @param sourceDir
     * @param targetDir
     * @return
     * @throws InclusionScanException
     */
    public Set getIncludedSources( File sourceDir, File targetDir )
        throws InclusionScanException
    {
        String[] sourceNames = scanForSources( sourceDir, sourceIncludes, sourceExcludes );
        Set sources = new HashSet();
        for ( int i = 0; i < sourceNames.length; i++ )
        {
            String path = sourceNames[i];
            File sourceFile = new File( sourceDir, path );
            sources.add( sourceFile );
        }
        return sources;
    }
}
