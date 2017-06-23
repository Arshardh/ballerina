import ballerina.lang.files;
import ballerina.lang.blobs;
import ballerina.lang.system;

function main(string[] args) {
    blob content;
    int n;
    //initialize file struct
    files:File target = {path : "/tmp/content.txt"};
    //open file in read mode
    files:open(target, "r");
    //reads file and returns blob value and no. of bytes read
    content,n = files:read(target, 100000);
    //convert returned blob value to a string
    string s = blobs:toString(content, "utf-8");
    //print read content
    system:println(s);
    //close the file once done
    files:close(target);
}
