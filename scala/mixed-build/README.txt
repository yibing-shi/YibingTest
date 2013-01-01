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
1. mvn clean package
2. mvn eclipse:clean eclipse:eclipse
3. import the project in Elipse (should install scala-ide for eclipse first)
4. Run/Debug
