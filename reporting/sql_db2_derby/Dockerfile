# use latest db2 for testing
FROM ibmcom/db2
# create custom directory, all scripts in this folder
# will be run automatically by db2 when setting up
RUN mkdir -p /var/custom
# copy all sql files
COPY . /var/custom
# make db2 script runnable
RUN chmod a+x /var/custom/setup.sh
