This project show how to build java scala mixed project.
In this project, java codes depend on scala code.
To build a project where scala codes depend on java codes, use the following 
to replace the corresponding part in pom.xml.

      <plugin>
        <groupId>org.scala-tools</groupId>
        <artifactId>maven-scala-plugin</artifactId>
        <executions>
          <execution>
            <goals>
              <goal>compile</goal>
              <goal>testCompile</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <jvmArgs>
            <jvmArg>-Xms64m</jvmArg>
            <jvmArg>-Xmx1024m</jvmArg>
          </jvmArgs>
        </configuration>
      </plugin>

===Build===
mvn clean package

===Open project in eclipse===
1. mvn eclipse:clean eclipse:eclipse
2. import the project in Elipse (should install scala-ide for eclipse first)

===Open project in IntelliJ===
1. Install scala and sbt plugin for IntelliJ
2. Open/Import this maven project in IntelliJ
