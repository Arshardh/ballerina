import ballerina.lang.files;
import ballerina.lang.blobs;
import ballerina.lang.system;
import ballerina.lang.strings;

function main(string[] args) {

    blob content;
    //create file struct
    files:File target = {path : "/tmp/result.txt"};
    //open file in write mode
    files:open(target, "w");
    //prepare blob content to write
    content = strings:toBlob("Sample Content", "utf-8");
    //write file
    files:write(content, target);
    system:println("file written: /tmp/result.txt");
    files:close(target);

    //Get a boolean value whether the file exists.
    boolean b = files:exists(target);
    system:println("file existence: " + b);

    int n;
    //open file in read mode
    files:open(target, "r");
    //reads file and returns blob value and number of bytes read
    content,n = files:read(target, 100000);
    //convert returned blob value to a string
    string s = blobs:toString(content, "utf-8");
    //print read content
    system:println("file content: " + s);
    //close the file once done
    files:close(target);

    files:File source = {path : "/tmp/result.txt"};
    files:File destination = {path : "/tmp/copy.txt"};
    //copy source file to destination
    files:copy(source, destination);
    system:println("file copied: /tmp/result.txt to /tmp/copy.txt");

    //Delete a file by giving the file struct.
    files:delete(destination);
    system:println("file deleted: /tmp/copy.txt");

    destination = {path : "/tmp/move.txt"};
    //move source file to destination
    files:move(source, destination);
    system:println("file moved: /tmp/result.txt to /tmp/move.txt");

    files:delete(destination);
    system:println("file deleted: /tmp/move.txt");
}
