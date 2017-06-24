import ballerina.lang.files;
import ballerina.lang.strings;

function main(string[] args) {
    //Create a file struct by providing
    files:File target = {path : "test/num.txt"};
    //Delete a file by giving the file struct.
    files:delete(content, target);
}