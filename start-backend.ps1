$ErrorActionPreference = "Stop"

$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-11.0.31.11-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Set-Location "$PSScriptRoot\backend"
.\mvnw.cmd spring-boot:run
