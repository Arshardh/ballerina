# To run the program, put the code in `file-copy.bal`
# and use `$BALLERINA_HOME/bin/ballerina`.

$ $BALLERINA_HOME/bin/ballerina echo "sample" > /tmp/source.txt
$ $BALLERINA_HOME/bin/ballerina file-copy.bal

$ $BALLERINA_HOME/bin/ballerina cat /tmp/destination.txt
sample

# Now that we can run basic Ballerina programs, let's
# learn more about the language.
