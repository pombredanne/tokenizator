tokenizator
========

This Java software converts source code in different source code languages
into a token format that is useful for detecting code similarities.


Getting started
===============

- Download the most recent commit of the code
- Open the project using NetBeans (requires Java 1.6 or above)
- Run the ```tokenizator/Tokenize.java``` file to see a demonstration


Adding new languages
====================

You can add new languages by yourself. Look on the two files
inside the ```tokenizedLanguages/Java``` and copy them to a
different folder that represents the language you want to add.

The two files do the following:
1) CodeSplit -> Picks a source code file, breaks into relevant snippets of code
2) CodeConvertToTokens -> for each snippet, convert the code to tokens

The Java example contains more details to help you getting started.

When you've finished the writing the new tokenizator, add the new feature
inside the ```tokenizator/Tokenize.java```.

If something is not clear, just ask. :-)


Testing new languages
=====================

For each language that is tokenized you find a JUnit test case
inside the ```Test packages``` folder.

For example, the Java test cases is ```JavaCodeTest.java```.
You can run the test case by right-clicking the file and selecting "run".


License
=======
Except where otherwise noted, this code is available as open source
through the European Public License without the appendix paragraph.

Read the LICENSE file for details.

