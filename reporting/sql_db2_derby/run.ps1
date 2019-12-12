docker run -itd --name db2test `
    --privileged=true `
    -p 50000:50000 `
    -e LICENSE=accept `
    -e DB2INST1_PASSWORD=admin `
    db2integration
