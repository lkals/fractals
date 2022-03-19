#usr/bin/bash

set -o allexport
[[ -f .env ]] && source .env
set +o allexport

if [ -z $JAVA_PATH ] || [ -z $JAVA_FX_LIB_PATH ]
then echo "Merci de completer le fichier .env avant d'executer ce script".
exit 1
fi

${JAVA_PATH} --module-path ${JAVA_FX_LIB_PATH} --add-modules javafx.controls,javafx.media,javafx.fxml -Djava.library.path=${JAVA_FX_LIB_PATH} -Dfile.encoding=UTF-8 -classpath out/production/Fractales:${JAVA_FX_LIB_PATH}/src.zip:${JAVA_FX_LIB_PATH}/javafx-swt.jar:${JAVA_FX_LIB_PATH}/javafx.web.jar:${JAVA_FX_LIB_PATH}/javafx.base.jar:${JAVA_FX_LIB_PATH}/javafx.fxml.jar:${JAVA_FX_LIB_PATH}/javafx.media.jar:${JAVA_FX_LIB_PATH}/javafx.swing.jar:${JAVA_FX_LIB_PATH}/javafx.controls.jar:${JAVA_FX_LIB_PATH}/javafx.graphics.jar application.Main "$@"
