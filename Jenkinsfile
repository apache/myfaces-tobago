/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

pipeline {
    agent {
        label 'ubuntu'
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timeout(time: 2, unit: 'HOURS')
        timestamps()
        quietPeriod(30)
    }

    triggers {
        cron('@daily')
        pollSCM('@hourly')
    }

    stages {
        stage('Prepare') {
            matrix {
                agent {
                  label 'ubuntu'
                }
                axes {
                    axis {
                        name 'JAVA_VERSION'
                        values 'JDK 1.8 (latest)', 'JDK 11 (latest)'
                    }
                }

                tools {
                    maven "Maven (latest)"
                    jdk "${JAVA_VERSION}"
                }

                stages {
                    stage('BuildAndTest') {
                        steps {
                            sh "mvn clean deploy checkstyle:check apache-rat:check animal-sniffer:check dependency-check:check  -Pgenerate-assembly -Ptomcat"
                        }
                        post {
                            always {
                               junit(testResults: '**/surefire-reports/*.xml', allowEmptyResults: true)
                               junit(testResults: '**/failsafe-reports/*.xml', allowEmptyResults: true)
                               archiveArtifacts '**/target/*.jar'
                            }
                        }
                    }
                }
            }
        }
    }
}