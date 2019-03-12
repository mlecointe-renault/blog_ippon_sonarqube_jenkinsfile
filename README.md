#  sonarqube and jenkinsfile
Code source du tuto Jenkinsfile with Sonarqube !

In **CodeNarc-0.25.2_blog**, run **gradlew install -x test -x signArchives**

Next, in **sonar-groovy-master_blog**, run **mvn -U clean package**

Your plugin is now in **sonar-groovy-master_blog\sonar-groovy-plugin\target\sonar-groovy-plugin-1.6-RC2-SNAPSHOT.jar**

Copy it into your SonarQube plugin directory **sonarqube-6.7.6\extensions\plugins** and restart your SonarQube

Use the sample file **jenkinsfile** in a project, then analyse :

`mvn -U clean package sonar:sonar -Dsonar.sources=jenkinsfile,pom.xml,src/main`

or

```mvn -U clean package sonar:sonar -Dsonar.sources=. -Dsonar.inclusions=*enkins*,src/main/** -Dsonar.exclusions=/target/**```
