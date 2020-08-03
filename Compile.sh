sources="src/main/AlphabetDialog.java src/main/CryptoUnit.java src/main/Frame.java src/main/JTextFieldLimit.java src/main/Main.java"
outputDir=build

echo "Sources: $sources"

mkdir build
rm -rf build/*

cp resources/Dictionary.txt build/Dictionary.txt

javac -d $outputDir src/main/*.java