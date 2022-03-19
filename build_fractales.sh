#usr/bin/bash

set -o allexport
[[ -f .env ]] && source .env
set +o allexport

if [ -z $JAVA_PATH ] || [ -z $JAVAC_PATH ]
then echo "Merci de completer le fichier .env avant d'executer ce script".
exit 1
fi
$JAVAC_PATH --module-path $JAVA_FX_LIB_PATH --add-modules javafx.controls,javafx.media,javafx.fxml -d out/production/Fractales $(find Fractales/src/ -name "*.java") && cp Fractales/src/Vue/*.fxml out/production/Fractales/Vue/ && cp Fractales/src/image/icons/* out/production/Fractales/
