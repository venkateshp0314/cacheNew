# Define the timestamp
$timestamp = Get-Date "2025-24# Define the desired timestamp
$timestamp = Get-Date "2025-01-24T09:00:00"

# Update timestamps for all files except those in the .git directory
Get-ChildItem -Recurse -File | Where-Object { $_.FullName -notmatch "\\.git\\" } | ForEach-Object {
    Set-ItemProperty -Path $_.FullName -Name LastWriteTime -Value $timestamp
}

Write-Host "All file timestamps updated to: $timestamp"
-01T12:00:00"

# Update timestamps for all files except those in the .git directory
Get-ChildItem -Recurse -File | Where-Object { $_.FullName -notmatch "\\.git\\" } | ForEach-Object {
    Set-ItemProperty -Path $_.FullName -Name LastWriteTime -Value $timestamp
}

Write-Host "All file timestamps updated to: $timestamp"
