/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.addressbook;

//import org.springframework.instrument.classloading.SimpleInstrumentableClassLoader;
//import org.springframework.instrument.classloading.SimpleLoadTimeWeaver;


/**
 * <a href ="https://issues.apache.org/jira/browse/OPENJPA-40">COPY FROM OPENJPA-40</a>
 * OpenJPA-specific SimpleLoadTimeWeaver which excludes all the core
 * <em>org.apache.openjpa</em> packages from being instrumented. This
 * is important, since enhanced classes will wind up implementing
 * <em>org.apache.openjpa.enhance.PersistenceCapable</em>, so it there
 * is a mismatch between the PersistenceCapable that is loaded by the
 * system classloader and the one that is loaded by the 
 * instrumenting ClassLoader, then OpenJPA will not function properly.
 *
 * <p>
 *
 * This loader can be specific by including the following in the 
 * spring configuration XML as follows:
 *
 * <p>
 *
 * <code>
 *  &lt;bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean"&rt;
 *  ...
 *    &lt;property name="loadTimeWeaver"&rt;
 *        &lt;bean class="org.apache.myfaces.tobago.example.addressbook.OpenJpaLoadTimeWeaver"/&rt;
 *    &lt;/property&rt;
 *    &lt;/bean&rt;
 * </code>
 * 
 * author Marc Prud'hommeaux
 * @since 2.0
 */
/*
public class OpenJpaLoadTimeWeaver extends SimpleLoadTimeWeaver {

    public OpenJpaLoadTimeWeaver() {
        super();
        ((SimpleInstrumentableClassLoader) getInstrumentableClassLoader()).
            excludePackage("org.apache.openjpa");
    }

    public OpenJpaLoadTimeWeaver(SimpleInstrumentableClassLoader loader) {
        super(loader);
        ((SimpleInstrumentableClassLoader) getInstrumentableClassLoader()).
            excludePackage("org.apache.openjpa");
    }
}
*/
