#~/bin/sh
echo "we are in the directory: " $PWD
echo "tag: " $tag
echo "BRANCH_OR_TAG: " $BRANCH_OR_TAG
git pull
if [ $tag != 'origin/master'  ] && [ $tag != 'master' ]; then
#  git checkout tags/$tag
#this is for branch checkout for now
	echo "checkout of" $tag
	git checkout $tag
fi
git pull

# Function to check if wildfly is up #
function wait_for_server() {
  until `/opt/wildfly/bin/jboss-cli.sh -c --controller=localhost:19990 ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}

echo "=> build application"

echo ant -file build.xml -Ddebug=false -DBRANCH_OR_TAG=${BRANCH_OR_TAG} -Dtiername=${tiername} -DCADSR.DS.USER=${CADSR_DS_USER} -DCADSR.DS.PSWD=******** -DCADSR.DS.TNS.HOST=${CADSR_DS_TNS_HOST} -DCADSR.DS.TNS.SID=${CADSR_DS_TNS_SID} -DJDEBUG=ON -DCADSR.DS.TNS.PORT=${CADSR_DS_TNS_PORT} -DLINKS.PCS.HOST.URL=${LINKS_PCS_HOST_URL} -LINKS.PCS.NAME=${LINKS_PCS_NAME} dist
ant -file build.xml -Ddebug=false -DBRANCH_OR_TAG=${BRANCH_OR_TAG} -Dtiername=${tiername} -DCADSR.DS.USER=${CADSR_DS_USER} -DCADSR.DS.PSWD=${CADSR_DS_PSWD} -DCADSR.DS.TNS.HOST=${CADSR_DS_TNS_HOST} -DCADSR.DS.TNS.ENTRY=${CADSR_DS_TNS_ENTRY} -DJDEBUG=ON -DCADSR.DS.TNS.PORT=${CADSR_DS_TNS_PORT} -DLINKS.PCS.HOST.URL=${LINKS_PCS_HOST_URL} -DLINKS.PCS.NAME=${LINKS_PCS_NAME} dist

echo "=> starting wildfly in background"
/opt/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &

echo "=> Waiting for the server to boot"
wait_for_server

echo "=> deploying modules"
cp dist/cdecurate.war /local/content/cdecurate/bin
cp dist/ojdbc7-12.1.0.2.0.jar /local/content/cdecurate/bin

/opt/wildfly/bin/jboss-cli.sh -c --controller=localhost:9990 --file=dist/cdecurate_modules.cli

echo "=> reloading wildfly"
/opt/wildfly/bin/jboss-cli.sh --connect command=:reload

echo "=> Waiting for the server to reload"
wait_for_server

echo "=> deploying"
/opt/wildfly/bin/jboss-cli.sh -c --controller=localhost:9990 --file=dist/cdecurate_deploy.cli

echo "=> shutting wildfly down"
/opt/wildfly/bin/jboss-cli.sh --connect command=:shutdown

echo "=> starting wildfly in foreground"
/opt/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 
