import ballerina.lang.files;
import ballerina.lang.strings;

function main(string[] args) {
    blob content;
    //initialize file struct
    files:File target = {path : "/tmp/result.txt"};
    //open file in write mode
    files:open(target, "w");
    //prepare blob content to write
    content = strings:toBlob("Sample Content", "utf-8");
    //write file
    files:write(content, target);
    //close file when done
    files:close(target);
}