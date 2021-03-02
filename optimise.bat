@echo off
setlocal
setlocal EnableDelayedExpansion
for /f "tokens=2 skip=4" %%a in ('tasklist')  do (
  echo taskkill /pid %%a
  )
endlocal