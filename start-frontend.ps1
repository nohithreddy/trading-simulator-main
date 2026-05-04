$ErrorActionPreference = "Stop"

$env:BROWSER = "none"
Set-Location "$PSScriptRoot\frontend"
npm start
