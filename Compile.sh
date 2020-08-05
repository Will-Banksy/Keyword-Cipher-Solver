sources="src/main/AlphabetDialog.java src/main/CryptoUnit.java src/main/Frame.java src/main/JTextFieldLimit.java src/main/Main.java"
outputDir=build

# If the build directory doesn't already exist, create it
if [ ! -d "build" ]; then
	mkdir build
fi
# Make sure the build directory is clean
rm -rf build/*

cp resources/Dictionary.txt build/Dictionary.txt

javac -d $outputDir src/main/*.java

echo "Compiled Successfully"