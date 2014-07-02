@echo OFF
javac *.java
IF ERRORLEVEL 1 (
	del *.class
	exit /b
) ELSE (
	jar cvfm Attrition.jar Manifest.txt *.class resources
	del *.class
)
