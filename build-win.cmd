@echo off
where mvn || path %~dp0target\apache-maven-3.9.9\bin;%path%

where mvn || (
  IF NOT EXIST target (
    mkdir target
  )
  pushd target && (
    curl -O https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip
    tar -xvf apache-maven-3.9.9-bin.zip
    popd
  )
)
mvn package
