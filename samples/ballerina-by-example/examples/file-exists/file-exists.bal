import ballerina.lang.files;

function main(string[] args) (boolean) {
    //Create a file struct by providing.
    files:File target = {path : "test/num.txt"};
    //Get a boolean value whether the file exists.
    boolean b = files:exists(target);
    //Return the obtained boolean value.
    return b;
}