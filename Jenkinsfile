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
                        values 'jdk_17_latest'
                    }
                }

                tools {
                    maven "maven_latest"
                    jdk "${JAVA_VERSION}"
                }

                stages {
                    stage('BuildAndTest') {
                        steps {
                            sh "mvn -U clean package checkstyle:check apache-rat:check dependency-check:check -Pgenerate-assembly -Pfrontend"
                        }
                        post {
                            always {
                               junit(testResults: '**/tobago-*/target/surefire-reports/*.xml', allowEmptyResults: true)
                               junit(testResults: '**/tobago-*/target/failsafe-reports/*.xml', allowEmptyResults: true)
                               archiveArtifacts '**/target/*.jar'
                            }
                        }
                    }
                }
            }
        }
        stage('Deploy') {
            tools {
                maven "maven_latest"
                jdk "jdk_17_latest"
            }
            steps {
                sh "mvn clean deploy -Pgenerate-assembly -Ptomcat -Pfrontend"
            }
        }
    }
  post {
    // If this build failed, send an email to the list.
    failure {
      script {
        emailext(
            to: "notifications@myfaces.apache.org",
            recipientProviders: [[$class: 'DevelopersRecipientProvider']],
            from: "Mr. Jenkins <jenkins@builds.apache.org>",
            subject: "Jenkins job ${env.JOB_NAME}#${env.BUILD_NUMBER} failed",
            body: """
There is a build failure in ${env.JOB_NAME}.

Build: ${env.BUILD_URL}
Logs: ${env.BUILD_URL}console
Changes: ${env.BUILD_URL}changes

--
Mr. Jenkins
Director of Continuous Integration
"""
        )
      }
    }

    // If this build didn't fail, but there were failing tests, send an email to the list.
    unstable {
      script {
        emailext(
            to: "notifications@myfaces.apache.org",
            recipientProviders: [[$class: 'DevelopersRecipientProvider']],
            from: "Mr. Jenkins <jenkins@builds.apache.org>",
            subject: "Jenkins job ${env.JOB_NAME}#${env.BUILD_NUMBER} unstable",
            body: """
Some tests have failed in ${env.JOB_NAME}.

Build: ${env.BUILD_URL}
Logs: ${env.BUILD_URL}console
Changes: ${env.BUILD_URL}changes

--
Mr. Jenkins
Director of Continuous Integration
"""
        )
      }
    }

    // Send an email, if the last build was not successful and this one is.
    fixed {
      script {
        emailext(
            to: "notifications@myfaces.apache.org",
            recipientProviders: [[$class: 'DevelopersRecipientProvider']],
            from: 'Mr. Jenkins <jenkins@builds.apache.org>',
            subject: "Jenkins job ${env.JOB_NAME}#${env.BUILD_NUMBER} back to normal",
            body: """
The build for ${env.JOB_NAME} completed successfully and is back to normal.

Build: ${env.BUILD_URL}
Logs: ${env.BUILD_URL}console
Changes: ${env.BUILD_URL}changes

--
Mr. Jenkins
Director of Continuous Integration
"""
        )
      }
    }
  }
}
