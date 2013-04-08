# Xebia Essentials for Android

This repository contains the source code for the Xebia-essentials
Android app available from [Google Play][1].

![Xebia Essentials for Android screenshots][2]

**Unofficial application** that interacts with the [Xebia Essentials][3] cards.
You should own the cards to use the application.


## Features

* Scan your Xebia essentials cards from everywhere - no Internet connection required
* Share cards with others
* Designed for phones and tablets
* Browse all cards, or use the search functionality
* Safe - no permissions required


## Building

### Requirements before building

* Before building the app, you must go to the project root directory and type the following to specify the SDK directory (only required once).

    `android update project --path . --target android-17`

* You must also create a `data.xml` file in the `assets` folder that will contain all the Xebia Essentials Cards data.
See the `data.xml.example` template.
We do not provide the original data file in the GitHub repository, as it is a property of Xebia.

* The build requires [Maven][4] and the [Android SDK][5] must be installed in your development environment.
In addition you will need to set the `ANDROID_HOME` environment variable to the location of your SDK:

    `export ANDROID_HOME=/home/user/tools/android-sdk`

* You must also install the latest support-v4 library in you Maven repository

    `cd ${ANDROID_HOME}/extras/android/support/v4`
    `mvn install:install-file -Dfile=./android-support-v4.jar -DgroupId=com.google.android -DartifactId=support-v4 -Dversion=r12 -Dpackaging=jar`


### Building with Maven only

After satisfying those requirements, the build is pretty simple:

* Run `mvn clean package` from the `app` directory to build the APK only
* Run `mvn clean install` from the root directory to build the app


### Building with Eclipse ###

* Open the `pom.xml` and remove the support-v4 + ActionBarSherlock + android-drawer dependencies
* Download [ActionBarSherlock][6] and import it to your Eclipse workspace using `File > New > Other > Android Project from Existing Code`
* Replace ActionBarSherlock's support library by the latest support-v4 library
* Download [SimonVT's android-menudrawer][7] and import it to your Eclipse workspace using `File > Import > Existing Maven Projects`
* Import Xebia-essentials to your Eclipse workspace the same way (`File > Import > Existing Maven Projects`)
* Right click on xebia-essentials > `Properties > Android` and add actionbarsherlock + android-menudrawer library references
* If you have many error (`Class_ cannot be resolved to a type`), right click on xebia-essentials > `Properties > Java Compiler > Annotation Processing`. Untick "Enable project specific settings", click on apply, and tick again. Select yes when eclipse asks you to rebuild the project
* Then refresh, clean, and rebuild all projects.


### Generate the `ormlite_config.txt` file

This is not required, unless you have modified the database scheme.
If you have modified the database structure, run the following command to generate the new ORMLite configuration:

    mvn clean install exec:java -Dexec.mainClass="com.nilhcem.xebia.essentials.core.DatabaseConfigUtil"

See [this link][8] for more information


## Acknowledgements

This project uses many great open-source libraries, such as:

* [ActionBarSherlock][6]
* [OrmLite][9]
* [AndroidAnnotations][10]
* [Zxing][11]
* [android-menudrawer][7]

These are just a few of the major dependencies, the entire list of dependencies
is listed in the [app's POM file][12]


## License

    Copyright (c) 2012-2013, Gautier <Nilhcem> MECHLING
    All rights reserved.
    
    Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
    * Neither the name of the original author nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
    
    THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
    LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
    EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

[1]: https://play.google.com/store/apps/details?id=com.nilhcem.xebia.essentials
[2]: http://nilhcem.github.com/screenshots/xebia-essentials.png
[3]: http://essentials.xebia.com/
[4]: http://maven.apache.org/download.html
[5]: http://developer.android.com/sdk/index.html
[6]: http://actionbarsherlock.com
[7]: https://github.com/SimonVT/android-menudrawer
[8]: http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_4.html
[9]: http://ormlite.com/
[10]: http://androidannotations.org/
[11]: http://code.google.com/p/zxing/
[12]: https://github.com/Nilhcem/xebia-essentials-android/blob/master/pom.xml
