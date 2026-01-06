<!---
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
-->

# Release Checklist

Checklist of tasks to perform for each release. For general information about Apache releases you may also consult
[Publishing Maven Releases](https://infra.apache.org/publishing-maven-artifacts.html).

## Preparation

* You need to add your GPG keys in [Subversion](https://svn.apache.org/repos/asf/myfaces/keys/KEYS) and
  the [Apache site](https://www.apache.org/dist/myfaces/KEYS) before a release. The SVN is read-only in general but the
  KEYS file can be committed. Change the [KEYS](https://dist.apache.org/repos/dist/release/myfaces/KEYS) file as it will
  be copied to the Apache site.
* Ensure that all open bugs and issues in [Jira](https://issues.apache.org/jira/browse/TOBAGO) have been either fixed or
  moved to another release version.
* Ensure that all examples are working with the release candidate.
* Since Tobago 5: Ensure that the script ./test-scenarios-locally.sh runs successfully.
* Check the scheduled version number against "semantic versioning".
* Post a note and ask for problems with the release candidate.
  > Subject: [Tobago] Preparation for the &lt;version> release
  >
  > Hi folks,
  >
  > we plan to build version &lt;version> of Tobago soon. If you know any blocking problems with the current SNAPSHOT,
  > give me a hint.
  >
  > Regards,
  > &lt;name>
* Ensure that there is no dependency with a SNAPSHOT version (e.g. for checkstyle-rules).
* Perform basic checks on an unmodified checkout for all modules. Use JDK 17 or higher for all builds.
  ```
  mvn clean install && mvn checkstyle:check apache-rat:check dependency-check:check
  ```

## Building the Release

* Do not use your forked repository for the following steps to keep the history of the release process.
* Commit: "chore(release): prepare" - check version info in these files manually (set to release version without
  SNAPSHOT suffix):
  * package.json
  * package-lock.json
  * package-info.java (check also version in comment)
  * Release.java (don't add new SNAPSHOT version)
  * ApiController.java
* Prepare the release with (the release-plugin make use of
  the [maven-gpg-plugin](https://maven.apache.org/plugins/maven-gpg-plugin/)):
  ```
  mvn release:prepare
  ```
* Make sure "apache.snapshots.https" and "apache.releases.https" servers are configured in settings.xml. Have a look
  at [Publishing Maven Releases](https://infra.apache.org/publishing-maven-artifacts.html) for more information. Then
  perform the release with:
  ```
  mvn release:perform
  ```
* Commit: "chore(release): set next SNAPSHOT version" - set next SNAPSHOT version in:
  * package.json
  * package-lock.json
  * package-info.java
  * Release.java (add new SNAPSHOT version)

## Staging repository

_Close_ the repository on the [Nexus](https://repository.apache.org/) instance for staging (you will receive a mail with
the staging location).

## Voting

* Propose a vote on the dev list with the staging location.
  > Subject: [VOTE] Release Tobago &lt;version>
  >
  > Hello,
  >
  > we would like to release Tobago &lt;version>.
  >
  > For a detail list please consult the release notes at:
  > https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310273&version=&lt;id-form-jira>
  >
  > The version is available at the staging repository (Nexus) at:
  > https://repository.apache.org/content/repositories/orgapachemyfaces-&lt;id-from-nexus>/
  >
  > Source (sha-256 &lt;value>):
  > https://repository.apache.org/content/repositories/orgapachemyfaces-&lt;id-from-nexus>/org/apache/myfaces/tobago/tobago/&lt;version>/tobago-&lt;version>-source-release.zip
  >
  > Please vote now! (The vote is open for 72h.)
  >
  > [ ] +1<br/>
  > [ ] +0<br/>
  > [ ] -1
  >
  > Regards,<br/>
  > &lt;name>
* For a positive result wait at least 72 hours.
* Once a vote is successful, post the result to the dev list.
  If the result mail should also count as a vote, add a "this email counts as my binding +1". To make it even clearer,
  also send a standard +1 voting mail.

## Publishing

* _Release_ the version in [Jira](https://issues.apache.org/jira/) and close all resolved issues for the release.
* _Release_ and drop the staging repository on the [Nexus](https://repository.apache.org/) instance.
* Copy the download artifacts from the repository to the site (see script ./release-artifacts.sh)
* Add the release version and date to
  the [Apache Committee Report Helper](https://reporter.apache.org/addrelease.html?myfaces).
  Full version name is "tobago-{VERSION}".
* Update _myfaces-homepage_ repository
  * Checkout: https://github.com/apache/myfaces-homepage.git
  * Update the next SNAPSHOT version on tobago-demo.apache.org.
    * Edit versions in:
      * tobago/tobago-vm/apache-proxy/index.html
      * tobago/tobago-vm/docker-compose.yml
  * Updating Tag Library Documentation (TLD).
    * generate in myfaces-tobago folder with:<br/>
      ```
      mvn clean package -Pgenerate-assembly
      ```
    * Copy from "myfaces-tobago/tobago-core/target/tlddoc" to "myfaces-homepage/tobago/doc/{tobago-version}/tld"
  * Updating "tobago.md" and "tobago-download.md".
    * myfaces-homepage/tobago.md
    * myfaces-homepage/tobago-download.md
  * Commit and push: "Tobago {tobago-version} release"
    * description:
      ```
      * update demos
      * update TLDs
      * update readmes
      ```
* Updating the release and version information in other branches, e.g.:
  * tobago-example/tobago-example-demo/src/main/java/org/apache/myfaces/tobago/example/demo/Release.java
* Remove old download artifacts from the site (see script ./drop-artifacts.sh). Older releases are automatic available
  in the [archive](http://archive.apache.org/dist/myfaces/).
* Remove old snapshots in the maven snapshot repository. This is done automatically, it is only needed, when there are
  dead development ends, e.g. last alpha or beta version like 3.0.0-beta-2-SNAPSHOT which will no longer developed.
* Create and send announcement
  > Subject: [ANNOUNCE] Apache Tobago &lt;version> released
  >
  > The Apache MyFaces team is pleased to announce the release of Apache Tobago <version>.
  >
  > Apache Tobago is a component library for JavaServer Faces (JSF) that allows to write web-applications without the
  need of coding HTML, CSS and JavaScript.
  >
  > Main new features<br/>
  > &#8203;-----------------<br/>
  > &lt;list of main new features here or remove this section>
  >
  > Changes<br/>
  > &#8203;-------<br/>
  > Please check the release notes
  at https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310273&version=&lt;id-form-jira> for a full list
  of the changes in this version.
  >
  > Known limitations and bugs<br/>
  > &#8203;--------------------------<br/>
  > &lt;add main limitations and known bugs here or remove this section>
  >
  > For more information about Apache Tobago, please visit https://myfaces.apache.org/#/tobago.
  >
  > Have fun,
  > -The MyFaces team
