@echo OFF
IF EXIST Attrition.jar (
	java -jar Attrition.jar
) ELSE (
	echo Could not find jar file. Attempting to compile and run from source.
	javac *.java > nul 2> nul
	java Window
	IF ERRORLEVEL 1 (
		echo Couldn't run from source. Exiting.
	)
	del *.class
)
