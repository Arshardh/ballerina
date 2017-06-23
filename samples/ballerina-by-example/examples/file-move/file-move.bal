import ballerina.lang.files;

function main(string[] args) {
    //initialize file struct
    files:File source = {path : "/tmp/source.txt"};
    files:File destination = {path : "/tmp/destination.txt"};
    //move source file to destination
    files:move(source, destination);
}