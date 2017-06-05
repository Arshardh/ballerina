import ballerina.lang.files;

function testCopy(files:File source, files:File dest) {
    files:copy(source, dest);
}

function testMove(files:File source, files:File dest) {
    files:move(source, dest);
}

function testDelete(files:File target) {
    files:delete(target);
}

function testOpen(files:File source) {
    files:open(source);
}

function testWrite(blob content, files:File source) {
    files:write(content, source);
}